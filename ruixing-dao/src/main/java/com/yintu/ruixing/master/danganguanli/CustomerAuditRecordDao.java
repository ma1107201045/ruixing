package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.CustomerAuditRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CustomerAuditRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAuditRecordEntity record);

    int insertSelective(CustomerAuditRecordEntity record);

    CustomerAuditRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAuditRecordEntity record);

    int updateByPrimaryKey(CustomerAuditRecordEntity record);

    List<CustomerAuditRecordEntity> selectByExample(Integer[] ids, Integer customerId);

    List<CustomerAuditRecordEntity> selectByCustomerIdAndAuditStatus(Integer customerId, Short auditStatus);

}