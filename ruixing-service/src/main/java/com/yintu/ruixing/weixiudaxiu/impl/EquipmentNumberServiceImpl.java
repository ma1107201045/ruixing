package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/13 11:08
 */
@Service
@Transactional
public class EquipmentNumberServiceImpl implements EquipmentNumberService {

    @Autowired
    private EquipmentNumberDao equipmentNumberDao;
    @Autowired
    private EquipmentNumberRecordService equipmentNumberRecordService;

    @Override
    public void add(EquipmentNumberEntity entity) {
        equipmentNumberDao.insertSelective(entity);
        EquipmentNumberRecordEntity equipmentNumberRecordEntity = new EquipmentNumberRecordEntity();
        equipmentNumberRecordEntity.setCreateBy(entity.getCreateBy());
        equipmentNumberRecordEntity.setCreateTime(entity.getCreateTime());
        equipmentNumberRecordEntity.setModifiedBy(entity.getModifiedBy());
        equipmentNumberRecordEntity.setModifiedTime(entity.getModifiedTime());
        equipmentNumberRecordEntity.setEquipmentNumber(entity.getEquipmentNumber());
        equipmentNumberRecordEntity.setName(entity.getName());
        equipmentNumberRecordEntity.setConfiguration(entity.getConfiguration());
        equipmentNumberRecordEntity.setEquipmentNumberId(entity.getId());
        equipmentNumberRecordService.add(equipmentNumberRecordEntity);
    }

    @Override
    public void remove(Integer id) {
        equipmentNumberDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentNumberEntity entity) {
        equipmentNumberDao.updateByPrimaryKeySelective(entity);
        EquipmentNumberRecordEntity equipmentNumberRecordEntity = new EquipmentNumberRecordEntity();
        equipmentNumberRecordEntity.setCreateBy(entity.getModifiedBy());
        equipmentNumberRecordEntity.setCreateTime(entity.getModifiedTime());
        equipmentNumberRecordEntity.setModifiedBy(entity.getModifiedBy());
        equipmentNumberRecordEntity.setModifiedTime(entity.getModifiedTime());
        equipmentNumberRecordEntity.setEquipmentNumber(entity.getEquipmentNumber());
        equipmentNumberRecordEntity.setName(entity.getName());
        equipmentNumberRecordEntity.setConfiguration(entity.getConfiguration());
        equipmentNumberRecordEntity.setEquipmentNumberId(entity.getId());
        equipmentNumberRecordService.add(equipmentNumberRecordEntity);
    }

    @Override
    public EquipmentNumberEntity findById(Integer id) {
        List<EquipmentNumberEntity> equipmentNumberEntities = equipmentNumberDao.selectByCondition(new Integer[]{id}, null);
        return equipmentNumberEntities.isEmpty() ? null : equipmentNumberEntities.get(0);
    }

    @Override
    public List<EquipmentNumberEntity> findByCondition(Integer[] ids, String equipmentNumber) {
        return equipmentNumberDao.selectByCondition(ids, equipmentNumber);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }
}
