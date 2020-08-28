package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BiddingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingEntity record);

    int insertSelective(BiddingEntity record);

    BiddingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingEntity record);

    int updateByPrimaryKey(BiddingEntity record);

    List<BiddingEntity> selectAll();

    List<BiddingEntity> selectByYear(Integer year);

    List<Integer> selectByDistinctProjectDate();
}