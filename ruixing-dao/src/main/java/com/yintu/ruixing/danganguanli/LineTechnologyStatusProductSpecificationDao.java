package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LineTechnologyStatusProductSpecificationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductSpecificationEntity record);

    int insertSelective(LineTechnologyStatusProductSpecificationEntity record);

    LineTechnologyStatusProductSpecificationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductSpecificationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductSpecificationEntity record);
}