package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiCustomerWenTiTypeEntity;

import java.util.List;

public interface EquipmentWenTiCustomerWenTiTypeDao {
    int insert(EquipmentWenTiCustomerWenTiTypeEntity record);

    EquipmentWenTiCustomerWenTiTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(EquipmentWenTiCustomerWenTiTypeEntity record);

    //////////////////////////////////////////////////////////////////////////

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiCustomerWenTiTypeEntity record);

    int insertSelective(EquipmentWenTiCustomerWenTiTypeEntity record);

    List<EquipmentWenTiCustomerWenTiTypeEntity> findAllWenTiType();

}