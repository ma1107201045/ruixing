package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.danganguanli.CustomerEntity;

import java.util.List;


public interface CustomerDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerEntity record);

    int insertSelective(CustomerEntity record);

    CustomerEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerEntity record);

    int updateByPrimaryKey(CustomerEntity record);

    Long countByExample(Integer typeId, String name);

    List<CustomerEntity> selectByExample(Integer[] ids, Integer typeId, Integer departmentId, String name);

    List<AuditTotalVo> selectAuditRecordByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);
}