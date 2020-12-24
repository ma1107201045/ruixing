package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/24 9:37
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information/accessory")
public class LineBaseInformationAccessoryController extends SessionController {
    @Autowired
    private LineBaseInformationAccessoryService lineBaseInformationAccessoryService;

    @PostMapping
    public Map<String, Object> add(@Validated LineBaseInformationAccessoryEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationAccessoryService.add(entity);
        return ResponseDataUtil.ok("添加线段基本信息附件成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationAccessoryService.remove(id);
        return ResponseDataUtil.ok("删除线段基本信息附件成功");
    }

    @GetMapping
    public Map<String, Object> findAll(@Validated LineBaseInformationAccessoryEntity query) {
        List<LineBaseInformationAccessoryEntity> lineBaseInformationAccessoryEntities = lineBaseInformationAccessoryService.findByExample(query);
        return ResponseDataUtil.ok("查询线段基本信息附件信息列表成功", lineBaseInformationAccessoryEntities);
    }
}
