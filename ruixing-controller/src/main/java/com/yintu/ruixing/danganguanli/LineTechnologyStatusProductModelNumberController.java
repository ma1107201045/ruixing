package com.yintu.ruixing.danganguanli;

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
 * @date 2020/9/24 18:20
 */
@RestController
@RequestMapping("/line/technology/status/product/model/numbers")
public class LineTechnologyStatusProductModelNumberController extends SessionController implements BaseController<LineTechnologyStatusProductModelNumberEntity, Integer> {
    @Autowired
    private LineTechnologyStatusProductModelNumberService lineTechnologyStatusProductModelNumberService;

    @PostMapping
    public Map<String, Object> add(@Validated LineTechnologyStatusProductModelNumberEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductModelNumberService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态产品型号信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusProductModelNumberService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态产品型号信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineTechnologyStatusProductModelNumberEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusProductModelNumberService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态产品型号信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusProductModelNumberEntity lineTechnologyStatusProductModelNumberEntity = lineTechnologyStatusProductModelNumberService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态产品型号信息成功", lineTechnologyStatusProductModelNumberEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<LineTechnologyStatusProductModelNumberEntity> lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberService.findAll();
        lineTechnologyStatusProductModelNumberEntities = lineTechnologyStatusProductModelNumberEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusProductModelNumberEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态产品型号信息列表成功", lineTechnologyStatusProductModelNumberEntities);

    }
}
