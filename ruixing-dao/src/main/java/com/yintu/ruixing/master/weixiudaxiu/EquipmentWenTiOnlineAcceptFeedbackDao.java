package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackEntity;

import java.util.List;

public interface EquipmentWenTiOnlineAcceptFeedbackDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    int updateByPrimaryKeySelective(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    int updateByPrimaryKey(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    //////////////////////////////////////////////////////////////////////////////
    EquipmentWenTiOnlineAcceptFeedbackEntity selectByPrimaryKey(Integer id);

    int insertSelective(EquipmentWenTiOnlineAcceptFeedbackEntity record);

    String findNumber();

    List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAcceptFeedbackByphone(String phone);
}