package com.yintu.ruixing.weixiudaxiu;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface EquipmentSparePartsManagementService extends BaseService<EquipmentSparePartsManagementEntity, Integer> {

    void add(EquipmentSparePartsManagementEntity entity, String loginTrueName);

    void remove(Integer[] ids);

    void put(String loginUserName, String loginTrueName, Integer id, Integer putAmount);

    List<EquipmentSparePartsManagementEntity> findByCondition(String materialNumber, String equipmentName);


    JSONArray findRecordById(Integer id);
}
