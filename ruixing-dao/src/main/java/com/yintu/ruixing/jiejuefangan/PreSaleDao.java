package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PreSaleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleEntity record);

    int insertSelective(PreSaleEntity record);

    PreSaleEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleEntity record);

    int updateByPrimaryKey(PreSaleEntity record);

    List<PreSaleEntity> selectAll();

    List<PreSaleEntity> selectByYear(Integer year);

    List<PreSaleEntity> selectByExample(Integer year, String projectName);

    List<Integer> selectByDistinctProjectDate();


}