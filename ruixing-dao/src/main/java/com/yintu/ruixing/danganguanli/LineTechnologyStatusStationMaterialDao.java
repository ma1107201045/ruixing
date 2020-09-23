package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationMaterialEntity;

public interface LineTechnologyStatusStationMaterialDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationMaterialEntity record);

    int insertSelective(LineTechnologyStatusStationMaterialEntity record);

    LineTechnologyStatusStationMaterialEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationMaterialEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationMaterialEntity record);
}