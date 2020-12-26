package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.danganguanli.CustomerAuditRecordAuditorEntity;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/25 18:57
 * @Version: 1.0
 */
public interface CustomerAuditRecordAuditorService extends BaseService<CustomerAuditRecordAuditorEntity, Integer> {

    List<CustomerAuditRecordAuditorEntity> findByCustomerAuditRecordId(Integer customerAuditRecordId);

    List<CustomerAuditRecordAuditorEntity> findByExample(Integer customerAuditRecordId, Integer auditorId, Integer sort, Short activate);

    void addMuch(List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities);

    void removeByCustomerAuditRecordId(Integer customerAuditRecordId);
}
