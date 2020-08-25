package com.yintu.ruixing.xitongguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/19 17:36
 */
@RestController
@RequestMapping("/roles")
public class RoleController extends SessionController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public Map<String, Object> add(@Validated RoleEntity roleEntity, @RequestParam Long[] permissionIds) {
        roleEntity.setCreateBy(this.getLoginUserName());
        roleEntity.setCreateTime(new Date());
        roleEntity.setModifiedBy(this.getLoginUserName());
        roleEntity.setModifiedTime(new Date());
        roleService.addRoleAndPermissions(roleEntity, permissionIds);
        return ResponseDataUtil.ok("添加角色成功");
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        roleService.removeByIds(ids);
        return ResponseDataUtil.ok("删除角色成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, @Validated RoleEntity roleEntity, @RequestParam Long[] permissionIds) {
        roleEntity.setModifiedBy(this.getLoginUserName());
        roleEntity.setModifiedTime(new Date());
        roleService.editRoleAndPermissions(roleEntity, permissionIds);
        return ResponseDataUtil.ok("修改角色成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        RoleEntity roleEntity = roleService.findById(id);
        return ResponseDataUtil.ok("查询角色成功", roleEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "search_text", required = false) String name) {
        JSONObject jo = new JSONObject();
        List<String> requestMethods = permissionService.findRequestMethodsByUserIdAndUrl(this.getLoginUserId(), "/users");
        jo.put("requestMethods", requestMethods);
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<RoleEntity> roleEntities = roleService.findAllOrByName(name);
        PageInfo<RoleEntity> pageInfo = new PageInfo<>(roleEntities);
        jo.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询角色列表信息成功", jo);
    }

    @GetMapping("/permissions")
    public Map<String, Object> findPermissions() {
        List<TreeNodeUtil> treeNodeUtils = permissionService.findPermissionTree(-1L);
        return ResponseDataUtil.ok("查询权限树信息成功", treeNodeUtils);
    }

    @GetMapping("/{id}/permissions")
    public Map<String, Object> findPermissionsById(@PathVariable Long id) {
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        roleService.findPermissionsById(id, -1L, treeNodeUtils);
        return ResponseDataUtil.ok("查询角色权限信息成功", treeNodeUtils);
    }
}
