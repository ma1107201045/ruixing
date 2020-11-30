package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentNumberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


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