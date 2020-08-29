package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SolutionLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SolutionLogEntity record);

    int insertSelective(SolutionLogEntity record);

    SolutionLogEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SolutionLogEntity record);

    int updateByPrimaryKeyWithBLOBs(SolutionLogEntity record);

    int updateByPrimaryKey(SolutionLogEntity record);

    List<SolutionLogEntity> selectByExample(SolutionLogEntity entity);
}