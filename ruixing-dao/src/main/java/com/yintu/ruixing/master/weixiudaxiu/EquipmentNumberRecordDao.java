package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentNumberRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface EquipmentNumberRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentNumberRecordEntity record);

    int insertSelective(EquipmentNumberRecordEntity record);

    EquipmentNumberRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentNumberRecordEntity record);

    int updateByPrimaryKeyWithBLOBs(EquipmentNumberRecordEntity record);

    int updateByPrimaryKey(EquipmentNumberRecordEntity record);

    List<EquipmentNumberRecordEntity> selectByCondition(Integer[] ids, Integer equipmentNumberId);
}