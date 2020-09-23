package com.yintu.ruixing.danganguanli;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeService;
import com.yintu.ruixing.common.SessionController;
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
 * @date 2020/9/22 19:13
 */
@RestController
@RequestMapping("/line/technology/status")
public class LineTechnologyStatusController extends SessionController {
    @Autowired
    private LineTechnologyStatusService lineTechnologyStatusService;
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeService anZhuangTiaoShiCheZhanXiangMuTypeService;

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineTechnologyStatusEntityWithBLOBs lineTechnologyStatusEntityWithBLOBs, Integer[] xiangmutypeIds) {
        lineTechnologyStatusEntityWithBLOBs.setModifiedBy(this.getLoginUserName());
        lineTechnologyStatusEntityWithBLOBs.setModifiedTime(new Date());
        lineTechnologyStatusService.edit(lineTechnologyStatusEntityWithBLOBs, xiangmutypeIds);
        return ResponseDataUtil.ok("修改线段技术状态信息成功");
    }

    @GetMapping("/railways/bureau")
    public Map<String, Object> findRailwaysBureauStatistics(@RequestParam Integer tid) {
        Map<String, Object> map = lineTechnologyStatusService.findRailwaysBureauStatistics(tid);
        return ResponseDataUtil.ok("查询线段技术状态铁路局统计信息成功", map);
    }

    @GetMapping("/signal/depot")
    public Map<String, Object> findSignalDepotStatistics(@RequestParam Integer did) {
        Map<String, Object> map = lineTechnologyStatusService.findSignalDepotStatistics(did);
        return ResponseDataUtil.ok("查询线段技术状态电务段统计信息成功", map);
    }

    @GetMapping("/special/railway/line")
    public Map<String, Object> findLineInfoAndStatistics(@RequestParam Integer xid) {
        JSONObject jo = lineTechnologyStatusService.findLineInfoAndStatistics(xid, this.getLoginUserName());
        return ResponseDataUtil.ok("查询线段技术状态线段统计信息成功", jo);
    }

    @GetMapping("/xiangmutypes")
    public Map<String, Object> findXiangmuTypes() {
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> anZhuangTiaoShiCheZhanXiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeService.findAllXiangMuType(null, null);
        anZhuangTiaoShiCheZhanXiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeEntities
                .stream()
                .sorted(Comparator.comparing(AnZhuangTiaoShiCheZhanXiangMuTypeEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询设备类型信息列表成功", anZhuangTiaoShiCheZhanXiangMuTypeEntities);
    }
}
