package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/3 12:00
 */
@RestController
@RequestMapping("/reports")
public class ReportController extends SessionController {
    @Autowired
    private QuDuanInfoService quDuanInfoService;


    /**
     * 根据车站id查询出不同的属性树
     *
     * @param czId 车站id
     * @return
     */
    @GetMapping("/properties/tree")
    public Map<String, Object> findByCzId(@RequestParam("czId") Integer czId, @RequestParam("isAll") Boolean isAll) {
        List<TreeNodeUtil> treeNodeUtils = quDuanInfoService.findPropertiesTree(czId, isAll);
        return ResponseDataUtil.ok("查询实时报表属性树成功", treeNodeUtils);
    }


    /**
     * 实时报表
     *
     * @param czId        车站id
     * @param properties  属性id集
     * @param isDianMaHua 是否电码化
     * @return
     */
    @GetMapping("/realreport/v2")
    public Map<String, Object> realTimeReport(@RequestParam("cz_id") Integer czId, @RequestParam("properties") Integer[] properties, @RequestParam("q_name") String qName, @RequestParam("is_dian_ma_hua") Boolean isDianMaHua) {
        JSONObject jo = quDuanInfoService.realTimeReport(czId, properties, qName, isDianMaHua);
        return ResponseDataUtil.ok("查询实时报表成功", jo);
    }


    /**
     * 日报表
     *
     * @param czId        车站
     * @param properties  属性集
     * @param isDianMaHua 是否电码化
     * @return
     */
    @GetMapping("/dayreport/v2")
    public Map<String, Object> dayReport(@RequestParam("cz_id") Integer czId, @RequestParam("time") Date time, @RequestParam("properties") Integer[] properties, @RequestParam("q_name") String qName, @RequestParam("is_dian_ma_hua") Boolean isDianMaHua) {
        JSONObject jo = quDuanInfoService.dayReport(czId, time, properties, qName, isDianMaHua);
        return ResponseDataUtil.ok("查询实时报表成功", jo);
    }

}
