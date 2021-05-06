package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity;

import java.util.List;

public interface EquipmentWenTiOnlineAcceptFeedbackPushRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity record);

    int insertSelective(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity record);

    EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity record);

    int updateByPrimaryKey(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity record);
///////////////////////////////////////////////////////////////////////////////////////////////
    EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity findOneAcceptFeedbackById(Integer id);

    String findFristPushNumber();

    List<EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity> findPushMessageRecordById(Integer fid);
}