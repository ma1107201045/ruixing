package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentSparePartsManagementPutDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentSparePartsManagementPutEntity record);

    int insertSelective(EquipmentSparePartsManagementPutEntity record);

    EquipmentSparePartsManagementPutEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentSparePartsManagementPutEntity record);

    int updateByPrimaryKey(EquipmentSparePartsManagementPutEntity record);

    List<EquipmentSparePartsManagementPutEntity> selectByCondition(Integer equipmentSparePartsManagementId);
}