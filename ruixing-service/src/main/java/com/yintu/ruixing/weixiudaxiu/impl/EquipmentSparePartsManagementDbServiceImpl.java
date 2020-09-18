package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementDbDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementDbEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementDbService;
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
        List<EquipmentSparePartsManagementDbEntity> equipmentSparePartsManagementDbEntities = equipmentSparePartsManagementDbDao.selectByCondition(new Integer[]{id}, null, null);
        return equipmentSparePartsManagementDbEntities.isEmpty() ? null : equipmentSparePartsManagementDbEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentSparePartsManagementDbEntity> findByCondition(Integer id, String equipmentNumber, String equipmentName) {
        return equipmentSparePartsManagementDbDao.selectByCondition(null, equipmentNumber, equipmentName);
    }
}
