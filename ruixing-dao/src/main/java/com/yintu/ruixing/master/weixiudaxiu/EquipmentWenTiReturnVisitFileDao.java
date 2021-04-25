package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitFileEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitFileDao {
    int insert(EquipmentWenTiReturnVisitFileEntity record);

    EquipmentWenTiReturnVisitFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitFileEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitFileEntity record);

    ///////////////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int insertSelective(EquipmentWenTiReturnVisitFileEntity record);

    List<EquipmentWenTiReturnVisitFileEntity> findFie(Integer filetype,Integer vid);

    List<EquipmentWenTiReturnVisitFileEntity> findWenTiFile(Integer id);

    List<EquipmentWenTiReturnVisitFileEntity> findReturnFile(Integer id);
}