package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 17:21
 */
public interface CustomerService extends BaseService<CustomerEntity, Integer> {

    Long countByExample(Integer typeId, String name);

    void add(CustomerEntity customerEntity, Integer[] customerDepartmentIds);

    void remove(Integer[] ids);

    void edit(CustomerEntity customerEntity, Integer[] customerDepartmentIds);

    void addOrEditCustomerCustomerDepartment(CustomerEntity customerEntity, Integer[] customerDepartmentIds);

    List<CustomerEntity> findByExample(Integer[] ids, Integer typeId, Integer departmentId, String name);


    void addCustomerAuditRecord(CustomerEntity customerEntity, Integer[] customerDepartmentIds, String trueName);

    void audit(Integer id, Short auditStatus, String reason, Integer loginUserId, String userName);

}
