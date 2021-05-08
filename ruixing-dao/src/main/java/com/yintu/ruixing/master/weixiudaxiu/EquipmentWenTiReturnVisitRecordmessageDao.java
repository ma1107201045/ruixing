package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitRecordmessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiReturnVisitRecordmessageEntity record);

    EquipmentWenTiReturnVisitRecordmessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitRecordmessageEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitRecordmessageEntity record);
//////////////////////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiReturnVisitRecordmessageEntity record);

    List<EquipmentWenTiReturnVisitRecordmessageEntity> findRecordById(Integer id);

    List<EquipmentWenTiReturnVisitRecordmessageEntity> findRecordByPid(Integer pid);
}