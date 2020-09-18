package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private EquipmentSparePartsManagementService equipmentSparePartsManagementService;
    @Autowired
    private EquipmentSparePartsManagementDbService equipmentSparePartsManagementDbService;
    @Autowired
    private EquipmentReprocessedProductManagementService equipmentReprocessedProductManagementService;

    @Override
    public void add(EquipmentNumberEntity entity) {
        equipmentNumberDao.insertSelective(entity);
        EquipmentNumberRecordEntity equipmentNumberRecordEntity = new EquipmentNumberRecordEntity();
        equipmentNumberRecordEntity.setCreateBy(entity.getCreateBy());
        equipmentNumberRecordEntity.setCreateTime(entity.getCreateTime());
        equipmentNumberRecordEntity.setModifiedBy(entity.getModifiedBy());
        equipmentNumberRecordEntity.setModifiedTime(entity.getModifiedTime());
        equipmentNumberRecordEntity.setEquipmentNumber(entity.getEquipmentNumber());
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

    }

    @Override
    public EquipmentNumberEntity findById(Integer id) {
        List<EquipmentNumberEntity> equipmentNumberEntities = equipmentNumberDao.selectByCondition(new Integer[]{id}, null);
        return equipmentNumberEntities.isEmpty() ? null : equipmentNumberEntities.get(0);
    }


    @Override
    public void change(String loginUserName, String loginTrueName, Integer id, String equipmentNumber, String configuration) {
        EquipmentNumberEntity equipmentNumberEntity = this.findById(id);
        if (equipmentNumberEntity != null) {
            equipmentNumberEntity.setModifiedBy(loginUserName);
            equipmentNumberEntity.setModifiedTime(new Date());
            equipmentNumberEntity.setEquipmentNumber(equipmentNumber);
            equipmentNumberEntity.setConfiguration(configuration);
            this.edit(equipmentNumberEntity);//更新器材编号以及配置

            Integer equipmentSparePartsManagementId = equipmentNumberEntity.getEquipmentSparePartsManagementId();
            EquipmentSparePartsManagementEntity equipmentSparePartsManagementEntity = equipmentSparePartsManagementService.findById(equipmentSparePartsManagementId);
            if (equipmentSparePartsManagementEntity == null)
                throw new BaseRuntimeException("应急备品信息有误");
            Integer inventoryAmount = equipmentSparePartsManagementEntity.getInventoryAmount();
            if (inventoryAmount < 1)
                throw new BaseRuntimeException("库存不足，请及时入库");
            equipmentSparePartsManagementEntity.setModifiedBy(equipmentNumberEntity.getModifiedBy());
            equipmentSparePartsManagementEntity.setModifiedTime(equipmentNumberEntity.getModifiedTime());
            equipmentSparePartsManagementEntity.setInventoryAmount(inventoryAmount - 1);
            equipmentSparePartsManagementService.edit(equipmentSparePartsManagementEntity);//库存减1


            EquipmentNumberRecordEntity equipmentNumberRecordEntity = new EquipmentNumberRecordEntity();
            equipmentNumberRecordEntity.setCreateBy(equipmentNumberEntity.getModifiedBy());
            equipmentNumberRecordEntity.setCreateTime(equipmentNumberEntity.getModifiedTime());
            equipmentNumberRecordEntity.setModifiedBy(equipmentNumberEntity.getModifiedBy());
            equipmentNumberRecordEntity.setModifiedTime(equipmentNumberEntity.getModifiedTime());
            equipmentNumberRecordEntity.setEquipmentNumber(equipmentNumberEntity.getEquipmentNumber());
            equipmentNumberRecordEntity.setConfiguration(equipmentNumberEntity.getConfiguration());
            equipmentNumberRecordEntity.setEquipmentNumberId(equipmentNumberEntity.getId());
            equipmentNumberRecordService.add(equipmentNumberRecordEntity);//生成更换记录

            EquipmentSparePartsManagementDbEntity equipmentSparePartsManagementDbEntity = new EquipmentSparePartsManagementDbEntity();
            equipmentSparePartsManagementDbEntity.setCreateBy(equipmentNumberEntity.getModifiedBy());
            equipmentSparePartsManagementDbEntity.setCreateTime(equipmentNumberEntity.getModifiedTime());
            equipmentSparePartsManagementDbEntity.setModifiedBy(equipmentNumberEntity.getModifiedBy());
            equipmentSparePartsManagementDbEntity.setModifiedTime(equipmentNumberEntity.getModifiedTime());
            equipmentSparePartsManagementDbEntity.setOperator(loginTrueName);
            equipmentSparePartsManagementDbEntity.setQuantity(equipmentNumberEntity.getQuantity());

            equipmentSparePartsManagementDbEntity.setProvinceId(equipmentSparePartsManagementEntity.getProvinceId());
            equipmentSparePartsManagementDbEntity.setCityId(equipmentSparePartsManagementEntity.getCityId());
            equipmentSparePartsManagementDbEntity.setDistrictId(equipmentSparePartsManagementEntity.getDistrictId());
            equipmentSparePartsManagementDbEntity.setDetailedAddress(equipmentSparePartsManagementEntity.getDetailedAddress());
            equipmentSparePartsManagementDbEntity.setContactPerson(equipmentSparePartsManagementEntity.getContactPerson());
            equipmentSparePartsManagementDbEntity.setContactPhone(equipmentSparePartsManagementEntity.getContactPhone());
            equipmentSparePartsManagementDbEntity.setEquipmentNumberId(id);
            equipmentSparePartsManagementDbEntity.setEquipmentNumber(equipmentNumber);
            equipmentSparePartsManagementDbEntity.setEquipmentSparePartsManagementId(equipmentSparePartsManagementEntity.getId());
            equipmentSparePartsManagementDbEntity.setEquipmentName(equipmentSparePartsManagementEntity.getEquipmentName());
            equipmentSparePartsManagementDbService.add(equipmentSparePartsManagementDbEntity);  //生成发货单

            EquipmentReprocessedProductManagementEntityWithBLOBs equipmentReprocessedProductManagementEntityWithBLOBs = new EquipmentReprocessedProductManagementEntityWithBLOBs();
            equipmentReprocessedProductManagementEntityWithBLOBs.setCreateBy(equipmentNumberEntity.getModifiedBy());
            equipmentReprocessedProductManagementEntityWithBLOBs.setCreateTime(equipmentNumberEntity.getModifiedTime());
            equipmentReprocessedProductManagementEntityWithBLOBs.setModifiedBy(equipmentNumberEntity.getModifiedBy());
            equipmentReprocessedProductManagementEntityWithBLOBs.setModifiedTime(equipmentNumberEntity.getModifiedTime());
            equipmentReprocessedProductManagementEntityWithBLOBs.setEquipmentNumberId(id);
            equipmentReprocessedProductManagementEntityWithBLOBs.setEquipmentNumber(equipmentNumberEntity.getEquipmentNumber());
            equipmentReprocessedProductManagementEntityWithBLOBs.setEquipmentName(equipmentSparePartsManagementEntity.getEquipmentName());
            equipmentReprocessedProductManagementService.add(equipmentReprocessedProductManagementEntityWithBLOBs);//生成返修品记录
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<EquipmentNumberEntity> findByCondition(Integer[] ids, String equipmentNumber) {
        return equipmentNumberDao.selectByCondition(ids, equipmentNumber);
    }
}
