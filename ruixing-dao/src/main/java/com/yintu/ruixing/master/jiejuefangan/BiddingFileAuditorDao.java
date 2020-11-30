package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorEntity;

import java.util.List;

public interface BiddingFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingFileAuditorEntity record);

    int insertSelective(BiddingFileAuditorEntity record);

    BiddingFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingFileAuditorEntity record);

    int updateByPrimaryKey(BiddingFileAuditorEntity record);

    List<BiddingFileAuditorEntity> selectByBiddingFileId(Integer biddingFileId);

    void insertMuch(List<BiddingFileAuditorEntity> biddingFileAuditorEntities);

    void deleteByBiddingFileId(Integer biddingFileId);
}