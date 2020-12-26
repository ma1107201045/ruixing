package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.CustomerAuditRecordAuditorEntity;

import java.util.List;

public interface CustomerAuditRecordAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAuditRecordAuditorEntity record);

    int insertSelective(CustomerAuditRecordAuditorEntity record);

    CustomerAuditRecordAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAuditRecordAuditorEntity record);

    int updateByPrimaryKeyWithBLOBs(CustomerAuditRecordAuditorEntity record);

    int updateByPrimaryKey(CustomerAuditRecordAuditorEntity record);

    List<CustomerAuditRecordAuditorEntity> selectByCustomerAuditRecordIdId(Integer customerAuditRecordId);

    List<CustomerAuditRecordAuditorEntity> selectByExample(Integer customerAuditRecordId, Integer auditorId, Integer sort, Short activate);

    void insertMuch(List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities);

    void deleteByCustomerAuditRecordIdId(Integer customerAuditRecordId);
}