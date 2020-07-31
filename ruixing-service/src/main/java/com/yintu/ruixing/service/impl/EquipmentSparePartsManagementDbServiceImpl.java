package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.EquipmentSparePartsManagementDbDao;
import com.yintu.ruixing.entity.EquipmentSparePartsManagementDbEntity;
import com.yintu.ruixing.service.EquipmentSparePartsManagementDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/30 11:12
 */
@Service
@Transactional
public class EquipmentSparePartsManagementDbServiceImpl implements EquipmentSparePartsManagementDbService {

    @Autowired
    private EquipmentSparePartsManagementDbDao equipmentSparePartsManagementDbDao;

    @Override
    public void add(EquipmentSparePartsManagementDbEntity entity) {
        equipmentSparePartsManagementDbDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentSparePartsManagementDbDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentSparePartsManagementDbEntity entity) {
        equipmentSparePartsManagementDbDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentSparePartsManagementDbEntity findById(Integer id) {
        List<EquipmentSparePartsManagementDbEntity> equipmentSparePartsManagementDbEntities = equipmentSparePartsManagementDbDao.connectSelectByCondition(id, null, null);
        return equipmentSparePartsManagementDbEntities.isEmpty() ? null : equipmentSparePartsManagementDbEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentSparePartsManagementDbEntity> findByEquipmentNameAndMaterialNumber(Integer id, String equipmentName, String materialNumber) {
        return equipmentSparePartsManagementDbDao.connectSelectByCondition(null, equipmentName, materialNumber);
    }
}
