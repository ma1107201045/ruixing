package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentSparePartsManagementPutServiceImpl implements EquipmentSparePartsManagementPutService {
    @Autowired
    private EquipmentSparePartsManagementPutDao equipmentSparePartsManagementPutDao;

    @Override
    public void add(EquipmentSparePartsManagementPutEntity entity) {
        equipmentSparePartsManagementPutDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentSparePartsManagementPutDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentSparePartsManagementPutEntity entity) {
        equipmentSparePartsManagementPutDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentSparePartsManagementPutEntity findById(Integer id) {
        return equipmentSparePartsManagementPutDao.selectByPrimaryKey(id);
    }

    @Override
    public List<EquipmentSparePartsManagementPutEntity> findByCondition(Integer equipmentSparePartsManagementId) {
        return equipmentSparePartsManagementPutDao.selectByCondition(equipmentSparePartsManagementId);
    }
}
