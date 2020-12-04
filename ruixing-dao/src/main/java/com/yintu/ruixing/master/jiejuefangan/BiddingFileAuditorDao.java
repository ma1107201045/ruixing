package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;

import java.util.List;

public interface BiddingFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingFileAuditorEntity record);

    int insertSelective(BiddingFileAuditorEntity record);

    BiddingFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingFileAuditorEntity record);

    int updateByPrimaryKey(BiddingFileAuditorEntity record);

    List<BiddingFileAuditorEntity> selectByBiddingFileId(Integer biddingFileId);

    List<BiddingFileAuditorEntity> selectByExample(Integer biddingFileId, Integer auditorId, Short isCheck);

    void insertMuch(List<BiddingFileAuditorEntity> biddingFileAuditorEntities);

    void deleteByBiddingFileId(Integer biddingFileId);
}