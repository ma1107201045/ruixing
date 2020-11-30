package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusEntityWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface LineTechnologyStatusDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusEntityWithBLOBs record);

    int insertSelective(LineTechnologyStatusEntityWithBLOBs record);

    LineTechnologyStatusEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LineTechnologyStatusEntityWithBLOBs record);

    int updateByPrimaryKey(LineTechnologyStatusEntity record);

    List<LineTechnologyStatusEntityWithBLOBs> selectByExample(Integer xid);

    Map<String, Object> selectRailwaysBureauStatistics(Integer tid);

    Map<String, Object> selectSignalDepotStatistics(Integer did);

    Map<String, Object> selectLineStatistics(Integer xid);

}