package com.yintu.ruixing.weixiudaxiu.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentSparePartsManagementDao;
import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentSparePartsManagementServiceImpl implements EquipmentSparePartsManagementService {
    @Autowired
    private EquipmentSparePartsManagementDao equipmentSparePartsManagementDao;
    @Autowired
    private EquipmentSparePartsManagementPutService equipmentSparePartsManagementPutService;
    @Autowired
    private EquipmentSparePartsManagementDbService equipmentSparePartsManagementDbService;

    @Override
    public void add(EquipmentSparePartsManagementEntity entity) {
        if (entity.getThresholdAmount() > entity.getInventoryAmount())
            throw new BaseRuntimeException("库存数量不能大于预警门限数量");
        equipmentSparePartsManagementDao.insertSelective(entity);
    }


    @Override
    public void remove(Integer id) {
        equipmentSparePartsManagementDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(EquipmentSparePartsManagementEntity entity) {
        if (entity.getThresholdAmount() > entity.getInventoryAmount())
            throw new BaseRuntimeException("库存数量不能大于预警门限数量");
        equipmentSparePartsManagementDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public EquipmentSparePartsManagementEntity findById(Integer id) {
        List<EquipmentSparePartsManagementEntity> equipmentSparePartsManagementEntities = equipmentSparePartsManagementDao.selectByCondition(new Integer[]{id}, null, null);
        return equipmentSparePartsManagementEntities.isEmpty() ? null : equipmentSparePartsManagementEntities.get(0);
    }

    @Override
    public void add(EquipmentSparePartsManagementEntity entity, String loginTrueName) {
        this.add(entity);
        EquipmentSparePartsManagementPutEntity equipmentSparePartsManagementPutEntity = new EquipmentSparePartsManagementPutEntity();
        equipmentSparePartsManagementPutEntity.setCreateBy(entity.getCreateBy());
        equipmentSparePartsManagementPutEntity.setCreateTime(entity.getCreateTime());
        equipmentSparePartsManagementPutEntity.setModifiedBy(entity.getModifiedBy());
        equipmentSparePartsManagementPutEntity.setModifiedTime(entity.getModifiedTime());
        equipmentSparePartsManagementPutEntity.setOperator(loginTrueName);
        equipmentSparePartsManagementPutEntity.setPutAmount(entity.getInventoryAmount());
        equipmentSparePartsManagementPutEntity.setEquipmentSparePartsManagementId(entity.getId());
        equipmentSparePartsManagementPutService.add(equipmentSparePartsManagementPutEntity);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void put(String loginUserName, String loginTrueName, Integer id, Integer putAmount) {
        EquipmentSparePartsManagementEntity equipmentSparePartsManagementEntity = this.findById(id);
        if (equipmentSparePartsManagementEntity != null) {
            equipmentSparePartsManagementEntity.setModifiedBy(loginUserName);
            equipmentSparePartsManagementEntity.setModifiedTime(new Date());
            equipmentSparePartsManagementEntity.setInventoryAmount(equipmentSparePartsManagementEntity.getInventoryAmount() + putAmount);
            this.edit(equipmentSparePartsManagementEntity);

            EquipmentSparePartsManagementPutEntity equipmentSparePartsManagementPutEntity = new EquipmentSparePartsManagementPutEntity();
            equipmentSparePartsManagementPutEntity.setCreateBy(equipmentSparePartsManagementEntity.getModifiedBy());
            equipmentSparePartsManagementPutEntity.setCreateTime(equipmentSparePartsManagementEntity.getModifiedTime());
            equipmentSparePartsManagementPutEntity.setModifiedBy(equipmentSparePartsManagementEntity.getModifiedBy());
            equipmentSparePartsManagementPutEntity.setModifiedTime(equipmentSparePartsManagementEntity.getModifiedTime());
            equipmentSparePartsManagementPutEntity.setOperator(loginTrueName);
            equipmentSparePartsManagementPutEntity.setPutAmount(putAmount);
            equipmentSparePartsManagementPutEntity.setEquipmentSparePartsManagementId(id);
            equipmentSparePartsManagementPutService.add(equipmentSparePartsManagementPutEntity);
        }
    }

    @Override
    public List<EquipmentSparePartsManagementEntity> findByCondition(String materialNumber, String equipmentName) {
        return equipmentSparePartsManagementDao.selectByCondition(null, materialNumber, equipmentName);
    }

    @Override
    public List<Object> findRecordById(Integer id) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        List<EquipmentSparePartsManagementPutEntity> equipmentSparePartsManagementPutEntities = equipmentSparePartsManagementPutService.findByCondition(id);
        for (EquipmentSparePartsManagementPutEntity equipmentSparePartsManagementPutEntity : equipmentSparePartsManagementPutEntities) {
            JSONObject jo = new JSONObject(true);
            jo.put("createTime", equipmentSparePartsManagementPutEntity.getCreateTime());
            jo.put("operator", equipmentSparePartsManagementPutEntity.getOperator());
            jo.put("quantity", equipmentSparePartsManagementPutEntity.getPutAmount());
            jo.put("type", "put");
            jsonObjects.add(jo);
        }
        List<EquipmentSparePartsManagementDbEntity> equipmentSparePartsManagementDbEntities = equipmentSparePartsManagementDbService.findByCondition(null, null, id, null);
        for (EquipmentSparePartsManagementDbEntity equipmentSparePartsManagementDbEntity : equipmentSparePartsManagementDbEntities) {
            JSONObject jo = new JSONObject(true);
            jo.put("createTime", equipmentSparePartsManagementDbEntity.getCreateTime());
            jo.put("operator", equipmentSparePartsManagementDbEntity.getOperator());
            jo.put("quantity", equipmentSparePartsManagementDbEntity.getQuantity());
            jo.put("type", "out");
            jsonObjects.add(jo);
        }
        return jsonObjects.stream()
                .sorted((a, b) -> Long.compare(b.getDate("createTime").getTime(), a.getDate("createTime").getTime()))
                .collect(Collectors.toList());

    }

}
