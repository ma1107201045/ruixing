package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementEntityWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface EquipmentOverhaulManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentOverhaulManagementEntityWithBLOBs record);

    int insertSelective(EquipmentOverhaulManagementEntityWithBLOBs record);

    EquipmentOverhaulManagementEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentOverhaulManagementEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(EquipmentOverhaulManagementEntityWithBLOBs record);

    int updateByPrimaryKey(EquipmentOverhaulManagementEntity record);

    List<EquipmentOverhaulManagementEntityWithBLOBs> selectByCondition(Integer[] ids, String equipmentNumber);
}