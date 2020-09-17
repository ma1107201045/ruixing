package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementEntityWithBLOBs;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        equipmentReprocessedProductManagementDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentReprocessedProductManagementEntityWithBLOBs findById(Integer id) {
        List<EquipmentReprocessedProductManagementEntityWithBLOBs> equipmentReprocessedProductManagementEntities = equipmentReprocessedProductManagementDao.selectByCondition(new Integer[]{id}, null);
        return equipmentReprocessedProductManagementEntities.isEmpty() ? null : equipmentReprocessedProductManagementEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentReprocessedProductManagementEntityWithBLOBs> findByCondition(String equipmentNumber) {
        return equipmentReprocessedProductManagementDao.selectByCondition(null, equipmentNumber);
    }
}
