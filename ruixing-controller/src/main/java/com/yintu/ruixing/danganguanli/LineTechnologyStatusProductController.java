package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 19:47
 */
@RestController
@RequestMapping("/line/technology/status/products")
public class LineTechnologyStatusProductController extends SessionController implements BaseController<LineTechnologyStatusProductEntityWithBLOBs, Integer> {

    @Autowired
    private LineTechnologyStatusProductService lineTechnologyStatusProductService;
    @Autowired
    private LineTechnologyStatusProductModelNumberService lineTechnologyStatusProductModelNumberService;
    @Autowired
    private LineTechnologyStatusProductSpecificationService lineTechnologyStatusProductSpecificationService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusProductEntityWithBLOBs entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态产品信息成功");
    }

    @Override
    public Map<String, Object> remove(@PathVariable Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        lineTechnologyStatusProductService.remove(ids);
        return ResponseDataUtil.ok("删除线段技术状态产品信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineTechnologyStatusProductEntityWithBLOBs entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态产品信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusProductEntityWithBLOBs lineTechnologyStatusProductEntityWithBLOBs = lineTechnologyStatusProductService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态产品信息成功", lineTechnologyStatusProductEntityWithBLOBs);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "cid", required = false) Integer cid) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineTechnologyStatusProductEntityWithBLOBs> lineTechnologyStatusProductEntityWithBLOBs = lineTechnologyStatusProductService.findByExample(name, cid);
        PageInfo<LineTechnologyStatusProductEntityWithBLOBs> pageInfo = new PageInfo<>(lineTechnologyStatusProductEntityWithBLOBs);
        return ResponseDataUtil.ok("查询线段技术状态产品信息列表成功", pageInfo);
    }

    @GetMapping("/model/numbers")
    public Map<String, Object> findProductModelNumber() {
        List<LineTechnologyStatusProductModelNumberEntity> lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberService.findAll();
        lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusProductModelNumberEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态产品型号信息列表成功", lineTechnologyStatusProductModelNumberEntities);

    }

    @GetMapping("/specifications")
    public Map<String, Object> findProductSpecification() {
        List<LineTechnologyStatusProductSpecificationEntity> lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductSpecificationService.findAll();
        lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusProductSpecificationEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态产品规格信息列表成功", lineTechnologyStatusProductModelNumberEntities);

    }


}
