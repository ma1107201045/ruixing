package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface EquipmentSparePartsManagementPutService extends BaseService<EquipmentSparePartsManagementPutEntity, Integer> {

    List<EquipmentSparePartsManagementPutEntity> findByCondition(Integer equipmentSparePartsManagementId);
}
