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
 * @date 2020/9/24 11:32
 */
@RestController
@RequestMapping("/line/technology/status/station/configurations")
public class LineTechnologyStatusStationConfigurationController extends SessionController implements BaseController<LineTechnologyStatusStationConfigurationEntity, Integer> {
    @Autowired
    private LineTechnologyStatusStationConfigurationService lineTechnologyStatusStationConfigurationService;

    @PostMapping
    public Map<String, Object> add(LineTechnologyStatusStationConfigurationEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationConfigurationService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态配置文件信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineTechnologyStatusStationConfigurationService.remove(id);
        return ResponseDataUtil.ok("删除线段技术状态配置文件信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusStationConfigurationEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusStationConfigurationService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态配置文件信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusStationConfigurationEntity lineTechnologyStatusStationConfigurationEntity = lineTechnologyStatusStationConfigurationService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态配置文件信息成功", lineTechnologyStatusStationConfigurationEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<LineTechnologyStatusStationConfigurationEntity> lineTechnologyStatusStationConfigurationEntities = lineTechnologyStatusStationConfigurationService.findAll();
        lineTechnologyStatusStationConfigurationEntities = lineTechnologyStatusStationConfigurationEntities
                .stream()
                .sorted(Comparator.comparing(LineTechnologyStatusStationConfigurationEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询线段技术状态配置文件信息列表成功", lineTechnologyStatusStationConfigurationEntities);
    }

}
