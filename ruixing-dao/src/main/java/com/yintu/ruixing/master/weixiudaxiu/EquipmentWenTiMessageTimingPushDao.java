package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushEntity;

import java.util.List;

public interface EquipmentWenTiMessageTimingPushDao {
    int insert(EquipmentWenTiMessageTimingPushEntity record);

    int updateByPrimaryKeySelective(EquipmentWenTiMessageTimingPushEntity record);

    int updateByPrimaryKey(EquipmentWenTiMessageTimingPushEntity record);


    //////////////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    EquipmentWenTiMessageTimingPushEntity selectByPrimaryKey(Integer id);

    String findFristNumber();

    int insertSelective(EquipmentWenTiMessageTimingPushEntity record);

    List<EquipmentWenTiMessageTimingPushEntity> findAllMessagePush(String tljName, String dwdName, String xdName,
                                                                   String startTime, String endTime,
                                                                   Integer implementState, Integer pushType);

}