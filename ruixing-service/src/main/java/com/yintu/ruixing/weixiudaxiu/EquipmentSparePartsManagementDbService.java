package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementDbEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/30 11:09
 */
public interface EquipmentSparePartsManagementDbService extends BaseService<EquipmentSparePartsManagementDbEntity, Integer> {

    void remove(Integer[] ids);

    List<EquipmentSparePartsManagementDbEntity> findByCondition(Integer id, String equipmentNumber);

}
