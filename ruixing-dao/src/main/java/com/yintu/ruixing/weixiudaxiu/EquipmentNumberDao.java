package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentNumberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentNumberEntity record);

    int insertSelective(EquipmentNumberEntity record);

    EquipmentNumberEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentNumberEntity record);

    int updateByPrimaryKeyWithBLOBs(EquipmentNumberEntity record);

    int updateByPrimaryKey(EquipmentNumberEntity record);

    List<EquipmentNumberEntity> selectByCondition(Integer[] ids, String equipmentNumber);
}