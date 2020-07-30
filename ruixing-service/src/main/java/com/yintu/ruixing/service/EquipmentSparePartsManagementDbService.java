package com.yintu.ruixing.service;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.entity.EquipmentSparePartsManagementDbEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/30 11:09
 */
public interface EquipmentSparePartsManagementDbService extends BaseService<EquipmentSparePartsManagementDbEntity, Integer> {

    void remove(Integer[] ids);

    List<EquipmentSparePartsManagementDbEntity> findByEquipmentNameAndMaterialNumber(Integer id, String equipmentName, String materialNumber);

}
