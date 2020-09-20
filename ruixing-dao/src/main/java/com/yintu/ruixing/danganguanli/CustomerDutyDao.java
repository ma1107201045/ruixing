package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerDutyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDutyEntity record);

    int insertSelective(CustomerDutyEntity record);

    CustomerDutyEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerDutyEntity record);

    int updateByPrimaryKey(CustomerDutyEntity record);

    List<CustomerDutyEntity> selectByExample(String name);
}