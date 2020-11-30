package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.SolutionLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


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