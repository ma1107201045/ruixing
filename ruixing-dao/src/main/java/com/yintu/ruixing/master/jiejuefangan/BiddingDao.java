package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.BiddingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface BiddingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingEntity record);

    int insertSelective(BiddingEntity record);

    BiddingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingEntity record);

    int updateByPrimaryKey(BiddingEntity record);

    List<BiddingEntity> selectAll();

    List<BiddingEntity> selectByYear(Integer year);

    List<BiddingEntity> selectByExample(Integer year, String projectName);

    List<Integer> selectByDistinctProjectDate();
}