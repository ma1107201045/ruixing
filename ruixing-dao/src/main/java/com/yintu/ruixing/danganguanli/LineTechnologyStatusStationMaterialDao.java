package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusStationMaterialDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationMaterialEntity record);

    int insertSelective(LineTechnologyStatusStationMaterialEntity record);

    LineTechnologyStatusStationMaterialEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationMaterialEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationMaterialEntity record);

    List<LineTechnologyStatusStationMaterialEntity> selectByExample(Integer stationId);


    long countByStationId(Integer stationId);
}