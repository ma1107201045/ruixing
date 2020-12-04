package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface PreSaleFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleFileAuditorEntity record);

    int insertSelective(PreSaleFileAuditorEntity record);

    PreSaleFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleFileAuditorEntity record);

    int updateByPrimaryKey(PreSaleFileAuditorEntity record);

    List<PreSaleFileAuditorEntity> selectByPreSaleFileId(Integer preSaleFileId);

    List<PreSaleFileAuditorEntity> selectByExample(Integer preSaleFileId,Integer auditorId,Short isCheck);

    void insertMuch(List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities);

    void deleteByPreSaleFileId(Integer preSaleFileId);
}