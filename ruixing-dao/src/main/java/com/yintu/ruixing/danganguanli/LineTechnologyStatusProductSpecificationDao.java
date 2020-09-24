package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusProductSpecificationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductSpecificationEntity record);

    int insertSelective(LineTechnologyStatusProductSpecificationEntity record);

    LineTechnologyStatusProductSpecificationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductSpecificationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductSpecificationEntity record);

    List<LineTechnologyStatusProductSpecificationEntity> selectAll();
}