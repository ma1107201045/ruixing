package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/21 15:40
 */
public interface CustomerAuditRecordService extends BaseService<CustomerAuditRecordEntity, Integer> {

    List<CustomerAuditRecordEntity> findByExample(Integer[] ids, Integer customerId);

    List<CustomerAuditRecordEntity> findByCustomerIdAndAuditStatus(Integer customerId, Short auditStatus);
}
