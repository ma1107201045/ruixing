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
 * @date 2020/9/24 11:25
 */
@RestController
@RequestMapping("/line/technology/status/station/materials")
public class LineTechnologyStatusStationMaterialController extends SessionController implements BaseController<LineTechnologyStatusStationMaterialEntity, Integer> {
    @Autowired
    private LineTechnologyStatusStationMaterialService lineTechnologyStatusStationMaterialService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusStationMaterialEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationMaterialService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态软件物料信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusStationMaterialService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态软件物料信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusStationMaterialEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationMaterialService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态软件物料物料信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusStationMaterialEntity lineTechnologyStatusStationMaterialEntity = lineTechnologyStatusStationMaterialService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态软件物料物料信息成功", lineTechnologyStatusStationMaterialEntity);
    }

    @GetMapping
    public Map<String, Object> findByExample(@RequestParam Integer stationId) {
        List<LineTechnologyStatusStationMaterialEntity> lineTechnologyStatusStationMaterialEntities = lineTechnologyStatusStationMaterialService.findByExample(stationId);
        lineTechnologyStatusStationMaterialEntities = lineTechnologyStatusStationMaterialEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusStationMaterialEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态软件物料信息列表成功", lineTechnologyStatusStationMaterialEntities);
    }
}
