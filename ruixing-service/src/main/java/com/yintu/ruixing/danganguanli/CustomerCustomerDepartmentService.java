package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import org.omg.CORBA.INTERNAL;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 17:27
 */
public interface CustomerCustomerDepartmentService extends BaseService<CustomerCustomerDepartmentEntity, Integer> {
    Long countByExample(Integer departmentId);

    List<CustomerCustomerDepartmentEntity> findByExample(Integer customerId, Integer customerAuditRecordId);
}
