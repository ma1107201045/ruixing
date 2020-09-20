package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerTypeEntity record);

    int insertSelective(CustomerTypeEntity record);

    CustomerTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerTypeEntity record);

    int updateByPrimaryKey(CustomerTypeEntity record);

    List<CustomerTypeEntity> selectAll();
}