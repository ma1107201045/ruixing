package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackEntity;

public interface EquipmentWenTiOnlineAcceptFeedbackDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    EquipmentWenTiOnlineAcceptFeedbackEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    int updateByPrimaryKey(EquipmentWenTiOnlineAcceptFeedbackEntity record);
//////////////////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiOnlineAcceptFeedbackEntity record);
}