package com.yintu.ruixing.danganguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/28 14:15
 */
@Controller
@RequestMapping("/customers")
public class CustomerController extends SessionController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PermissionService permissionService;


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getPassword(), "密码不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setIsCustomer((short) 1);
        userService.addUserAndRoles(userEntity, roleIds, departmentIds, this.getLoginUserName());
        return ResponseDataUtil.ok("添加客户成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        userService.removeByIds(ids);
        return ResponseDataUtil.ok("删除客户成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Long id, UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {
        Assert.notNull(userEntity.getUsername(), "用户名不能为空");
        Assert.notNull(userEntity.getPassword(), "密码不能为空");
        Assert.notNull(userEntity.getEnableds(), "状态不能为空");
        userEntity.setIsCustomer((short) 1);
        userService.editUserAndRoles(userEntity, roleIds, departmentIds, this.getLoginUserName());
        return ResponseDataUtil.ok("修改客户成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Long id) {
        UserEntity userEntity = userService.findById(id);
        return ResponseDataUtil.ok("查询客户成功", userEntity);
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "search_text", required = false) String username) {
        JSONObject jo = new JSONObject();
        List<String> requestMethods = permissionService.findRequestMethodsByUserIdAndUrl(this.getLoginUserId(), "/customers");
        jo.put("requestMethods", requestMethods);
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<UserEntity> userEntities = userService.findAllOrByUsername(username, (short) 1);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        jo.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询客户列表成功", jo);
    }

    @GetMapping("/roles")
    @ResponseBody
    public Map<String, Object> findRoles() {
        List<RoleEntity> roleEntities = roleService.findAll();
        return ResponseDataUtil.ok("查询角色列表信息成功", roleEntities);
    }

    @GetMapping("/departments")
    @ResponseBody
    public Map<String, Object> findDepartments() {
        List<TreeNodeUtil> treeNodeUtils = departmentService.findDepartmentTree(-1L);
        return ResponseDataUtil.ok("查询部门列表信息成功", treeNodeUtils);
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Long[] ids, HttpServletResponse response) throws IOException {
        String fileName = "顾客档案列表信息" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        userService.exportFile(response.getOutputStream(), ids);
    }


}
