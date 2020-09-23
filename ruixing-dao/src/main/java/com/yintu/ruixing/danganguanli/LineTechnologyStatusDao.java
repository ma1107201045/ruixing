package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LineTechnologyStatusDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusEntityWithBLOBs record);

    int insertSelective(LineTechnologyStatusEntityWithBLOBs record);

    LineTechnologyStatusEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LineTechnologyStatusEntityWithBLOBs record);

    int updateByPrimaryKey(LineTechnologyStatusEntity record);

    List<LineTechnologyStatusEntityWithBLOBs> selectByExample(Integer xid);

    Map<String, Object> selectLineStatistics(Integer xid);
}