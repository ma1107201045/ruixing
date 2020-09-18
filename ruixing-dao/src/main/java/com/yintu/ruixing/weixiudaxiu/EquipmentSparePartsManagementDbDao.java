package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentSparePartsManagementDbDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentSparePartsManagementDbEntity record);

    int insertSelective(EquipmentSparePartsManagementDbEntity record);

    EquipmentSparePartsManagementDbEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentSparePartsManagementDbEntity record);

    int updateByPrimaryKey(EquipmentSparePartsManagementDbEntity record);

    List<EquipmentSparePartsManagementDbEntity> selectByCondition(Integer[] ids, String equipmentNumber,String equipmentName);

}