package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushRecordFileEntity;

public interface EquipmentWenTiMessageTimingPushRecordFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiMessageTimingPushRecordFileEntity record);

    int insertSelective(EquipmentWenTiMessageTimingPushRecordFileEntity record);

    EquipmentWenTiMessageTimingPushRecordFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiMessageTimingPushRecordFileEntity record);

    int updateByPrimaryKey(EquipmentWenTiMessageTimingPushRecordFileEntity record);
}