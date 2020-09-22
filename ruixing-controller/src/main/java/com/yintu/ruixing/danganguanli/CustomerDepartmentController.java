package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 14:30
 */
@RestController
@RequestMapping("/customer/departments")
public class CustomerDepartmentController extends SessionController implements BaseController<CustomerDepartmentEntity, Integer> {
    @Autowired
    private CustomerDepartmentService customerDepartmentService;

    @PostMapping
    public Map<String, Object> add(@Validated CustomerDepartmentEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        customerDepartmentService.add(entity);
        return ResponseDataUtil.ok("添加客户部门信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        customerDepartmentService.remove(id);
        return ResponseDataUtil.ok("删除客户部门信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated CustomerDepartmentEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        customerDepartmentService.edit(entity);
        return ResponseDataUtil.ok("修改客户部门信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        CustomerDepartmentEntity customerDepartmentEntity = customerDepartmentService.findById(id);
        return ResponseDataUtil.ok("查询客户部门信息成功", customerDepartmentEntity);
    }

    @GetMapping
    public Map<String, Object> findCustomerTypeAndCustomerDepartmentTree() {
        List<TreeNodeUtil> treeNodeUtils = customerDepartmentService.findCustomerTypeAndCustomerDepartmentTree();
        return ResponseDataUtil.ok("查询客户类型以及部门树信息成功", treeNodeUtils);
    }
}
