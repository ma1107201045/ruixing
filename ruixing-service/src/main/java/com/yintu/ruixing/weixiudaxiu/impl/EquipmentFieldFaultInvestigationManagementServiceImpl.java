package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.weixiudaxiu.EquipmentFieldFaultInvestigationManagementDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentFieldFaultInvestigationManagementEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentFieldFaultInvestigationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/30 19:23
 */
@Service
@Transactional
public class EquipmentFieldFaultInvestigationManagementServiceImpl implements EquipmentFieldFaultInvestigationManagementService {

    @Autowired
    private EquipmentFieldFaultInvestigationManagementDao equipmentFieldFaultInvestigationManagementDao;

    @Override
    public void add(EquipmentFieldFaultInvestigationManagementEntity entity) {
        equipmentFieldFaultInvestigationManagementDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentFieldFaultInvestigationManagementDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentFieldFaultInvestigationManagementEntity entity) {
        equipmentFieldFaultInvestigationManagementDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentFieldFaultInvestigationManagementEntity findById(Integer id) {
        List<EquipmentFieldFaultInvestigationManagementEntity> equipmentFieldFaultInvestigationManagementEntities = equipmentFieldFaultInvestigationManagementDao.selectByCondition(new Integer[]{id}, null, null);
        return equipmentFieldFaultInvestigationManagementEntities.isEmpty() ? null : equipmentFieldFaultInvestigationManagementEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentFieldFaultInvestigationManagementEntity> findByCondition(Date startDate, Date endDate) {
        return equipmentFieldFaultInvestigationManagementDao.selectByCondition(null, startDate, endDate);
    }
}
