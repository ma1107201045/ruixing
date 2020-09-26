package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.*;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

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

    @PostMapping
    public Map<String, Object> add(@Validated AuditConfigurationEntity entity, Long[] userIds) {
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
    public Map<String, Object> edit(@PathVariable Long id, @Validated AuditConfigurationEntity entity, Long[] userIds) {
        auditConfigurationService.edit(entity, userIds);
        return ResponseDataUtil.ok("修改审批流配置信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        AuditConfigurationEntity auditConfigurationEntity = auditConfigurationService.findById(id);
        return ResponseDataUtil.ok("查询审批流配置信息成功", auditConfigurationEntity);
    }
}
