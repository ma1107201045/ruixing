package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.jiejuefangan.BiddingFileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface BiddingFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiddingFileEntity record);

    int insertSelective(BiddingFileEntity record);

    BiddingFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiddingFileEntity record);

    int updateByPrimaryKey(BiddingFileEntity record);

    List<BiddingFileEntity> selectByCondition(Integer biddingId, Integer[] ids, String name, Short type, Integer userId, Short releaseStatus);

    List<AuditTotalVo> selectByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);
}