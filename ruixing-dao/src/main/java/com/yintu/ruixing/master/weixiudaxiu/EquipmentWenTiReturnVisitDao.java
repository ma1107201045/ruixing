package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitEntity;

public interface EquipmentWenTiReturnVisitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiReturnVisitEntity record);

    EquipmentWenTiReturnVisitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitEntity record);
/////////////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiReturnVisitEntity record);
}