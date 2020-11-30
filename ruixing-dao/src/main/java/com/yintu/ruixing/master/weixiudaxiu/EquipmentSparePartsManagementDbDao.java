package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementDbEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface EquipmentSparePartsManagementDbDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentSparePartsManagementDbEntity record);

    int insertSelective(EquipmentSparePartsManagementDbEntity record);

    EquipmentSparePartsManagementDbEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentSparePartsManagementDbEntity record);

    int updateByPrimaryKey(EquipmentSparePartsManagementDbEntity record);

    List<EquipmentSparePartsManagementDbEntity> selectByCondition(Integer[] ids, String equipmentNumber, Integer equipmentSparePartsManagementId, String equipmentName);

}