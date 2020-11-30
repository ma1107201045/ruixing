package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.master.weixiudaxiu.EquipmentIndexAnalysisDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentIndexAnalysisEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentIndexAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentIndexAnalysisServiceImpl implements EquipmentIndexAnalysisService {
    @Autowired
    private EquipmentIndexAnalysisDao equipmentIndexAnalysisDao;


    @Override
    public void add(EquipmentIndexAnalysisEntity entity) {
        equipmentIndexAnalysisDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        equipmentIndexAnalysisDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentIndexAnalysisEntity entity) {
        equipmentIndexAnalysisDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentIndexAnalysisEntity findById(Integer id) {
        return equipmentIndexAnalysisDao.selectByPrimaryKey(id);
    }

    @Override
    public List<EquipmentIndexAnalysisEntity> findEquipmentByCondition(String equipmentNumber) {
        return equipmentIndexAnalysisDao.selectEquipmentByCondition(equipmentNumber);
    }

    @Override
    public List<EquipmentIndexAnalysisEntity> findQuduanByCondition(String quDuanYunYingName) {
        return equipmentIndexAnalysisDao.selectQuduanByCondition(quDuanYunYingName);
    }


}
