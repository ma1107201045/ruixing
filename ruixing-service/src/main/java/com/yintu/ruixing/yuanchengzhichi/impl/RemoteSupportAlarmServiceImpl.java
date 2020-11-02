package com.yintu.ruixing.yuanchengzhichi.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseDao;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanDao;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/8 15:01
 */
@Service
public class RemoteSupportAlarmServiceImpl implements RemoteSupportAlarmService {
    @Autowired
    private RemoteSupportAlarmDao alarmDao;
    @Autowired
    private BaoJingYuJingBaseDao baoJingYuJingBaseDao;
    @Autowired
    private CheZhanDao cheZhanDao;
    @Autowired
    private RemoteSupportTicketService remoteSupportTicketService;

    @Value("${spring.datasource.druid.url}")
    private String jdbcURL;

    @Override
    public boolean isTableExist(String tableName) {
        return alarmDao.isTableExist(tableName) > 0;
    }

    @Override
    public List<String> findLikeTable(String databaseName, String tableName) {
        return alarmDao.selectLikeTable(databaseName, tableName);
    }

    @Override
    @Transactional
    public void remove(Integer[] czIds, Integer[] createTimes, Integer[] ids) {
        if (czIds.length == createTimes.length && czIds.length == ids.length) {
            for (int i = 0; i < czIds.length; i++) {
                String tableName = StringUtil.getBaoJingYuJingTableName(czIds[i], new Date(createTimes[i] * 1000L));
                if (this.isTableExist(tableName))
                    alarmDao.deleteByPrimaryKey(tableName, ids[i]);
            }
        }
    }

    @Override
    public RemoteSupportAlarmEntity findById(String tableName, Integer id) {
        RemoteSupportAlarmEntity remoteSupportAlarmEntity = null;
        if (this.isTableExist(tableName)) {
            remoteSupportAlarmEntity = alarmDao.selectByPrimaryKey(tableName, id);
            if (remoteSupportAlarmEntity != null) {
                int number = cheZhanDao.findCzNumber(remoteSupportAlarmEntity.getStationId());
                String context;
                if (remoteSupportAlarmEntity.getAlarmlevel() == 1) {
                    context = baoJingYuJingBaseDao.findAlarmContext(remoteSupportAlarmEntity.getAlarmcode(), number == 0 ? 1 : 2);
                } else {
                    context = baoJingYuJingBaseDao.findAlarmContext(remoteSupportAlarmEntity.getAlarmcode(), 3);
                }
                BaoJingYuJingBaseEntity baoJingYuJingBaseEntity = new BaoJingYuJingBaseEntity();
                baoJingYuJingBaseEntity.setBjcontext(context);
                remoteSupportAlarmEntity.setBaoJingYuJingBaseEntity(baoJingYuJingBaseEntity);
                RemoteSupportTicketEntity remoteSupportTicketEntity = remoteSupportTicketService.findLastByAlarmId(StringUtil.getAssemblyId(remoteSupportAlarmEntity.getStationId(), remoteSupportAlarmEntity.getCreatetime(), remoteSupportAlarmEntity.getId()));
                remoteSupportAlarmEntity.setAlarmStatus(remoteSupportTicketEntity == null ? 1 : remoteSupportTicketEntity.getStatus());
            }
        }
        return remoteSupportAlarmEntity;
    }


    @Override
    public List<RemoteSupportAlarmEntity> findByCondition(Integer pageNumber, Integer pageSize, Integer stationId, Date startTime, Date endTime) {
        List<RemoteSupportAlarmEntity> remoteSupportAlarmEntities = new ArrayList<>();
        List<String> times = new ArrayList<>();
        String databaseName = StrUtil.subBetweenAll(jdbcURL, "/", "?")[0];
        boolean isTime = false;
        //添加所选时间
        if (startTime != null && endTime != null) {
            if (startTime.after(endTime))
                throw new BaseRuntimeException("开始时间不能大于结束日期");
            long diffMonth = DateUtil.betweenMonth(startTime, endTime, true);
            Date diffTime = startTime;
            for (long i = 0; i < diffMonth + 1L; i++) {
                int month = DateUtil.month(diffTime) + 1;
                times.add(DateUtil.year(diffTime) + "" + (month > 9 ? month : "0" + month));
                diffTime = DateUtil.offsetMonth(diffTime, 1);
            }
            isTime = true;
        }
        if (stationId != null) {
            List<String> tables = this.findLikeTable(databaseName, "alarm\\_" + stationId + "\\_%");
            StringBuilder sb = new StringBuilder();
            if (!isTime) {
                for (String table : tables) {
                    sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                }
            } else {//对比时间减少拼接sql
                for (String time : times) {
                    for (String table : tables) {
                        if (("alarm_" + stationId + "_" + time).equals(table)) {
                            sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                            break;
                        }
                    }
                }
            }
            if (!"".equals(sb.toString())) {
                PageHelper.startPage(pageNumber, pageSize);
                remoteSupportAlarmEntities = alarmDao.selectByCondition(sb.toString(), startTime, endTime);
            }
        } else {
            List<String> tables = this.findLikeTable(databaseName, "alarm\\_%" + "\\_%");
            StringBuilder sb = new StringBuilder();
            if (!isTime) {
                for (String table : tables) {
                    sb.append("SELECT * FROM ").append(table).append(" UNION ALL ");
                }
            } else {//对比时间减少拼接sql
                for (String time : times) {
                    for (String table : tables) {
                        if (("alarm_" + table.split("_")[1] + "_" + time).equals(table)) {
                            sb.append("SELECT * FROM ").append(table).append(" UNION ALL ");
                            break;
                        }
                    }
                }
            }
            if (!"".equals(sb.toString())) {
                PageHelper.startPage(pageNumber, pageSize, "createtime DESC");
                remoteSupportAlarmEntities = alarmDao.selectByCondition(sb.toString(), startTime, endTime);
            }
        }
        // return remoteSupportAlarmEntities;
        return remoteSupportAlarmEntities.size() > 0 ? this.findMessage(remoteSupportAlarmEntities, stationId) : remoteSupportAlarmEntities;
    }

    @Override
    public List<RemoteSupportAlarmEntity> findMessage(List<RemoteSupportAlarmEntity> remoteSupportAlarmEntities, Integer stationId) {
        int number = 0;
        String czName = null;
        if (stationId != null) {
            number = cheZhanDao.findCzNumber(stationId);
            czName = cheZhanDao.selectNameById((long) stationId, false);
        }
        //查询报警信息跟处置单
        for (RemoteSupportAlarmEntity remoteSupportAlarmEntity : remoteSupportAlarmEntities) {
            if (stationId == null) {
                stationId = remoteSupportAlarmEntity.getStationId();
                number = cheZhanDao.findCzNumber(stationId);
                czName = cheZhanDao.selectNameById((long) stationId, false);
            }

            CheZhanEntity cheZhanEntity = new CheZhanEntity();
            cheZhanEntity.setCzName(czName);
            remoteSupportAlarmEntity.setCheZhanEntity(cheZhanEntity);

            String context;
            if (remoteSupportAlarmEntity.getAlarmlevel() == 1) {
                context = baoJingYuJingBaseDao.findAlarmContext(remoteSupportAlarmEntity.getAlarmcode(), number == 0 ? 1 : 2);
            } else {
                context = baoJingYuJingBaseDao.findAlarmContext(remoteSupportAlarmEntity.getAlarmcode(), 3);
            }
            BaoJingYuJingBaseEntity baoJingYuJingBaseEntity = new BaoJingYuJingBaseEntity();
            baoJingYuJingBaseEntity.setBjcontext(context);
            remoteSupportAlarmEntity.setBaoJingYuJingBaseEntity(baoJingYuJingBaseEntity);

            RemoteSupportTicketEntity remoteSupportTicketEntity = remoteSupportTicketService.findLastByAlarmId(StringUtil.getAssemblyId(stationId, remoteSupportAlarmEntity.getCreatetime(), remoteSupportAlarmEntity.getId()));
            remoteSupportAlarmEntity.setAlarmStatus(remoteSupportTicketEntity == null ? 1 : remoteSupportTicketEntity.getStatus());
        }
        return remoteSupportAlarmEntities;
    }
}
