package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface LineTechnologyStatusStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationEntity record);

    int insertSelective(LineTechnologyStatusStationEntity record);

    LineTechnologyStatusStationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationEntity record);

    List<LineTechnologyStatusStationEntity> selectByExample(Integer xid);

    Map<String, Object> selectStationStatistics(Integer cid);


    long countByStationId(Integer stationId);

}