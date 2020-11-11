package com.yintu.ruixing.xitongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/19 17:20
 */
@RestController
@RequestMapping("/users")
public class UserController extends SessionController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;


    @PostMapping
    public Map<String, Object> add(UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getPassword(), "密码不能为空");
        Assert.notNull(userEntity.getAuthType(), "类型不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setCreateBy(this.getLoginUserName());
        userEntity.setCreateTime(new Date());
        userEntity.setModifiedBy(this.getLoginUserName());
        userEntity.setModifiedTime(new Date());
        userService.addUserAndRoles(userEntity, roleIds, departmentIds );
        return ResponseDataUtil.ok("添加用户成功");
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        userService.removeByIds(ids);
        return ResponseDataUtil.ok("删除用户成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getPassword(), "密码不能为空");
        Assert.notNull(userEntity.getAuthType(), "类型不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setModifiedBy(this.getLoginUserName());
        userEntity.setModifiedTime(new Date());
        userService.editUserAndRoles(userEntity, roleIds, departmentIds);
        return ResponseDataUtil.ok("修改用户成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        UserEntity userEntity = userService.findById(id);
        return ResponseDataUtil.ok("查询用户成功", userEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "search_text", required = false) String username) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<UserEntity> userEntities = userService.findAllOrByUsername(username);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        return ResponseDataUtil.ok("查询用户列表成功", pageInfo);
    }

    @GetMapping("/roles")
    public Map<String, Object> findRoles() {
        List<RoleEntity> roleEntities = roleService.findAll();
        return ResponseDataUtil.ok("查询角色列表信息成功", roleEntities);
    }


    @GetMapping("/departments")
    public Map<String, Object> findDepartments() {
        List<TreeNodeUtil> treeNodeUtils = departmentService.findDepartmentTree(-1L);
        return ResponseDataUtil.ok("查询部门列表信息成功", treeNodeUtils);
    }


}
