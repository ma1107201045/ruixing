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
    public Map<String, Object> findByCzId(@RequestParam("czId") Integer czId) {
        List<TreeNodeUtil> treeNodeUtils = quDuanInfoService.findPropertiesTree(czId);
        return ResponseDataUtil.ok("查询实时报表属性树成功", treeNodeUtils);
    }


    /**
     * 实时报表
     *
     * @return
     */
    @GetMapping("/realreport/v2")
    public Map<String, Object> realTimeReport(@RequestParam("cz_id") Integer czId, @RequestParam("properties") Integer[] properties) {
        JSONObject jo = quDuanInfoService.realTimeReport(czId, properties);
        return ResponseDataUtil.ok("查询实时报表成功", jo);
    }


    /**
     * 日报表
     *
     * @param time 日期
     */
    @GetMapping("/dailypaper")
    public Map<String, Object> findStatisticsByDate(@RequestParam("cz_id") Integer cZid,
                                                    @RequestParam("time") Date time) {
        List<Map<String, Object>> maps = quDuanInfoService.findStatisticsByCzIdAndTime(cZid, time);
        return ResponseDataUtil.ok("查询日报表成功", maps);
    }

}
