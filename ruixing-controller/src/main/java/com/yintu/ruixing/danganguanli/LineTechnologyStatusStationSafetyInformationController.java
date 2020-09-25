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
 * @date 2020/9/24 11:21
 */
@RestController
@RequestMapping("/line/technology/status/station/safety/informations")
public class LineTechnologyStatusStationSafetyInformationController extends SessionController implements BaseController<LineTechnologyStatusStationSafetyInformationEntity, Integer> {

    @Autowired
    private LineTechnologyStatusStationSafetyInformationService lineTechnologyStatusStationSafetyInformationService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusStationSafetyInformationEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationSafetyInformationService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态安全信息套数信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusStationSafetyInformationService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态安全信息套数信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusStationSafetyInformationEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationSafetyInformationService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态安全信息套数信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusStationSafetyInformationEntity lineTechnologyStatusStationSafetyInformationEntity = lineTechnologyStatusStationSafetyInformationService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态安全信息套数信息成功", lineTechnologyStatusStationSafetyInformationEntity);
    }

    @GetMapping
    public Map<String, Object> findByExample(@RequestParam Integer stationId) {
        List<LineTechnologyStatusStationSafetyInformationEntity> lineTechnologyStatusStationSafetyInformationEntities = lineTechnologyStatusStationSafetyInformationService.findByExample(stationId);
        lineTechnologyStatusStationSafetyInformationEntities = lineTechnologyStatusStationSafetyInformationEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusStationSafetyInformationEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态安全信息套数信息列表成功", lineTechnologyStatusStationSafetyInformationEntities);
    }
}
