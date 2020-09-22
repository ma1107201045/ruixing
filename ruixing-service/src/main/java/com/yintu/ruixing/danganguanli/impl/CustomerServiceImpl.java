package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private UserService userService;

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
            List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByExample(id, null);
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
    public void addOrEditCustomerCustomerDepartment(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByExample(customerEntity.getId(), null);
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

    @Override
    public void addCustomerAuditRecord(CustomerEntity customerEntity, Integer[] customerDepartmentIds, String trueName) {
        CustomerEntity source = this.findById(customerEntity.getId());
        if (source != null) {
            if (source.getStatus() == 2)
                throw new BaseRuntimeException("此档案信息已在审批，无需重复审批");
            source = new CustomerEntity();
            source.setStatus((short) 2);//改为待审批状态
            this.edit(customerEntity);
            CustomerAuditRecordEntity customerAuditRecordEntity = new CustomerAuditRecordEntity();
            customerAuditRecordEntity.setCreateBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setCreateTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setOperator(trueName);
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
            customerAuditRecordEntity.setAuditorId(customerEntity.getAuditorId());
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
            messageEntity.setTitle("档案管理");
            messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已修改，需要您审核！");
            messageEntity.setType((short) 7);
            messageEntity.setSmallType((short) 2);
            messageEntity.setMessageType((short) 3);
            messageEntity.setProjectId(customerEntity.getId());
            messageEntity.setFileId(null);
            messageEntity.setSenderId(null);
            messageEntity.setReceiverId(customerEntity.getAuditorId());
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public void audit(Integer id, Short auditStatus, String reason, Integer loginUserId, String userName) {
        CustomerEntity customerEntity = this.findById(id);
        if (customerEntity != null) {
            if (!customerEntity.getAuditorId().equals(loginUserId)) {
                throw new BaseRuntimeException("您无权审批此档案信息");
            }
            List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByCustomerIdAndAuditStatus(id, (short) 1);
            if (customerAuditRecordEntities.isEmpty()) {
                throw new BaseRuntimeException("此档案信息已审批，无需重复审核");
            }
            if (auditStatus == null)
                throw new BaseRuntimeException("审批状态不能为空");
            if (auditStatus != 2 && auditStatus != 3) {
                throw new BaseRuntimeException("此文件审批状态有误");
            }
            CustomerAuditRecordEntity customerAuditRecordEntity = customerAuditRecordEntities.get(0);
            customerEntity.setModifiedBy(userName);
            customerEntity.setModifiedTime(new Date());
            customerEntity.setTypeId(customerAuditRecordEntity.getTypeId());
            customerEntity.setDutyId(customerAuditRecordEntity.getDutyId());
            customerEntity.setName(customerAuditRecordEntity.getName());
            customerEntity.setPhone(customerAuditRecordEntity.getPhone());
            customerEntity.setSpecialPlane(customerAuditRecordEntity.getSpecialPlane());
            customerEntity.setEmail(customerAuditRecordEntity.getSpecialPlane());
            customerEntity.setProvinceId(customerAuditRecordEntity.getProvinceId());
            customerEntity.setCityId(customerAuditRecordEntity.getCityId());
            customerEntity.setDistrictId(customerAuditRecordEntity.getDistrictId());
            customerEntity.setDetailedAddress(customerAuditRecordEntity.getDetailedAddress());
            customerEntity.setAuditorId(null);
            customerEntity.setStatus((short) 1);
            List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByExample(null, customerAuditRecordEntity.getId());
            Integer[] customerDepartmentIds = new Integer[customerCustomerDepartmentEntities.size()];
            for (int i = 0; i < customerCustomerDepartmentEntities.size(); i++) {
                customerDepartmentIds[i] = customerCustomerDepartmentEntities.get(i).getId();
            }
            this.edit(customerEntity, customerDepartmentIds);//更新档案信息

            customerAuditRecordEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setAuditStatus(auditStatus);
            customerAuditRecordEntity.setReason(auditStatus == 2 ? reason : null);
            customerAuditRecordService.edit(customerAuditRecordEntity);//更改档案审核记录信息

            //给审批人发审核结果消息
            MessageEntity messageEntity1 = new MessageEntity();
            messageEntity1.setCreateBy(customerEntity.getModifiedBy());
            messageEntity1.setCreateTime(customerEntity.getModifiedTime());
            messageEntity1.setModifiedBy(customerEntity.getModifiedBy());
            messageEntity1.setModifiedTime(customerEntity.getModifiedTime());
            messageEntity1.setTitle("档案管理");
            messageEntity1.setContext("顾客“" + customerEntity.getName() + "”的档案信息已经被您审核！");
            messageEntity1.setType((short) 7);
            messageEntity1.setSmallType((short) 2);
            messageEntity1.setMessageType((short) 3);
            messageEntity1.setProjectId(customerEntity.getId());
            messageEntity1.setFileId(null);
            messageEntity1.setSenderId(null);
            messageEntity1.setReceiverId(customerEntity.getAuditorId());
            messageEntity1.setStatus((short) 1);
            messageService.sendMessage(messageEntity1);
            //给被审批人发消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity1.setCreateBy(customerEntity.getModifiedBy());
            messageEntity1.setCreateTime(customerEntity.getModifiedTime());
            messageEntity1.setModifiedBy(customerEntity.getModifiedBy());
            messageEntity1.setModifiedTime(customerEntity.getModifiedTime());
            messageEntity.setTitle("文件");
            messageEntity1.setContext("顾客“" + customerEntity.getName() + "”的档案信息已经被审核，请查看结果!");
            messageEntity.setType((short) 7);
            messageEntity.setSmallType((short) 2);
            messageEntity.setMessageType((short) 3);
            messageEntity.setProjectId(customerEntity.getId());
            messageEntity.setFileId(null);
            messageEntity.setSenderId(null);
            List<UserEntity> userEntities = userService.findAllOrByUsername(customerAuditRecordEntity.getCreateBy());
            messageEntity.setReceiverId(userEntities.get(0).getId().intValue());
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }
}
