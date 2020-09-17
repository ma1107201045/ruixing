package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentReprocessedProductManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentReprocessedProductManagementEntityWithBLOBs record);

    int insertSelective(EquipmentReprocessedProductManagementEntityWithBLOBs record);

    EquipmentReprocessedProductManagementEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentReprocessedProductManagementEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(EquipmentReprocessedProductManagementEntityWithBLOBs record);

    int updateByPrimaryKey(EquipmentReprocessedProductManagementEntity record);

    List<EquipmentReprocessedProductManagementEntityWithBLOBs> selectByCondition(Integer[] ids, String equipmentNumber);

}