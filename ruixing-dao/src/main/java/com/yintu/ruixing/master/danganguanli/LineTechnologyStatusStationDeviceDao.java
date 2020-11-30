package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationDeviceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


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