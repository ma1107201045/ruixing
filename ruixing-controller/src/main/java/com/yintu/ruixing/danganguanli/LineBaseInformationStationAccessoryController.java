package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:35
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information/station/accessory")
public class LineBaseInformationStationAccessoryController extends SessionController {

    @Autowired
    private LineBaseInformationStationAccessoryService lineBaseInformationStationAccessoryService;

    @PostMapping
    public Map<String, Object> add(@Validated LineBaseInformationStationAccessoryEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationStationAccessoryService.add(entity);
        return ResponseDataUtil.ok("添加线段基本信息附件成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationStationAccessoryService.remove(id);
        return ResponseDataUtil.ok("删除线段基本信息附件成功");
    }

    @GetMapping
    public Map<String, Object> findAll(@Validated LineBaseInformationStationAccessoryEntity query) {
        List<LineBaseInformationStationAccessoryEntity> lineBaseInformationStationAccessoryEntities = lineBaseInformationStationAccessoryService.findByExample(query);
        return ResponseDataUtil.ok("查询线段基本信息附件信息列表成功", lineBaseInformationStationAccessoryEntities);
    }
}
