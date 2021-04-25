package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitCommentEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitCommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentWenTiReturnVisitCommentEntity record);

    EquipmentWenTiReturnVisitCommentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitCommentEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitCommentEntity record);
/////////////////////////////////////////////////////////////////
    int insertSelective(EquipmentWenTiReturnVisitCommentEntity record);

    List<EquipmentWenTiReturnVisitCommentEntity> findCommentByVid(Integer vid);
}