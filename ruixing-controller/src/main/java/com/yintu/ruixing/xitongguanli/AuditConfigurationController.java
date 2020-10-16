package com.yintu.ruixing.xitongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 15:12
 */
@RestController
@RequestMapping("/audit/configurations")
public class AuditConfigurationController extends SessionController {

    @Autowired
    private AuditConfigurationService auditConfigurationService;
    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public Map<String, Object> add(@Validated AuditConfigurationEntity entity, @RequestParam Long[] userIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        auditConfigurationService.add(entity, userIds);
        return ResponseDataUtil.ok("添加审批流配置信息成功");
    }


    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        auditConfigurationService.remove(auditConfigurationEntityExample);
        return ResponseDataUtil.ok("删除审批流配置信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, @Validated AuditConfigurationEntity entity, @RequestParam Long[] userIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        auditConfigurationService.edit(entity, userIds);
        return ResponseDataUtil.ok("修改审批流配置信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        AuditConfigurationEntity auditConfigurationEntity = auditConfigurationService.findById(id);
        return ResponseDataUtil.ok("查询审批流配置信息成功", auditConfigurationEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "department_name", required = false) String departmentName) {
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample(pageNumber, pageSize, orderBy, name, departmentName);
        PageInfo<AuditConfigurationEntity> pageInfo = new PageInfo<>(auditConfigurationEntities);
        return ResponseDataUtil.ok("查询审批流配置信息列表成功", pageInfo);
    }

    @GetMapping("/departments")
    public Map<String, Object> findDepartments() {
        List<DepartmentEntity> departmentEntities = departmentService.findByExample(new DepartmentEntityExample());
        departmentEntities = departmentEntities.stream()
                .sorted(Comparator.comparing(DepartmentEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询部门信息列表成功", departmentEntities);
    }

    @GetMapping("/users")
    public Map<String, Object> findUsers(@RequestParam Long departmentId) {
        List<UserEntity> userEntities = departmentService.findUsersById(departmentId);
        userEntities = userEntities.stream()
                .sorted(Comparator.comparing(UserEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询用户信息列表成功", userEntities);
    }
}
