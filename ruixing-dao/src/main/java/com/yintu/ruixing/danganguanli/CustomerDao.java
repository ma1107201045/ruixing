package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerEntity record);

    int insertSelective(CustomerEntity record);

    CustomerEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerEntity record);

    int updateByPrimaryKey(CustomerEntity record);

    Long countByExample(Integer typeId,String name);

    List<CustomerEntity> selectByExample(Integer[] ids, Integer typeId, Integer departmentId, String name);
}