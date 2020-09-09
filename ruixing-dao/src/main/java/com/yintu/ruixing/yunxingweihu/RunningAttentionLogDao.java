package com.yintu.ruixing.yunxingweihu;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RunningAttentionLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RunningAttentionLogEntity record);

    int insertSelective(RunningAttentionLogEntity record);

    RunningAttentionLogEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RunningAttentionLogEntity record);

    int updateByPrimaryKeyWithBLOBs(RunningAttentionLogEntity record);

    int updateByPrimaryKey(RunningAttentionLogEntity record);
}