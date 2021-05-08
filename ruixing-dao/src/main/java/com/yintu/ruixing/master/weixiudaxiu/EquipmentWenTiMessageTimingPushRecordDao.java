package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushRecordEntity;

import java.util.List;

public interface EquipmentWenTiMessageTimingPushRecordDao {
    int insert(EquipmentWenTiMessageTimingPushRecordEntity record);

    EquipmentWenTiMessageTimingPushRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(EquipmentWenTiMessageTimingPushRecordEntity record);


    ///////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiMessageTimingPushRecordEntity record);

    int insertSelective(EquipmentWenTiMessageTimingPushRecordEntity record);

    String findRecordNum(String number);

    List<EquipmentWenTiMessageTimingPushRecordEntity> findAllMessagePushRecord(Integer pushtype);
}