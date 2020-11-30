package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.master.weixiudaxiu.EquipmentNumberRecordDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentNumberRecordEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentNumberRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentNumberRecordServiceImpl implements EquipmentNumberRecordService {
    @Autowired
    private EquipmentNumberRecordDao equipmentNumberRecordDao;

    @Override
    public void add(EquipmentNumberRecordEntity entity) {
        equipmentNumberRecordDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentNumberRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentNumberRecordEntity entity) {
        equipmentNumberRecordDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public EquipmentNumberRecordEntity findById(Integer id) {
        List<EquipmentNumberRecordEntity> equipmentNumberRecordEntities = equipmentNumberRecordDao.selectByCondition(new Integer[]{id}, null);
        return equipmentNumberRecordEntities.isEmpty() ? null : equipmentNumberRecordEntities.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<EquipmentNumberRecordEntity> findByCondition(Integer[] ids, Integer equipmentNumberId) {
        return equipmentNumberRecordDao.selectByCondition(ids, equipmentNumberId);
    }
}
