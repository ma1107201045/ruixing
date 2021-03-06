package com.yintu.ruixing.yuanchengzhichi.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseService;
import com.yintu.ruixing.guzhangzhenduan.SkylightTimeService;
import com.yintu.ruixing.master.guzhangzhenduan.CheZhanDao;
import com.yintu.ruixing.master.yuanchengzhichi.AlarmDao;
import com.yintu.ruixing.yuanchengzhichi.AlarmEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/9 21:02
 * @Version: 1.0
 */
@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmDao alarmDao;
    @Autowired
    private CheZhanDao cheZhanDao;
    @Autowired
    private BaoJingYuJingBaseService baoJingYuJingBaseService;
    @Autowired
    private SkylightTimeService skylightTimeService;

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            alarmDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void edit(AlarmEntity alarmEntity) {
        alarmDao.updateByPrimaryKeySelective(alarmEntity);
    }

    @Override
    public void edit(Integer id, Integer idea, String remark) {
        AlarmEntity alarmEntity = this.findById(id);
        if (alarmEntity != null) {
            if (alarmEntity.getAlarmTicketId() == null)
                throw new BaseRuntimeException("请先填写处置单再修改维护单位意见");
            alarmEntity.setIdea(idea);
            alarmEntity.setRemark(remark);
            this.edit(alarmEntity);
        }
    }

    @Override
    public void edit(Integer id, Integer faultStatus, Integer disposeStatus, Integer alarmTicketId) {
        AlarmEntity alarmEntity = this.findById(id);
        if (alarmEntity != null) {
            alarmEntity.setFaultStatus(faultStatus);
            alarmEntity.setDisposeStatus(disposeStatus);
            alarmEntity.setAlarmTicketId(alarmTicketId);
            this.edit(alarmEntity);
        }
    }

    @Override
    public AlarmEntity findById(Integer id) {
        AlarmEntity alarmEntityQuery = new AlarmEntity();
        alarmEntityQuery.setId(id);
        List<AlarmEntity> alarmEntities = this.findByExample(null, null, null, null, null, alarmEntityQuery, null);
        return alarmEntities.stream().findFirst().orElse(null);
    }

    @Override
    public List<AlarmEntity> findByExample(Integer tid, Integer did, Integer xid, Date beginTime, Date endTime, AlarmEntity alarmEntityQuery, String xtType) {
        int bt;
        int dt;
        //多条件查询
        if (beginTime == null && endTime == null) {//如果前台时间区间为空，则查询当前数据（默认）
            Date now = DateUtil.date();
            Date b = DateUtil.beginOfDay(now);
            Date e = DateUtil.endOfDay(now);
            bt = (int) (b.getTime() / 1000);
            dt = (int) (e.getTime() / 1000.0);
        } else if (beginTime != null && endTime != null) {
            if (beginTime.after(endTime))
                throw new BaseRuntimeException("开始时间不能小于结束时间");
            long interval = DateUtil.between(beginTime, endTime, DateUnit.HOUR);
            if (interval > 48)
                throw new BaseRuntimeException("查询时间最大为48h");
            bt = (int) (beginTime.getTime() / 1000);
            dt = (int) (endTime.getTime() / 1000.0);
        } else {
            throw new BaseRuntimeException("时间选择有误有误");
        }
        List<AlarmEntity> alarmEntities = alarmDao.selectByExample(tid, did, xid, alarmEntityQuery.getStationId(), alarmEntityQuery.getSectionId(), bt, dt, alarmEntityQuery.getId(), xtType,
                alarmEntityQuery.getAlarmlevel(), alarmEntityQuery.getFaultStatus(), alarmEntityQuery.getDisposeStatus(), alarmEntityQuery.getIdea());
        for (AlarmEntity alarmEntity : alarmEntities) {
            String context;
            Integer number = cheZhanDao.findCzNumber(alarmEntity.getStationId());
            if (alarmEntity.getAlarmlevel() == 1) {
                context = baoJingYuJingBaseService.findAlarmContext(alarmEntity.getAlarmcode(), number == 0 ? 1 : 2);
            } else {
                context = baoJingYuJingBaseService.findAlarmContext(alarmEntity.getAlarmcode(), 3);
            }
            alarmEntity.setBjcontext(context);
            //判断故障状态是否是天窗状态
            if (alarmEntity.getFaultStatus() != 2 && alarmEntity.getAlarmTicketId() == null) {
                boolean isSkylightTime = skylightTimeService.isSkylightTime(alarmEntity.getStationId(), alarmEntity.getSectionId(), new Date(alarmEntity.getCreatetime() * 1000L));
                if (isSkylightTime) {
                    alarmEntity.setFaultStatus(2);
                    this.edit(alarmEntity);
                }
            }
        }
        return alarmEntities;
    }

    @Override
    public List<AlarmEntity> findByDisposeStatus() {
        List<AlarmEntity> alarmEntities = alarmDao.selectByDisposeStatus();
        for (AlarmEntity alarmEntity : alarmEntities) {
            String context;
            Integer number = cheZhanDao.findCzNumber(alarmEntity.getStationId());
            if (alarmEntity.getAlarmlevel() == 1) {
                context = baoJingYuJingBaseService.findAlarmContext(alarmEntity.getAlarmcode(), number == 0 ? 1 : 2);
            } else {
                context = baoJingYuJingBaseService.findAlarmContext(alarmEntity.getAlarmcode(), 3);
            }
            alarmEntity.setBjcontext(context);
        }
        return alarmEntities;
    }

    @Override
    public long count() {
        return alarmDao.count();
    }
}
