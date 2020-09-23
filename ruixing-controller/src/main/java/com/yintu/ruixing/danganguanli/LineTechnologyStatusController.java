package com.yintu.ruixing.danganguanli;

import com.alibaba.fastjson.JSONObject;
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
 * @date 2020/9/22 19:13
 */
@RestController
@RequestMapping("/line/technology/status")
public class LineTechnologyStatusController extends SessionController {
    @Autowired
    private LineTechnologyStatusService lineTechnologyStatusService;

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineTechnologyStatusEntityWithBLOBs lineTechnologyStatusEntityWithBLOBs) {
        lineTechnologyStatusEntityWithBLOBs.setModifiedBy(this.getLoginUserName());
        lineTechnologyStatusEntityWithBLOBs.setModifiedTime(new Date());
        lineTechnologyStatusService.edit(lineTechnologyStatusEntityWithBLOBs);
        return ResponseDataUtil.ok("修改线段技术状态信息成功");
    }

    @GetMapping
    public Map<String, Object> findLineInfoAndStatistics(@RequestParam Integer xid) {
        JSONObject jo = lineTechnologyStatusService.findLineInfoAndStatistics(xid, this.getLoginUserName());
        return ResponseDataUtil.ok("查询线段技术状态信息成功", jo);
    }

}
