package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.PreSaleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface PreSaleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSaleEntity record);

    int insertSelective(PreSaleEntity record);

    PreSaleEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSaleEntity record);

    int updateByPrimaryKey(PreSaleEntity record);

    List<PreSaleEntity> selectAll();

    List<PreSaleEntity> selectByYear(Integer year);

    List<PreSaleEntity> selectByExample(Integer[] ids, Integer year, String projectName);

    List<Integer> selectByDistinctProjectDate();


}