package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EquipmentOverhaulManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentOverhaulManagementEntityWithBLOBs record);

    int insertSelective(EquipmentOverhaulManagementEntityWithBLOBs record);

    EquipmentOverhaulManagementEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentOverhaulManagementEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(EquipmentOverhaulManagementEntityWithBLOBs record);

    int updateByPrimaryKey(EquipmentOverhaulManagementEntity record);

    List<EquipmentOverhaulManagementEntityWithBLOBs> selectByCondition(Integer id, String equipmentNumber);
}