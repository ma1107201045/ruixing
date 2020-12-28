package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 18:17
 */
@RestController
@RequestMapping("/line/technology/status/products/specifications")
public class LineTechnologyStatusProductSpecificationController extends SessionController implements BaseController<LineTechnologyStatusProductSpecificationEntity, Integer> {
    @Autowired
    private LineTechnologyStatusProductSpecificationService lineTechnologyStatusProductSpecificationService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusProductSpecificationEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductSpecificationService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态产品规格信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable  Integer id) {
        lineTechnologyStatusProductSpecificationService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态产品规格信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusProductSpecificationEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductSpecificationService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态产品规格信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusProductSpecificationEntity lineTechnologyStatusProductSpecificationEntity = lineTechnologyStatusProductSpecificationService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态产品规格信息成功", lineTechnologyStatusProductSpecificationEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<LineTechnologyStatusProductSpecificationEntity> lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductSpecificationService.findAll();
        lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusProductSpecificationEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态产品规格信息列表成功", lineTechnologyStatusProductModelNumberEntities);

    }
}
