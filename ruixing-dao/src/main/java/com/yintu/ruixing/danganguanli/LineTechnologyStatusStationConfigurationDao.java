package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusStationConfigurationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationConfigurationEntity record);

    int insertSelective(LineTechnologyStatusStationConfigurationEntity record);

    LineTechnologyStatusStationConfigurationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationConfigurationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationConfigurationEntity record);

    List<LineTechnologyStatusStationConfigurationEntity> selectByExample(Integer stationId);

    long countByStationId(Integer stationId);
}