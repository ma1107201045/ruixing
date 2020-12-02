package com.yintu.ruixing.xitongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

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
    @Autowired
    private DataStatsService dataStatsService;


    @PostMapping
    public Map<String, Object> add(UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds, @RequestParam Long[] tids, @RequestParam Long[] dids, @RequestParam Long[] xids, @RequestParam Long[] cids) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getPassword(), "密码不能为空");
        Assert.notNull(userEntity.getAuthType(), "类型不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setCreateBy(this.getLoginUserName());
        userEntity.setCreateTime(new Date());
        userEntity.setModifiedBy(this.getLoginUserName());
        userEntity.setModifiedTime(new Date());
        userService.add(userEntity, roleIds, departmentIds, tids, dids, xids, cids);
        return ResponseDataUtil.ok("添加用户成功");
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        userService.removeByIds(ids);
        return ResponseDataUtil.ok("删除用户成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds, @RequestParam Long[] tids, @RequestParam Long[] dids, @RequestParam Long[] xids, @RequestParam Long[] cids) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getAuthType(), "类型不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setModifiedBy(this.getLoginUserName());
        userEntity.setModifiedTime(new Date());
        userService.edit(userEntity, roleIds, departmentIds, tids, dids, xids, cids);
        return ResponseDataUtil.ok("修改用户成功");
    }

    @PutMapping("/{id}/password")
    public Map<String, Object> editPassword(@PathVariable Long id, @RequestParam String password) {
        userService.editPassword(id, password);
        return ResponseDataUtil.ok("修改用户密码成功");
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
        List<DepartmentEntity> departmentEntities = departmentService.findByExample(new DepartmentEntityExample());
        return ResponseDataUtil.ok("查询部门列表信息成功", departmentEntities);
    }

    @GetMapping("/datas")
    public Map<String, Object> findFourLinkage() {
        List<TreeNodeUtil> treeNodeUtils = dataStatsService.findFourLinkage();
        return ResponseDataUtil.ok("查询铁电线车四级联动信息成功", treeNodeUtils);
    }


}
