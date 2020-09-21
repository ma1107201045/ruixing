package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/5/28 14:15
 */
@Controller
@RequestMapping("/customers")
public class CustomerController extends SessionController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerTypeService customerTypeService;
    @Autowired
    private CustomerDepartmentService customerDepartmentService;
    @Autowired
    private CustomerDutyService customerDutyService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated CustomerEntity customerEntity, @RequestParam("departmentIds") Integer[] customerDepartmentIds) {
        customerService.add(customerEntity, customerDepartmentIds);
        return ResponseDataUtil.ok("添加顾客信息成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        customerService.remove(ids);
        return ResponseDataUtil.ok("删除客户信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated CustomerEntity customerEntity, @RequestParam("departmentIds") Integer[] customerDepartmentIds) {
        customerService.edit(customerEntity, customerDepartmentIds);
        return ResponseDataUtil.ok("修改顾客信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        CustomerEntity customerEntity = customerService.findById(id);
        return ResponseDataUtil.ok("查询顾客信息成功", customerEntity);
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "cer.id DESC") String orderBy,
                                       @RequestParam(value = "type_id", required = false) Integer typeId,
                                       @RequestParam(value = "department_id", required = false) Integer departmentId,
                                       @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<CustomerEntity> customerEntities = customerService.findByExample(null, typeId, departmentId, name);
        PageInfo<CustomerEntity> pageInfo = new PageInfo<>(customerEntities);
        return ResponseDataUtil.ok("查询顾客信息列表成功", pageInfo);
    }

    @GetMapping("/type")
    @ResponseBody
    public Map<String, Object> findTypes() {
        List<CustomerTypeEntity> customerTypeEntities = customerTypeService.findAll();
        return ResponseDataUtil.ok("查询顾客类型信息列表成功", customerTypeEntities);
    }

    @GetMapping("/department")
    @ResponseBody
    public Map<String, Object> findDepartments(@RequestParam Short typeId) {
        List<TreeNodeUtil> treeNodeUtils = customerDepartmentService.findByParentIdAndTypeId(-1, typeId);
        return ResponseDataUtil.ok("查询顾客部门信息列表成功", treeNodeUtils);
    }

    @GetMapping("/duty")
    @ResponseBody
    public Map<String, Object> findDyties() {
        List<CustomerDutyEntity> customerDutyEntities = customerDutyService.findByExample(null);
        customerDutyEntities = customerDutyEntities.stream().
                sorted(Comparator.comparing(CustomerDutyEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询顾客职位信息列表成功", customerDutyEntities);
    }


//    @GetMapping("/export/{ids}")
//    public void exportFile(@PathVariable Long[] ids, HttpServletResponse response) throws IOException {
//        return;
//    }


}
