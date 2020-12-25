package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/19 17:40
 */
@RestController
@RequestMapping("/permissions")
public class PermissionController extends SessionController {
    @Autowired
    private PermissionService permissionService;


    @PostMapping
    public Map<String, Object> add(@Validated PermissionEntity permissionEntity) {
        permissionEntity.setCreateBy(this.getLoginUserName());
        permissionEntity.setCreateTime(new Date());
        permissionEntity.setModifiedBy(this.getLoginUserName());
        permissionEntity.setModifiedTime(new Date());
        permissionService.add(permissionEntity);
        return ResponseDataUtil.ok("添加权限成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Long id) {
        permissionService.remove(id);
        return ResponseDataUtil.ok("删除权限成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, @Validated PermissionEntity permissionEntity) {
        permissionEntity.setModifiedBy(this.getLoginUserName());
        permissionEntity.setModifiedTime(new Date());
        permissionService.edit(permissionEntity);
        return ResponseDataUtil.ok("修改权限成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        PermissionEntity permissionEntity = permissionService.findById(id);
        return ResponseDataUtil.ok("查询权限成功", permissionEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<TreeNodeUtil> treeNodeUtils = permissionService.findPermissionTree(-1L);
        return ResponseDataUtil.ok("查询权限列表成功", treeNodeUtils);
    }
}
