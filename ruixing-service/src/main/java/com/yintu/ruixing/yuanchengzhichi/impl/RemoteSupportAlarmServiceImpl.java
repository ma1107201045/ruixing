package com.yintu.ruixing.yuanchengzhichi.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public boolean isTableExist(String tableName) {
        return alarmDao.isTableExist(tableName) > 0;
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
    public List<RemoteSupportAlarmEntity> findByCondition(String tableName, Integer stationId, Date startTime, Date endTime) {
        return alarmDao.selectByCondition(tableName, stationId, startTime, endTime);
    }

    @Override
    public List<RemoteSupportAlarmEntity> findByCondition(Integer pageNumber, Integer pageSize, Integer stationId, Date startTime, Date endTime) {
        if (startTime.after(endTime))
            throw new BaseRuntimeException("开始时间不能大于结束日期");
        List<RemoteSupportAlarmEntity> remoteSupportAlarmEntities = new ArrayList<>();
        if (DateUtil.month(startTime) == DateUtil.month(endTime)) {//月份相等
            String tableName = StringUtil.getBaoJingYuJingTableName(stationId, startTime);
            if (this.isTableExist(tableName)) {
                remoteSupportAlarmEntities = this.findByCondition(tableName, stationId, startTime, endTime);
                if (remoteSupportAlarmEntities.size() > 0)
                    remoteSupportAlarmEntities = remoteSupportAlarmEntities.stream()
                            .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                            .collect(Collectors.toList());
            }
        } else {
            List<RemoteSupportAlarmEntity> list1 = new ArrayList<>();
            List<RemoteSupportAlarmEntity> list2 = new ArrayList<>();
            List<RemoteSupportAlarmEntity> list3 = new ArrayList<>();
            long betweenMonth = DateUtil.betweenMonth(startTime, endTime, true);
            String startTableName = StringUtil.getBaoJingYuJingTableName(stationId, startTime);
            if (betweenMonth == 1L) {
                if (this.isTableExist(startTableName)) {
                    list1 = this.findByCondition(startTableName, stationId, startTime, DateUtil.endOfMonth(startTime));
                    if (list1.size() > 0)
                        list1 = list1.stream()
                                .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                                .collect(Collectors.toList());
                }
                String endTableName = StringUtil.getBaoJingYuJingTableName(stationId, endTime);
                if (this.isTableExist(endTableName)) {
                    list2 = this.findByCondition(endTableName, stationId, DateUtil.beginOfMonth(endTime), endTime);
                    if (list2.size() > 0)
                        list2 = list2.stream()
                                .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                                .collect(Collectors.toList());
                }
            } else {
                if (this.isTableExist(startTableName))
                    list1 = this.findByCondition(startTableName, stationId, startTime, DateUtil.endOfMonth(startTime));
                if (list1.size() > 0)
                    list1 = list1.stream()
                            .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                            .collect(Collectors.toList());


                List<List<RemoteSupportAlarmEntity>> listAll = new ArrayList<>();
                for (long i = 0L; i < betweenMonth - 1L; i++) {
                    startTime = DateUtil.offsetMonth(DateUtil.beginOfMonth(startTime), 1);
                    String tableName = StringUtil.getBaoJingYuJingTableName(stationId, startTime);
                    if (this.isTableExist(tableName)) {
                        List<RemoteSupportAlarmEntity> list = this.findByCondition(startTableName, stationId, startTime, DateUtil.endOfMonth(startTime));
                        if (list.size() > 0) {
                            list = list.stream()
                                    .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                                    .collect(Collectors.toList());
                            listAll.add(list);
                        }
                    }
                }
                for (int i = listAll.size() - 1; i > -1; i--) {
                    list2.addAll(listAll.get(i));
                }

                String endTableName = StringUtil.getBaoJingYuJingTableName(stationId, endTime);
                if (this.isTableExist(endTableName))
                    list3 = this.findByCondition(endTableName, stationId, DateUtil.beginOfMonth(endTime), endTime);
                if (list1.size() > 0)
                    list3 = list3.stream()
                            .sorted(Comparator.comparingLong(RemoteSupportAlarmEntity::getId).reversed())
                            .collect(Collectors.toList());
            }
            if (list3.size() > 0)
                remoteSupportAlarmEntities.addAll(list3);
            if (list2.size() > 0)
                remoteSupportAlarmEntities.addAll(list2);
            if (list1.size() > 0)
                remoteSupportAlarmEntities.addAll(list1);
        }

        Page<RemoteSupportAlarmEntity> page = new Page<>(pageNumber, pageSize);
        page.setTotal(remoteSupportAlarmEntities.size());

        remoteSupportAlarmEntities = remoteSupportAlarmEntities.stream()
                .skip((pageNumber - 1L) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        int number = cheZhanDao.findCzNumber(stationId);
        for (RemoteSupportAlarmEntity remoteSupportAlarmEntity : remoteSupportAlarmEntities) {
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
        page.addAll(remoteSupportAlarmEntities);
        return page;
    }
}
