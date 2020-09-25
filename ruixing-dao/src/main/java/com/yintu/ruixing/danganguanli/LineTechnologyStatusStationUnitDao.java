package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusStationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationUnitEntity record);

    int insertSelective(LineTechnologyStatusStationUnitEntity record);

    LineTechnologyStatusStationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationUnitEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationUnitEntity record);

    List<LineTechnologyStatusStationUnitEntity> selectByExample(Integer stationId);

    long countByStationId(Integer stationId);
}