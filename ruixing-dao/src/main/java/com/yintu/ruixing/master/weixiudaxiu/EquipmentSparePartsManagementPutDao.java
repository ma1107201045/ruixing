package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentSparePartsManagementPutEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface EquipmentSparePartsManagementPutDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentSparePartsManagementPutEntity record);

    int insertSelective(EquipmentSparePartsManagementPutEntity record);

    EquipmentSparePartsManagementPutEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentSparePartsManagementPutEntity record);

    int updateByPrimaryKey(EquipmentSparePartsManagementPutEntity record);

    List<EquipmentSparePartsManagementPutEntity> selectByCondition(Integer equipmentSparePartsManagementId);
}