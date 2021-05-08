package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushRecordPhoneEntity;

public interface EquipmentWenTiMessageTimingPushRecordPhoneDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiMessageTimingPushRecordPhoneEntity record);

    int insertSelective(EquipmentWenTiMessageTimingPushRecordPhoneEntity record);

    EquipmentWenTiMessageTimingPushRecordPhoneEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiMessageTimingPushRecordPhoneEntity record);

    int updateByPrimaryKey(EquipmentWenTiMessageTimingPushRecordPhoneEntity record);
}