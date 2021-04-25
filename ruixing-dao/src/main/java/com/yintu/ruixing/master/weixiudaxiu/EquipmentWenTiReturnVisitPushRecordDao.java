package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitPushRecordEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitPushRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiReturnVisitPushRecordEntity record);

    EquipmentWenTiReturnVisitPushRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitPushRecordEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitPushRecordEntity record);

    /////////////////////////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiReturnVisitPushRecordEntity record);

    String findFristPushNumber();

    List<EquipmentWenTiReturnVisitPushRecordEntity> findPushMessageRecordById(Integer vid);
}