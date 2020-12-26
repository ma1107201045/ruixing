package com.yintu.ruixing.danganguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.jiejuefangan.BiddingEntity;
import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.CustomerAuditRecordAuditorService;
import com.yintu.ruixing.master.danganguanli.CustomerDao;
import com.yintu.ruixing.xitongguanli.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

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
    private CustomerAuditRecordAuditorService customerAuditRecordAuditorService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;

    @Override
    public void add(CustomerEntity entity) {
        entity.setAuditStatus((short) 1);
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
        customerEntity.setAuditStatus((short) 1);
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
    public void addCustomerAuditRecord(CustomerEntity customerEntity, Integer[] customerDepartmentIds, Long[] auditorIds, Integer[] sorts, String trueName) {
        CustomerEntity cSource = this.findById(customerEntity.getId());
        if (cSource != null) {
            if (cSource.getAuditStatus() != 1)
                throw new BaseRuntimeException("此档案信息已在审批，无需重复审批");
            customerEntity.setAuditStatus((short) 2);//审核状态 1.正常 2.审核中 3.已审核通过 4.已审核未通过
            this.edit(customerEntity);
            Integer currentUserId = cSource.getUserId();

            CustomerAuditRecordEntity customerAuditRecordEntity = new CustomerAuditRecordEntity();
            BeanUtil.copyProperties(customerEntity, customerAuditRecordEntity, "id");
            customerAuditRecordEntity.setOperator(trueName);
            customerAuditRecordEntity.setCreateBy(customerAuditRecordEntity.getModifiedBy());
            customerAuditRecordEntity.setCreateTime(customerAuditRecordEntity.getModifiedTime());
            customerAuditRecordEntity.setCustomerId(cSource.getId());
            customerAuditRecordService.add(customerAuditRecordEntity);
            Integer customerAuditRecordId = customerAuditRecordEntity.getId();
            //去重
            Set<Integer> set = new HashSet<>(Arrays.asList(customerDepartmentIds));
            for (Integer customerDepartmentId : set) {
                CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity = new CustomerCustomerDepartmentEntity();
                customerCustomerDepartmentEntity.setCreateBy(customerEntity.getModifiedBy());
                customerCustomerDepartmentEntity.setCreateTime(customerEntity.getModifiedTime());
                customerCustomerDepartmentEntity.setModifiedBy(customerEntity.getModifiedBy());
                customerCustomerDepartmentEntity.setModifiedTime(customerEntity.getModifiedTime());
                customerCustomerDepartmentEntity.setCustomerAuditRecordId(customerAuditRecordId);
                customerCustomerDepartmentEntity.setCustomerAuditRecordDepartmentId(customerDepartmentId);
                customerCustomerDepartmentService.add(customerCustomerDepartmentEntity);//审核记录中部门信息
            }
            List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities = new ArrayList<>();
            if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 7, (short) 1, (short) 1);//查询已经配置好的审核人集
                List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                    if (!currentUserId.equals(auditConfigurationUserEntity.getUserId().intValue())) {//排除当前创建文件用户
                        CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity = new CustomerAuditRecordAuditorEntity();
                        customerAuditRecordAuditorEntity.setCustomerAuditRecordId(customerAuditRecordId);
                        customerAuditRecordAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                        Integer sort = auditConfigurationUserEntity.getSort();
                        customerAuditRecordAuditorEntity.setSort(sort);
                        if (sort == 1) {
                            customerAuditRecordAuditorEntity.setActivate((short) 1);
                        } else {
                            customerAuditRecordAuditorEntity.setActivate((short) 0);
                        }
                        customerAuditRecordAuditorEntity.setIsDispose((short) 0);
                        customerAuditRecordAuditorEntity.setAuditStatus((short) 2);
                        customerAuditRecordAuditorEntities.add(customerAuditRecordAuditorEntity);
                    }

                }
            } else { //没有提前配置好需要按照前台配好的审批流走
                if (sorts != null && auditorIds.length == sorts.length) {
                    for (int i = 0; i < auditorIds.length; i++) {
                        if (!currentUserId.equals(auditorIds[i].intValue())) {//排除当前创建文件用户
                            CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity = new CustomerAuditRecordAuditorEntity();
                            customerAuditRecordAuditorEntity.setCustomerAuditRecordId(customerAuditRecordId);
                            customerAuditRecordAuditorEntity.setAuditorId(auditorIds[i].intValue());
                            Integer sort = sorts[i];
                            customerAuditRecordAuditorEntity.setSort(sort);
                            if (sort == 1) {
                                customerAuditRecordAuditorEntity.setActivate((short) 1);
                            } else {
                                customerAuditRecordAuditorEntity.setActivate((short) 0);
                            }
                            customerAuditRecordAuditorEntity.setIsDispose((short) 0);
                            customerAuditRecordAuditorEntity.setAuditStatus((short) 2);
                            customerAuditRecordAuditorEntities.add(customerAuditRecordAuditorEntity);
                        }

                    }
                }
            }
            if (!customerAuditRecordAuditorEntities.isEmpty()) {
                customerAuditRecordAuditorService.addMuch(customerAuditRecordAuditorEntities);
                for (CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity : customerAuditRecordAuditorEntities) {
                    if (!currentUserId.equals(customerAuditRecordAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                        if (customerAuditRecordAuditorEntity.getSort() == 1) {//从第一批人开始发消息
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(customerAuditRecordEntity.getModifiedBy());
                            messageEntity.setCreateTime(customerAuditRecordEntity.getModifiedTime());
                            messageEntity.setModifiedBy(customerAuditRecordEntity.getModifiedBy());
                            messageEntity.setModifiedTime(customerAuditRecordEntity.getModifiedTime());
                            messageEntity.setTitle("档案管理");
                            messageEntity.setContext("顾客“" + cSource.getName() + "”的档案信息已修改，需要您审核！");
                            messageEntity.setType((short) 7);
                            messageEntity.setSmallType((short) 2);
                            messageEntity.setMessageType((short) 3);
                            messageEntity.setProjectId(null);
                            messageEntity.setFileId(null);
                            messageEntity.setSenderId(null);
                            messageEntity.setReceiverId(customerAuditRecordAuditorEntity.getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageService.sendMessage(messageEntity);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void audit(Integer id, Short isPass, String context, String accessoryName, String accessoryPath, Integer passUserId, Integer loginUserId, String userName, String trueName) {


        CustomerEntity customerEntity = this.findById(id);
        if (customerEntity != null) {
            if (!customerEntity.getAuditStatus().equals(loginUserId)) {
                throw new BaseRuntimeException("您无权审批此档案信息");
            }
            List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByCustomerIdAndAuditStatus(id, (short) 1);
            if (customerAuditRecordEntities.isEmpty()) {
                throw new BaseRuntimeException("此档案信息已审批，无需重复审核");
            }
            if (isPass == null)
                throw new BaseRuntimeException("审批状态不能为空");
            if (isPass != 2 && isPass != 3) {
                throw new BaseRuntimeException("此文件审批状态有误");
            }

            CustomerAuditRecordEntity customerAuditRecordEntity = customerAuditRecordEntities.get(0);
            customerAuditRecordEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setAuditStatus(isPass);
            customerAuditRecordService.edit(customerAuditRecordEntity);//更改档案审核记录信息

            customerEntity.setAuditStatus((short) 1);//审核通过或者未通过都需要改变状态，意味着此次审核周期结束。
            if (isPass == 2) {//只有审核通过才能同步顾客档案信息
                customerEntity.setModifiedBy(userName);
                customerEntity.setModifiedTime(new Date());
                customerEntity.setTypeId(customerAuditRecordEntity.getTypeId());
                customerEntity.setDutyId(customerAuditRecordEntity.getDutyId());
                customerEntity.setName(customerAuditRecordEntity.getName());
                customerEntity.setPhone(customerAuditRecordEntity.getPhone());
                customerEntity.setSpecialPlane(customerAuditRecordEntity.getSpecialPlane());
                customerEntity.setEmail(customerAuditRecordEntity.getEmail());
                customerEntity.setProvinceId(customerAuditRecordEntity.getProvinceId());
                customerEntity.setCityId(customerAuditRecordEntity.getCityId());
                customerEntity.setDistrictId(customerAuditRecordEntity.getDistrictId());
                customerEntity.setDetailedAddress(customerAuditRecordEntity.getDetailedAddress());
                List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByExample(null, customerAuditRecordEntity.getId());
                Integer[] customerDepartmentIds = new Integer[customerCustomerDepartmentEntities.size()];
                for (int i = 0; i < customerCustomerDepartmentEntities.size(); i++) {
                    customerDepartmentIds[i] = customerCustomerDepartmentEntities.get(i).getCustomerAuditRecordDepartmentId();
                }
                this.edit(customerEntity, customerDepartmentIds);//更新档案信息
            } else {
                this.edit(customerEntity);
            }
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
            messageEntity1.setReceiverId(customerAuditRecordEntity.getUserId());
            messageEntity1.setStatus((short) 1);
            messageService.sendMessage(messageEntity1);
            //给被审批人发消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(customerEntity.getModifiedBy());
            messageEntity.setCreateTime(customerEntity.getModifiedTime());
            messageEntity.setModifiedBy(customerEntity.getModifiedBy());
            messageEntity.setModifiedTime(customerEntity.getModifiedTime());
            messageEntity.setTitle("档案管理");
            messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已经被审核，请查看结果!");
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

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "顾客档案列表";
        //excel表名
        String[] headers = {"序号", "单位", "部门", "职务", "姓名", "手机", "座机", "邮箱", "邮寄地址", "创建时间", "状态"};
        //获取数据
        List<CustomerEntity> customerEntities = this.findByExample(ids, null, null, null);
        customerEntities = customerEntities.stream()
                .sorted(Comparator.comparing(CustomerEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[customerEntities.size()][headers.length];

        for (int i = 0; i < customerEntities.size(); i++) {
            CustomerEntity customerEntity = customerEntities.get(0);
            content[i][0] = customerEntity.getId().toString();
            content[i][1] = customerEntity.getCustomerTypeEntity().getName();
            StringBuilder sb = new StringBuilder();
            List<CustomerDepartmentEntity> customerDepartmentEntities = customerEntity.getCustomerDepartmentEntities();
            for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
                sb.append(customerDepartmentEntity.getName()).append("\n");
            }
            content[i][2] = sb.toString();
            content[i][3] = customerEntity.getCustomerDutyEntity().getName();
            content[i][4] = customerEntity.getName();
            content[i][5] = customerEntity.getPhone();
            content[i][6] = customerEntity.getSpecialPlane();
            content[i][7] = customerEntity.getEmail();
            content[i][8] = customerEntity.getDetailedAddress();
            content[i][9] = DateUtil.formatDateTime(customerEntity.getCreateTime());
            content[i][10] = customerEntity.getAuditStatus() == 1 ? "正常" : customerEntity.getAuditStatus() == 2 ? "审核中" : customerEntity.getAuditStatus() == 3 ? ".审批完成 " : "已撤销";
        }

        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
