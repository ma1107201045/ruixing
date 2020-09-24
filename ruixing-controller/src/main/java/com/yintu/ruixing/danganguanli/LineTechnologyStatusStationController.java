package com.yintu.ruixing.danganguanli;

import cn.hutool.db.Session;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 10:46
 */
@RestController
@RequestMapping("/line/technology/status/stations")
public class LineTechnologyStatusStationController extends SessionController {
    @Autowired
    private LineTechnologyStatusStationService lineTechnologyStatusStationService;

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineTechnologyStatusStationEntity lineTechnologyStatusStationEntity) {
        lineTechnologyStatusStationEntity.setModifiedBy(this.getLoginUserName());
        lineTechnologyStatusStationEntity.setModifiedTime(new Date());
        lineTechnologyStatusStationService.edit(lineTechnologyStatusStationEntity);
        return ResponseDataUtil.ok("修改线段技术状态车站信息成功");
    }

    @GetMapping
    public Map<String, Object> findStationInfoAndStatistics(@RequestParam Integer cid) {
        Map<String, Object> map = lineTechnologyStatusStationService.findStationInfoAndStatistics(cid, this.getLoginUserName());
        return ResponseDataUtil.ok("查询线段技术状态统计车站信息成功", map);
    }

}
