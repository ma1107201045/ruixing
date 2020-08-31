package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PreSaleFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleFileAuditorEntity record);

    int insertSelective(PreSaleFileAuditorEntity record);

    PreSaleFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleFileAuditorEntity record);

    int updateByPrimaryKey(PreSaleFileAuditorEntity record);

    List<PreSaleFileAuditorEntity> selectByPreSaleFileId(Integer preSaleFileId);

    void insertMuch(List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities);

    void deleteByPreSaleFileId(Integer preSaleFileId);
}