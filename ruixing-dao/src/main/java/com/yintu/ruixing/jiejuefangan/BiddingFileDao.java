package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BiddingFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingFileEntity record);

    int insertSelective(BiddingFileEntity record);

    BiddingFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingFileEntity record);

    int updateByPrimaryKey(BiddingFileEntity record);

    List<BiddingFileEntity> selectByCondition(Integer biddingId, Integer[] ids, String name, Short type, Integer userId, Short releaseStatus);

}