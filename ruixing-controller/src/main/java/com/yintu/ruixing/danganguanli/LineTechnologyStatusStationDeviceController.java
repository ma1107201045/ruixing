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
 * @date 2020/9/24 11:18
 */
@RestController
@RequestMapping("/line/technology/status/station/devices")
public class LineTechnologyStatusStationDeviceController extends SessionController implements BaseController<LineTechnologyStatusStationDeviceEntity, Integer> {
    @Autowired
    private LineTechnologyStatusStationDeviceService lineTechnologyStatusStationDeviceService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusStationDeviceEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationDeviceService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态管理设备硬件物料信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusStationDeviceService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态设备硬件物料信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusStationDeviceEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationDeviceService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态设备硬件物料信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusStationDeviceEntity lineTechnologyStatusStationDeviceEntity = lineTechnologyStatusStationDeviceService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态设备硬件物料信息成功", lineTechnologyStatusStationDeviceEntity);
    }

    @GetMapping
    public Map<String, Object> findByExample(@RequestParam Integer stationId) {
        List<LineTechnologyStatusStationDeviceEntity> lineTechnologyStatusStationDeviceEntities = lineTechnologyStatusStationDeviceService.findByExample(stationId);
        lineTechnologyStatusStationDeviceEntities = lineTechnologyStatusStationDeviceEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusStationDeviceEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态设备硬件物料信息列表成功", lineTechnologyStatusStationDeviceEntities);
    }
}
