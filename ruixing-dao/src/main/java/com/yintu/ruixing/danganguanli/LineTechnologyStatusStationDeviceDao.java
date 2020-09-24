package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusStationDeviceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationDeviceEntity record);

    int insertSelective(LineTechnologyStatusStationDeviceEntity record);

    LineTechnologyStatusStationDeviceEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationDeviceEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationDeviceEntity record);

    List<LineTechnologyStatusStationDeviceEntity> selectByExample(Integer stationId);

    long countByStationId(Integer stationId);
}