package com.yintu.ruixing.danganguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.danganguanli.*;
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
        List<CustomerEntity> customerEntities = customerDao.selectByExample(ids, typeId, departmentId, name);
        for (CustomerEntity customerEntity : customerEntities) {
            List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByCustomerIdAndAuditStatus(customerEntity.getId(), (short) 2);
            if (customerAuditRecordEntities.isEmpty()) {
                customerEntity.setCustomerAuditRecordAuditorEntities(new ArrayList<>());
                continue;
            }
            List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities = customerAuditRecordAuditorService.findByCustomerAuditRecordId(customerAuditRecordEntities.get(0).getId());
            customerEntity.setCustomerAuditRecordAuditorEntities(customerAuditRecordAuditorEntities);
        }
        return customerEntities;
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
            cSource.setAuditStatus((short) 2);//审核状态 1.正常 2.审核中 3.已审核通过 4.已审核未通过
            this.edit(cSource);
            CustomerAuditRecordEntity customerAuditRecordEntity = new CustomerAuditRecordEntity();
            BeanUtil.copyProperties(customerEntity, customerAuditRecordEntity, "id");
            customerAuditRecordEntity.setOperator(trueName);
            customerAuditRecordEntity.setCreateBy(customerAuditRecordEntity.getModifiedBy());
            customerAuditRecordEntity.setCreateTime(customerAuditRecordEntity.getModifiedTime());
            customerAuditRecordEntity.setUserId(customerEntity.getUserId());
            customerAuditRecordEntity.setCustomerId(cSource.getId());
            customerAuditRecordEntity.setAuditStatus((short) 2);
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
            } else { //没有提前配置好需要按照前台配好的审批流走
                if (sorts != null && auditorIds.length == sorts.length) {
                    for (int i = 0; i < auditorIds.length; i++) {
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
            if (!customerAuditRecordAuditorEntities.isEmpty()) {
                customerAuditRecordAuditorService.addMuch(customerAuditRecordAuditorEntities);
                for (CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity : customerAuditRecordAuditorEntities) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(customerAuditRecordEntity.getModifiedBy());
                    messageEntity.setCreateTime(customerAuditRecordEntity.getModifiedTime());
                    messageEntity.setModifiedBy(customerAuditRecordEntity.getModifiedBy());
                    messageEntity.setModifiedTime(customerAuditRecordEntity.getModifiedTime());
                    messageEntity.setTitle("档案管理");
                    messageEntity.setContext("顾客“" + cSource.getName() + "”的档案信息已修改，需要您审核！");
                    messageEntity.setType((short) 8);
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


    @Override
    public void audit(Integer id, Short isPass, String context, String accessoryName, String accessoryPath, Integer passUserId, Integer loginUserId, String userName, String trueName) {
        if (isPass == null)
            throw new BaseRuntimeException("审核状态不能为空");
        if (isPass != 3 && isPass != 4 && isPass != 5) {
            throw new BaseRuntimeException("此文件审核状态有误");
        }
        CustomerEntity customerEntity = this.findById(id);
        if (customerEntity != null) {
            List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByCustomerIdAndAuditStatus(customerEntity.getId(), (short) 2);
            if (customerAuditRecordEntities.isEmpty())
                throw new BaseRuntimeException("此档案信息不需要审批");
            CustomerAuditRecordEntity customerAuditRecordEntity = customerAuditRecordEntities.get(0);
            //查询当前人是否有审核权限
            List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities = customerAuditRecordAuditorService.findByExample(customerAuditRecordEntity.getId(), loginUserId, null, (short) 1);
            if (customerAuditRecordAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
            }
            //取出当前审核人
            CustomerAuditRecordAuditorEntity now = customerAuditRecordAuditorEntities.get(0);

            customerAuditRecordAuditorEntities = customerAuditRecordAuditorService.findByExample(customerAuditRecordEntity.getId(), null, null, (short) 1);
            if (customerAuditRecordAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("此文件已审核，无需重复审核");
            }
            for (CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity : customerAuditRecordAuditorEntities) {//更改当前顺序的审核人群的激活状态
                customerAuditRecordAuditorEntity.setActivate((short) 0);
                customerAuditRecordAuditorEntity.setIsDispose((short) 1);
                customerAuditRecordAuditorEntity.setAuditStatus(isPass);
                customerAuditRecordAuditorEntity.setAuditFinishTime(new Date());
                customerAuditRecordAuditorService.edit(customerAuditRecordAuditorEntity);
            }
            Integer sort = now.getSort();//取出当前人审核顺序
            //转交时候
            if (isPass == 5) {
                if (passUserId == null)
                    throw new BaseRuntimeException("转交人id不能为空");
                now.setActivate((short) 0);
                now.setIsDispose((short) 1);
                now.setAuditStatus((short) 5);
                customerAuditRecordAuditorService.edit(now);
                CustomerAuditRecordAuditorEntity c = new CustomerAuditRecordAuditorEntity();
                c.setCustomerAuditRecordId(customerAuditRecordEntity.getId());
                c.setAuditorId(passUserId);
                c.setSort(sort);
                c.setActivate((short) 1);
                c.setIsDispose((short) 0);
                c.setAuditStatus((short) 2);
                customerAuditRecordAuditorService.add(c);
                //转交只给这个人发信息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(userName);
                messageEntity.setCreateTime(new Date());
                messageEntity.setModifiedBy(userName);
                messageEntity.setModifiedTime(new Date());
                messageEntity.setTitle("档案修改");
                messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已修改，需要您审核！");
                messageEntity.setType((short) 8);
                messageEntity.setSmallType((short) 2);
                messageEntity.setMessageType((short) 3);
                messageEntity.setProjectId(null);
                messageEntity.setFileId(null);
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(passUserId);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
                return;
            }
            now.setActivate((short) 0);
            now.setIsDispose((short) 1);
            now.setAuditStatus(isPass);
            now.setAuditFinishTime(new Date());
            now.setContext(context);
            now.setAccessoryName(accessoryName);
            now.setAccessoryPath(accessoryPath);
            customerAuditRecordAuditorService.edit(now);//记录每次审核人审核回复以及上传的附件

            //判断是否通过 通过继续让下一批人审批，直到所有人审批通过,不通过此次文件审核流程结束。
            if (isPass == 3) {
                customerAuditRecordAuditorEntities = customerAuditRecordAuditorService.findByExample(customerAuditRecordEntity.getId(), null, sort + 1, null);
                if (customerAuditRecordAuditorEntities.isEmpty()) {
                    BeanUtil.copyProperties(customerAuditRecordEntity, customerEntity, "id");
                    customerEntity.setModifiedBy(userName);
                    customerEntity.setModifiedTime(new Date());
                    customerEntity.setAuditStatus((short) 1);
                    customerEntity.setAuditFinishTime(null);
                    customerEntity.setAuditFinishTime(new Date());
                    List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByExample(null, customerAuditRecordEntity.getId());
                    Integer[] customerDepartmentIds = new Integer[customerCustomerDepartmentEntities.size()];
                    for (int i = 0; i < customerCustomerDepartmentEntities.size(); i++) {
                        customerDepartmentIds[i] = customerCustomerDepartmentEntities.get(i).getCustomerAuditRecordDepartmentId();
                    }
                    this.edit(customerEntity, customerDepartmentIds);//更新档案信息
                    customerAuditRecordEntity.setModifiedBy(userName);
                    customerAuditRecordEntity.setModifiedTime(new Date());
                    customerAuditRecordEntity.setAuditStatus((short) 3);
                    customerAuditRecordEntity.setAuditFinishTime(new Date());
                    customerAuditRecordService.edit(customerAuditRecordEntity);//更改审核记录审核状态


                    //给发起文件审核的人发消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(userName);
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy(userName);
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle("档案管理");
                    messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已被审核，请查看结果!");
                    messageEntity.setType((short) 8);
                    messageEntity.setSmallType((short) 2);
                    messageEntity.setMessageType((short) 3);
                    messageEntity.setProjectId(null);
                    messageEntity.setFileId(null);
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(customerAuditRecordEntity.getUserId());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                } else {
                    for (CustomerAuditRecordAuditorEntity customerAuditRecordAuditorEntity : customerAuditRecordAuditorEntities) {//更改当前顺序的审核人群的激活状态
                        customerAuditRecordAuditorEntity.setActivate((short) 1);
                        customerAuditRecordAuditorService.edit(customerAuditRecordAuditorEntity);
                        Integer currentUserId = customerAuditRecordEntity.getUserId();
                        if (!currentUserId.equals(customerAuditRecordAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(userName);
                            messageEntity.setCreateTime(new Date());
                            messageEntity.setModifiedBy(userName);
                            messageEntity.setModifiedTime(new Date());
                            messageEntity.setTitle("档案管理");
                            messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已修改，需要您审核！");
                            messageEntity.setType((short) 8);
                            messageEntity.setSmallType((short) 2);
                            messageEntity.setMessageType((short) 3);
                            messageEntity.setProjectId(null);
                            messageEntity.setFileId(null);
                            messageEntity.setSenderId(null);
                            messageEntity.setReceiverId(customerAuditRecordAuditorEntity.getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageService.sendMessage(messageEntity);//给下一批人开始发消息审核
                        }
                    }
                }

            } else {
                customerEntity.setModifiedBy(userName);
                customerEntity.setModifiedTime(new Date());
                customerEntity.setAuditStatus((short) 1);
                customerEntity.setAuditFinishTime(null);
                customerEntity.setAuditFinishTime(new Date());
                this.edit(customerEntity);//更改原始记录审核状态

                customerAuditRecordEntity.setModifiedBy(userName);
                customerAuditRecordEntity.setModifiedTime(new Date());
                customerAuditRecordEntity.setAuditStatus((short) 4);
                customerAuditRecordEntity.setAuditFinishTime(new Date());
                customerAuditRecordService.edit(customerAuditRecordEntity);//更改原始记录审核状态
                //给发起文件审核的人发消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(userName);
                messageEntity.setCreateTime(new Date());
                messageEntity.setModifiedBy(userName);
                messageEntity.setModifiedTime(new Date());
                messageEntity.setTitle("档案管理");
                messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息已被审核，请查看结果!");
                messageEntity.setType((short) 8);
                messageEntity.setSmallType((short) 2);
                messageEntity.setMessageType((short) 3);
                messageEntity.setProjectId(null);
                messageEntity.setFileId(null);
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(customerAuditRecordEntity.getUserId());
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
            //给审核人发审核结果消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(userName);
            messageEntity.setCreateTime(new Date());
            messageEntity.setModifiedBy(userName);
            messageEntity.setModifiedTime(new Date());
            messageEntity.setTitle("档案管理");
            messageEntity.setContext("顾客“" + customerEntity.getName() + "”的档案信息文件已被您审核！");
            messageEntity.setType((short) 8);
            messageEntity.setSmallType((short) 2);
            messageEntity.setMessageType((short) 3);
            messageEntity.setProjectId(null);
            messageEntity.setFileId(null);
            messageEntity.setSenderId(null);
            messageEntity.setReceiverId(loginUserId);
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

    @Override
    public List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId) {
        CustomerEntity customerEntity = this.findById(id);
        if (customerEntity == null)
            throw new BaseRuntimeException("审批内容不存在");
        List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByCustomerIdAndAuditStatus(customerEntity.getId(), (short) 2);
        CustomerAuditRecordEntity c = customerAuditRecordEntities.get(0);
        List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities = customerAuditRecordAuditorService.findByCustomerAuditRecordId(c.getId());
        List<Long> auditorIds = customerAuditRecordAuditorEntities.stream().map(customerAuditRecordAuditorEntity -> Long.valueOf(customerAuditRecordAuditorEntity.getAuditorId())).collect(Collectors.toList());
        auditorIds.add(loginUserId);
        auditorIds.add(c.getUserId().longValue());
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdNotIn(auditorIds);
        userEntityExample.setOrderByClause("id DESC");
        return userService.findByExample(userEntityExample);
    }

    @Override
    public List<AuditTotalVo> findByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose) {
        return customerDao.selectAuditRecordByExample(search, userId, auditStatus, auditorId, activate, isDispose);
    }

    @Override
    public List<CustomerEntity> findAllCustomer() {
        return customerDao.findAllCustomer();
    }
}
