package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationConfigurationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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