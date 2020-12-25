package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.CustomerDepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CustomerDepartmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDepartmentEntity record);

    int insertSelective(CustomerDepartmentEntity record);

    CustomerDepartmentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerDepartmentEntity record);

    int updateByPrimaryKey(CustomerDepartmentEntity record);

    List<CustomerDepartmentEntity> selectByParentIdAndTypeId(Integer parentId, Short typeId);

    List<CustomerDepartmentEntity> selectByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short typeId);
}