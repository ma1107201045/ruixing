package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerCustomerDepartmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerCustomerDepartmentEntity record);

    int insertSelective(CustomerCustomerDepartmentEntity record);

    CustomerCustomerDepartmentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerCustomerDepartmentEntity record);

    int updateByPrimaryKey(CustomerCustomerDepartmentEntity record);

    Long countExample(Integer departmentId);

    List<CustomerCustomerDepartmentEntity> selectByCustomerId(Integer customerId);
}