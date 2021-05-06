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

    List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAllAcceptFeedback(String number, String statrTime, String endTime,
                                                                         String tljName, String dwdName, String xdName,
                                                                         String feedbackName, String wentiType,
                                                                         String wentiMiaoshu, Integer feedbackState,
                                                                         Integer acceptUserid, Integer wentiState,
                                                                         Integer pushState);

}