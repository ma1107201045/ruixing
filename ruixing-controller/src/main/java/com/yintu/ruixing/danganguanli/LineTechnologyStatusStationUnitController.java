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
 * @date 2020/9/24 10:09
 */
@RestController
@RequestMapping("//line/technology/status/station/units")
public class LineTechnologyStatusStationUnitController extends SessionController implements BaseController<LineTechnologyStatusStationUnitEntity, Integer> {
    @Autowired
    private LineTechnologyStatusStationUnitService lineTechnologyStatusStationUnitService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusStationUnitEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationUnitService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态管理单位信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusStationUnitService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态管理单位信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusStationUnitEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationUnitService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态管理单位信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusStationUnitEntity lineTechnologyStatusStationUnitEntity = lineTechnologyStatusStationUnitService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态管理单位信息成功", lineTechnologyStatusStationUnitEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<LineTechnologyStatusStationUnitEntity> lineTechnologyStatusStationUnitEntities = lineTechnologyStatusStationUnitService.findAll();
        lineTechnologyStatusStationUnitEntities = lineTechnologyStatusStationUnitEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusStationUnitEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态管理单位信息列表成功", lineTechnologyStatusStationUnitEntities);
    }
}
