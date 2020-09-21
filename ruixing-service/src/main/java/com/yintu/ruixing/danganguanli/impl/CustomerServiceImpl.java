package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.danganguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 17:22
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerCustomerDepartmentService customerCustomerDepartmentService;
    @Autowired
    private CustomerAuditRecordService customerAuditRecordService;
    @Autowired
    private MessageService messageService;

    @Override
    public void add(CustomerEntity entity) {
        entity.setStatus((short) 1);
        customerDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerEntity entity) {
        entity.setStatus((short) 2);
        customerDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerEntity findById(Integer id) {
        List<CustomerEntity> customerEntities = customerDao.selectByExample(new Integer[]{id}, null, null, null);
        return customerEntities.isEmpty() ? null : customerEntities.get(0);
    }

    @Override
    public List<CustomerEntity> findByExample(Integer[] ids, Integer typeId, Integer departmentId, String name) {
        return customerDao.selectByExample(ids, typeId, departmentId, name);
    }

    @Override
    public Long countByExample(Integer typeId, String name) {
        return customerDao.countByExample(typeId, name);
    }

    @Override
    public void add(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        this.add(customerEntity);
        this.addOrEditCustomerCustomerDepartment(customerEntity, customerDepartmentIds);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByCustomerId(id);
            for (CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity : customerCustomerDepartmentEntities) {
                customerCustomerDepartmentService.remove(customerCustomerDepartmentEntity.getId());
            }
            this.remove(id);
        }
    }

    @Override
    public void edit(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        this.edit(customerEntity);
        this.addOrEditCustomerCustomerDepartment(customerEntity, customerDepartmentIds);
    }

    @Override
    public void edit(CustomerEntity customerEntity, Integer[] customerDepartmentIds, Integer auditorId) {
        if (auditorId == null)
            throw new BaseRuntimeException("审批人id不能为空");
        CustomerEntity source = this.findById(customerEntity.getId());
        if (source != null) {
            source.setStatus((short) 2);//改为待审批状态
            this.edit(customerEntity);
            CustomerAuditRecordEntity customerAuditRecordEntity = new CustomerAuditRecordEntity();
            customerAuditRecordEntity.setCreateBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setCreateTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setTypeId(customerEntity.getTypeId());
            customerAuditRecordEntity.setDutyId(customerEntity.getDutyId());
            customerAuditRecordEntity.setName(customerEntity.getName());
            customerAuditRecordEntity.setPhone(customerEntity.getPhone());
            customerAuditRecordEntity.setSpecialPlane(customerEntity.getSpecialPlane());
            customerAuditRecordEntity.setEmail(customerEntity.getSpecialPlane());
            customerAuditRecordEntity.setProvinceId(customerEntity.getProvinceId());
            customerAuditRecordEntity.setCityId(customerEntity.getCityId());
            customerAuditRecordEntity.setDistrictId(customerEntity.getDistrictId());
            customerAuditRecordEntity.setDetailedAddress(customerEntity.getDetailedAddress());
            customerAuditRecordEntity.setCustomerId(customerEntity.getId());
            customerAuditRecordEntity.setAuditorId(auditorId);
            customerAuditRecordEntity.setAuditStatus((short) 1);
            customerAuditRecordService.add(customerAuditRecordEntity);//添加审核记录
            //去重
            Set<Integer> set = new HashSet<>(Arrays.asList(customerDepartmentIds));
            for (Integer customerDepartmentId : set) {
                CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity = new CustomerCustomerDepartmentEntity();
                customerCustomerDepartmentEntity.setCreateBy(customerEntity.getModifiedBy());
                customerCustomerDepartmentEntity.setCreateTime(customerEntity.getModifiedTime());
                customerCustomerDepartmentEntity.setModifiedBy(customerEntity.getModifiedBy());
                customerCustomerDepartmentEntity.setModifiedTime(customerEntity.getModifiedTime());
                customerCustomerDepartmentEntity.setCustomerAuditRecordId(customerAuditRecordEntity.getId());
                customerCustomerDepartmentEntity.setCustomerAuditRecordDepartmentId(customerDepartmentId);
                customerCustomerDepartmentService.add(customerCustomerDepartmentEntity);//审核记录中部门信息
            }
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(customerEntity.getModifiedBy());
            messageEntity.setCreateTime(customerEntity.getModifiedTime());
            messageEntity.setModifiedBy(customerEntity.getModifiedBy());
            messageEntity.setModifiedTime(customerEntity.getModifiedTime());
            messageEntity.setTitle("文件");
            messageEntity.setContext("客户“" + customerEntity.getName() + "”的档案信息的修改，需要您审核！");
            messageEntity.setType((short) 7);
            messageEntity.setSmallType((short) 1);
            messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(customerEntity.getId());
            messageEntity.setFileId(null);
            messageEntity.setSenderId(null);
            messageEntity.setReceiverId(auditorId);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public void addOrEditCustomerCustomerDepartment(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByCustomerId(customerEntity.getId());
        for (CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity : customerCustomerDepartmentEntities) {
            customerCustomerDepartmentService.remove(customerCustomerDepartmentEntity.getId());
        }
        //去重
        Set<Integer> set = new HashSet<>(Arrays.asList(customerDepartmentIds));
        for (Integer customerDepartmentId : set) {
            CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity = new CustomerCustomerDepartmentEntity();
            customerCustomerDepartmentEntity.setCreateBy(customerEntity.getModifiedBy());
            customerCustomerDepartmentEntity.setCreateTime(customerEntity.getModifiedTime());
            customerCustomerDepartmentEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerCustomerDepartmentEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerCustomerDepartmentEntity.setCustomerId(customerEntity.getId());
            customerCustomerDepartmentEntity.setDepartmentId(customerDepartmentId);
            customerCustomerDepartmentService.add(customerCustomerDepartmentEntity);
        }
    }


}
