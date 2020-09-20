package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerDepartmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDepartmentEntity record);

    int insertSelective(CustomerDepartmentEntity record);

    CustomerDepartmentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerDepartmentEntity record);

    int updateByPrimaryKey(CustomerDepartmentEntity record);
}