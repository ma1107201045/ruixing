package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.CustomerCustomerDepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CustomerCustomerDepartmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerCustomerDepartmentEntity record);

    int insertSelective(CustomerCustomerDepartmentEntity record);

    CustomerCustomerDepartmentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerCustomerDepartmentEntity record);

    int updateByPrimaryKey(CustomerCustomerDepartmentEntity record);

    Long countByExample(Integer departmentId);

    List<CustomerCustomerDepartmentEntity> selectByExample(Integer customerId, Integer customerAuditRecordId);

}