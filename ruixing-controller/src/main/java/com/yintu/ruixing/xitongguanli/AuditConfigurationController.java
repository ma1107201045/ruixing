package com.yintu.ruixing.xitongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    public Map<String, Object> add(@Validated AuditConfigurationEntity entity, Long[] roles, Long[] userIds, Integer[] sorts) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        auditConfigurationService.add(entity, roles, userIds, sorts);
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
    public Map<String, Object> edit(@PathVariable Long id, @Validated AuditConfigurationEntity entity, Long[] roles, Long[] userIds, Integer[] sorts) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        auditConfigurationService.edit(entity, roles, userIds, sorts);
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
                                       @RequestParam(value = "name_id", required = false) Short nameId,
                                       @RequestParam(value = "model", required = false) Short model) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample(nameId, null, model);
        PageInfo<AuditConfigurationEntity> pageInfo = new PageInfo<>(auditConfigurationEntities);
        return ResponseDataUtil.ok("查询审批流配置信息列表成功", pageInfo);
    }

    @GetMapping("/roles")
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = auditConfigurationService.findTree();
        return ResponseDataUtil.ok("查询树信息列表成功", treeNodeUtils);
    }

    @GetMapping("/{id}/roles")
    public Map<String, Object> findTree(@PathVariable Long id) {
        List<List<TreeNodeUtil>> lists = auditConfigurationService.findTreeById(id);
        return ResponseDataUtil.ok("查询树信息列表成功", lists);
    }

}
