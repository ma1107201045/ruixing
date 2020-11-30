package com.yintu.ruixing.weixiudaxiu.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentReprocessedProductManagementDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementEntityWithBLOBs;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/3 16:31
 */
@Service
@Transactional
public class EquipmentReprocessedProductManagementServiceImpl implements EquipmentReprocessedProductManagementService {

    @Autowired
    private EquipmentReprocessedProductManagementDao equipmentReprocessedProductManagementDao;
    @Autowired
    private ScheduleJobService scheduleJobService;


    @Override
    public void add(EquipmentReprocessedProductManagementEntityWithBLOBs entity) {
        equipmentReprocessedProductManagementDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentReprocessedProductManagementDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentReprocessedProductManagementEntityWithBLOBs entity) {
        if (entity.getReturnTime() != null) {
            if (!entity.getReturnTime().after(new Date()))
                throw new BaseRuntimeException("返还时间不能小于或等于当前时间");
            List<ScheduleJobEntity> scheduleJobEntities = scheduleJobService.findByJobName("equipmentReprocessedProductManagementTask" + "-" + entity.getId());
            if (!scheduleJobEntities.isEmpty()) {
                for (ScheduleJobEntity jobEntity : scheduleJobEntities) {
                    jobEntity.setModifiedBy(entity.getModifiedBy());
                    jobEntity.setModifiedTime(entity.getModifiedTime());
                    jobEntity.setExecutionTime(new Date());
                    jobEntity.setCronExpression(String.format("%d %d %d %d %d ? %d",
                            DateUtil.second(entity.getReturnTime()),
                            DateUtil.minute(entity.getReturnTime()),
                            DateUtil.hour(entity.getReturnTime(), true),
                            DateUtil.dayOfMonth(entity.getReturnTime()),
                            DateUtil.month(entity.getReturnTime()) + 1,
                            DateUtil.year(entity.getReturnTime())));
                    scheduleJobService.edit(jobEntity);
                }
                return;
            }
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setCreateBy(entity.getModifiedBy());
            scheduleJobEntity.setCreateTime(entity.getModifiedTime());
            scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
            scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
            scheduleJobEntity.setExecutionTime(new Date());
            scheduleJobEntity.setJobName("equipmentReprocessedProductManagementTask" + "-" + entity.getId());
            scheduleJobEntity.setCronExpression(String.format("%d %d %d %d %d ? %d",
                    DateUtil.second(entity.getReturnTime()),
                    DateUtil.minute(entity.getReturnTime()),
                    DateUtil.hour(entity.getReturnTime(), true),
                    DateUtil.dayOfMonth(entity.getReturnTime()),
                    DateUtil.month(entity.getReturnTime()) + 1,
                    DateUtil.year(entity.getReturnTime())));
            scheduleJobEntity.setBeanName("equipmentReprocessedProductManagementTask");
            scheduleJobEntity.setMethodName("execute");
            scheduleJobEntity.setStatus(1);
            scheduleJobEntity.setDeleteFlag(false);
            scheduleJobService.add(scheduleJobEntity);
        }
        equipmentReprocessedProductManagementDao.updateByPrimaryKeySelective(entity);

    }

    @Override
    public EquipmentReprocessedProductManagementEntityWithBLOBs findById(Integer id) {
        List<EquipmentReprocessedProductManagementEntityWithBLOBs> equipmentReprocessedProductManagementEntities = equipmentReprocessedProductManagementDao.selectByCondition(new Integer[]{id}, null, null);
        return equipmentReprocessedProductManagementEntities.isEmpty() ? null : equipmentReprocessedProductManagementEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentReprocessedProductManagementEntityWithBLOBs> findByCondition(String equipmentNumber, String equipmentName) {
        return equipmentReprocessedProductManagementDao.selectByCondition(null, equipmentNumber, equipmentName);
    }
}
