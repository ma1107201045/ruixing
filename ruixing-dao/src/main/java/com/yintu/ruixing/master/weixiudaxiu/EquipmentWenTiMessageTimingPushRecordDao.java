package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushRecordEntity;

public interface EquipmentWenTiMessageTimingPushRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiMessageTimingPushRecordEntity record);

    int insertSelective(EquipmentWenTiMessageTimingPushRecordEntity record);

    EquipmentWenTiMessageTimingPushRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiMessageTimingPushRecordEntity record);

    int updateByPrimaryKey(EquipmentWenTiMessageTimingPushRecordEntity record);
///////////////////////////////////////////////////////////////////////
    String findRecordNum(String number);
}