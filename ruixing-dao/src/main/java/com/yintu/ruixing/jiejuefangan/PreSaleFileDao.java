package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PreSaleFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleFileEntity record);

    int insertSelective(PreSaleFileEntity record);

    PreSaleFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleFileEntity record);

    int updateByPrimaryKey(PreSaleFileEntity record);

    List<PreSaleFileEntity> selectByCondition(Integer projectId,  Integer[] ids, Short type);
}