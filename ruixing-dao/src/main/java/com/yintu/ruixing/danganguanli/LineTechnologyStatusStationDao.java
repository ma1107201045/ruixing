package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LineTechnologyStatusStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationEntity record);

    int insertSelective(LineTechnologyStatusStationEntity record);

    LineTechnologyStatusStationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationEntity record);

    List<LineTechnologyStatusStationEntity> selectByExample(Integer xid);

    Map<String, Object> selectLineStatistics(Integer cid);

}