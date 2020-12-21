package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.jiejuefangan.PreSaleFileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface PreSaleFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleFileEntity record);

    int insertSelective(PreSaleFileEntity record);

    PreSaleFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleFileEntity record);

    int updateByPrimaryKey(PreSaleFileEntity record);

    List<PreSaleFileEntity> selectByCondition(Integer preSaleId, Integer[] ids, String name, Short type, Integer userId, Short releaseStatus);


    List<AuditTotalVo> selectByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short isDispose);

}