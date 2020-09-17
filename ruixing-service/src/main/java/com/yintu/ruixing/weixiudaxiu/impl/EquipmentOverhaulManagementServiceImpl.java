package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementEntityWithBLOBs;
import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/28 16:56
 */
@Service
@Transactional
public class EquipmentOverhaulManagementServiceImpl implements EquipmentOverhaulManagementService {

    @Autowired
    private EquipmentOverhaulManagementDao equipmentOverhaulManagementDao;


    @Override
    public void add(EquipmentOverhaulManagementEntityWithBLOBs entity) {
        equipmentOverhaulManagementDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentOverhaulManagementDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentOverhaulManagementEntityWithBLOBs entity) {
        equipmentOverhaulManagementDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentOverhaulManagementEntityWithBLOBs findById(Integer id) {
        List<EquipmentOverhaulManagementEntityWithBLOBs> equipmentOverhaulManagementEntities = equipmentOverhaulManagementDao.selectByCondition(new Integer[]{id}, null);
        return equipmentOverhaulManagementEntities.isEmpty() ? null : equipmentOverhaulManagementEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentOverhaulManagementEntityWithBLOBs> findByCondition(String equipmentNumber) {
        return equipmentOverhaulManagementDao.selectByCondition(null, equipmentNumber);
    }
}
