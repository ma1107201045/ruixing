package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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


    @PostMapping
    public Map<String, Object> add(@Validated CustomerDutyEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        customerDutyService.add(entity);
        return ResponseDataUtil.ok("添加顾客职务信息成功");
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        customerDutyService.remove(ids);
        return ResponseDataUtil.ok("删除顾客职务信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated CustomerDutyEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        customerDutyService.edit(entity);
        return ResponseDataUtil.ok("修改顾客职务信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        CustomerDutyEntity customerDutyEntity = customerDutyService.findById(id);
        return ResponseDataUtil.ok("查询顾客职务信息成功", customerDutyEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<CustomerDutyEntity> customerDutyEntities = customerDutyService.findByExample(name);
        PageInfo<CustomerDutyEntity> pageInfo = new PageInfo<>(customerDutyEntities);
        return ResponseDataUtil.ok("查询顾客职务信息列表成功", pageInfo);
    }


}
