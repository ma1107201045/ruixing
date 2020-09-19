package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.xitongguanli.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 顾客职务
 *
 * @author:mlf
 * @date:2020/8/10 14:38
 */
@RestController
@RequestMapping("/customer/duties")
public class CustomerDutyController extends SessionController {
    @Autowired
    private CustomerDutyService customerDutyService;
    @Autowired
    private CustomerUnitsService customerUnitsService;
    @Autowired
    private DepartmentService departmentService;


    @PostMapping
    public Map<String, Object> add(@Validated CustomerDutyEntity entity, @RequestParam Long[] departmentIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setModifiedBy(this.getLoginUserName());
        customerDutyService.add(entity, departmentIds, this.getLoginUserName());
        return ResponseDataUtil.ok("添加顾客职务信息成功");
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        customerDutyService.removeByIds(ids);
        return ResponseDataUtil.ok("删除顾客职务信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Long id, @Validated CustomerDutyEntity entity, @RequestParam Long[] departmentIds) {
        entity.setModifiedBy(this.getLoginUserName());
        customerDutyService.edit(entity, departmentIds, this.getLoginUserName());
        return ResponseDataUtil.ok("修改顾客职务信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        CustomerDutyEntity customerDutyEntity = customerDutyService.findById(id);
        return ResponseDataUtil.ok("查询顾客职务信息成功", customerDutyEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<CustomerDutyEntity> customerDutyEntities = customerDutyService.findByExample(new CustomerDutyEntity(null, null, null, null, null, name, null, null, null));
        PageInfo<CustomerDutyEntity> pageInfo = new PageInfo<>(customerDutyEntities);
        return ResponseDataUtil.ok("查询顾客职务列表信息成功", pageInfo);
    }

    @GetMapping("/customer/units")
    public Map<String, Object> findCustomerUnits() {
        List<CustomerUnitsEntity> customerUnitsEntities = customerUnitsService.findByExample(new CustomerUnitsEntity());
        return ResponseDataUtil.ok("查询客户单位列表信息成功", customerUnitsEntities);
    }

    @GetMapping("/departments")
    public Map<String, Object> findDepartments() {
        List<TreeNodeUtil> treeNodeUtils = departmentService.findDepartmentTree(-1L);
        return ResponseDataUtil.ok("查询部门列表信息成功", treeNodeUtils);
    }


}
