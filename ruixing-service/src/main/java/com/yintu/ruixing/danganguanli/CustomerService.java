package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.xitongguanli.UserEntity;

import java.io.IOException;
import java.io.OutputStream;
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


    void addCustomerAuditRecord(CustomerEntity customerEntity, Integer[] customerDepartmentIds, Long[] auditorIds, Integer[] sorts, String trueName);

    void audit(Integer id, Short isPass, String context, String accessoryName, String accessoryPath, Integer passUserId, Integer loginUserId, String userName, String trueName);

    /**
     * 批量导出excel数据
     *
     * @param outputStream 输出流
     * @param ids          id集合
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;


    /**
     * 查询转交人列表（排除当前审核文件人列表以及当前登录人）
     *
     * @param id          文件id
     * @param loginUserId 当前登录用户id
     * @return 审核人列表
     */
    List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId);

    /**
     * 按照不同条件查询
     *
     * @param search      文件名称
     * @param userId      创建文件的用户id
     * @param auditStatus 文件审批状态
     * @param auditorId   文件审批人id
     * @param isDispose   是否处理
     * @return 审批总和视图
     */
    List<AuditTotalVo> findByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);


}
