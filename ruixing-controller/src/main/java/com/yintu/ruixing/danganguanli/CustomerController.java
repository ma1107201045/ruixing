package com.yintu.ruixing.danganguanli;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/5/28 14:15
 */
@Controller
@RequestMapping("/customers")
public class CustomerController extends SessionController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAuditRecordService customerAuditRecordService;
    @Autowired
    private CustomerTypeService customerTypeService;
    @Autowired
    private CustomerDepartmentService customerDepartmentService;
    @Autowired
    private CustomerCustomerDepartmentService customerCustomerDepartmentService;
    @Autowired
    private CustomerDutyService customerDutyService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated CustomerEntity customerEntity, @RequestParam("departmentIds") Integer[] customerDepartmentIds) {
        customerEntity.setCreateBy(this.getLoginUserName());
        customerEntity.setCreateTime(new Date());
        customerEntity.setModifiedBy(this.getLoginUserName());
        customerEntity.setModifiedTime(new Date());
        customerService.add(customerEntity, customerDepartmentIds);
        return ResponseDataUtil.ok("添加顾客信息成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        customerService.remove(ids);
        return ResponseDataUtil.ok("删除客户信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated CustomerEntity customerEntity,
                                    @RequestParam("departmentIds") Integer[] customerDepartmentIds) {
        customerEntity.setModifiedBy(this.getLoginUserName());
        customerEntity.setModifiedTime(new Date());
        customerService.addCustomerAuditRecord(customerEntity, customerDepartmentIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改顾客信息成功");
    }

    @PutMapping("/{id}/audit")
    @ResponseBody
    public Map<String, Object> audit(@PathVariable Integer id, @RequestParam("auditStatus") Short auditStatus, String reason) {
        customerService.audit(id, auditStatus, reason, this.getLoginUserId().intValue(), this.getLoginUserName());
        return ResponseDataUtil.ok("审核顾客信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        CustomerEntity customerEntity = customerService.findById(id);
        return ResponseDataUtil.ok("查询顾客信息成功", customerEntity);
    }

    @GetMapping("/{id}/audit/records")
    @ResponseBody
    public Map<String, Object> findAuditRecordById(@PathVariable Integer id) {
        List<CustomerAuditRecordEntity> customerAuditRecordEntities = customerAuditRecordService.findByExample(null, id);
        customerAuditRecordEntities = customerAuditRecordEntities.stream().
                sorted(Comparator.comparing(CustomerAuditRecordEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询顾客审核记录信息成功", customerAuditRecordEntities);
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "cer.id DESC") String orderBy,
                                       @RequestParam(value = "type_id", required = false) Integer typeId,
                                       @RequestParam(value = "department_id", required = false) Integer departmentId,
                                       @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<CustomerEntity> customerEntities = customerService.findByExample(null, typeId, departmentId, name);
        PageInfo<CustomerEntity> pageInfo = new PageInfo<>(customerEntities);
        if (departmentId != null) {
            pageInfo.setTotal(customerCustomerDepartmentService.countByExample(departmentId));
        } else {
            pageInfo.setTotal(customerService.countByExample(typeId, name));
        }
        return ResponseDataUtil.ok("查询顾客信息列表成功", pageInfo);
    }

    @GetMapping("/customer/types")
    @ResponseBody
    public Map<String, Object> findTypes() {
        List<CustomerTypeEntity> customerTypeEntities = customerTypeService.findAll();
        return ResponseDataUtil.ok("查询顾客类型信息列表成功", customerTypeEntities);
    }

    @GetMapping("/customer/departments")
    @ResponseBody
    public Map<String, Object> findDepartments(@RequestParam Short typeId) {
        List<TreeNodeUtil> treeNodeUtils = customerDepartmentService.findByParentIdAndTypeId(-1, typeId);
        return ResponseDataUtil.ok("查询顾客部门信息列表成功", treeNodeUtils);
    }

    @GetMapping("/customer/duties")
    @ResponseBody
    public Map<String, Object> findDuties() {
        List<CustomerDutyEntity> customerDutyEntities = customerDutyService.findByExample(null);
        customerDutyEntities = customerDutyEntities.stream().
                sorted(Comparator.comparing(CustomerDutyEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询顾客职位信息列表成功", customerDutyEntities);
    }

    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String trueName) {
        List<UserEntity> userEntities = userService.findByTruename(trueName);
        userEntities = userEntities
                .stream()
                .filter(userEntity -> !userEntity.getId().equals(this.getLoginUserId()))
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询审核人列表信息成功", userEntities);
    }

    /**
     * 查询配置的用户
     *
     * @return 用户列表
     */
    @GetMapping("/audit/configurations")
    @ResponseBody
    public Map<String, Object> findAuditConfigurations() {
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample(null, (short) 1, null);
        auditConfigurationEntities.forEach(auditConfigurationEntity -> auditConfigurationEntity.setRoleEntities(auditConfigurationEntity.getRoleEntities().stream()
                .filter(roleEntity -> !roleEntity.getId().equals(this.getLoginUserId()))
                .sorted(Comparator.comparing(RoleEntity::getId).reversed())
                .collect(Collectors.toList()))
        );
        return ResponseDataUtil.ok("查询审批流配置信息列表成功", auditConfigurationEntities);
    }


    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "顾客档案信息列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        customerService.exportFile(response.getOutputStream(), ids);
    }


}
