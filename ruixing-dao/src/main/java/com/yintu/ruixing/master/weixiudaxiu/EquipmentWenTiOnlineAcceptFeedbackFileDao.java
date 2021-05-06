package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackFileEntity;

import java.util.List;

public interface EquipmentWenTiOnlineAcceptFeedbackFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiOnlineAcceptFeedbackFileEntity record);

    EquipmentWenTiOnlineAcceptFeedbackFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiOnlineAcceptFeedbackFileEntity record);

    int updateByPrimaryKey(EquipmentWenTiOnlineAcceptFeedbackFileEntity record);
//////////////////////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiOnlineAcceptFeedbackFileEntity record);

    List<EquipmentWenTiOnlineAcceptFeedbackFileEntity> findFileByfid(Integer fid);
}