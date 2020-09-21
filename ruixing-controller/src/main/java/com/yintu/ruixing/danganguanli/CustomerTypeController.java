package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 11:27
 */
@RestController
@RequestMapping("/customer/types")
public class CustomerTypeController extends SessionController implements BaseController<CustomerTypeEntity, Integer> {
    @Autowired
    private CustomerTypeService customerTypeService;

    @PostMapping
    public Map<String, Object> add(@Validated CustomerTypeEntity entity) {
        customerTypeService.add(entity);
        return ResponseDataUtil.ok("添加顾客类型信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        customerTypeService.remove(id);
        return ResponseDataUtil.ok("删除顾客类型信息成功");

    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, CustomerTypeEntity entity) {
        customerTypeService.edit(entity);
        return ResponseDataUtil.ok("修改顾客类型信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        CustomerTypeEntity customerTypeEntity = customerTypeService.findById(id);
        return ResponseDataUtil.ok("查询顾客类型信息成功", customerTypeEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<CustomerTypeEntity> customerTypeEntities = customerTypeService.findAll();
        PageInfo<CustomerTypeEntity> pageInfo = new PageInfo<>(customerTypeEntities);
        return ResponseDataUtil.ok("查询顾客类型信息列表成功", pageInfo);
    }


}
