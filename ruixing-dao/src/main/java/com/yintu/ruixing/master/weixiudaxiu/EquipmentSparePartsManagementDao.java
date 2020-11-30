package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface EquipmentSparePartsManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentSparePartsManagementEntity record);

    int insertSelective(EquipmentSparePartsManagementEntity record);

    EquipmentSparePartsManagementEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentSparePartsManagementEntity record);

    int updateByPrimaryKey(EquipmentSparePartsManagementEntity record);

    List<EquipmentSparePartsManagementEntity> selectByCondition(Integer[] ids, String materialNumber, String equipmentName);
}