package com.yintu.ruixing.guzhangzhenduan;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author:mlf
 * @date:2020/6/3 12:00
 */
@RestController
@RequestMapping("/quduan/infos")
public class QuDuanInfoController extends SessionController {
    @Autowired
    private QuDuanInfoService quDuanInfoService;

    /**
     * 按照车站随机取出一条区段详情
     *
     * @param czId 区段id
     * @return
     */
    @GetMapping("/random")
    public Map<String, Object> findLastBycZId(@RequestParam("czId") Integer czId) {
        QuDuanInfoEntityV2 quDuanInfoEntity = quDuanInfoService.findLastBycZId(czId);
        return ResponseDataUtil.ok("查询区段详情成功", quDuanInfoEntity);
    }

    /**
     * 日报表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @param time       日期
     */
    @GetMapping("/dailypaper")
    public Map<String, Object> findStatisticsByDate(@RequestParam("page_number") Integer pageNumber,
                                                    @RequestParam("page_size") Integer pageSize,
                                                    @RequestParam("cz_id") Integer cZid,
                                                    @RequestParam("time") Date time) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Map<String, Object>> maps = quDuanInfoService.findStatisticsByCzIdAndTime(cZid, time);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);
        return ResponseDataUtil.ok("查询日报表成功", pageInfo);
    }


    /*----------------------------------------------分割线---------------------------------------------------------------
       ---------------------------------------------v2版本----------------------------------------------------------------
     */

    /**
     * @param czId      车站id
     * @param startTime 开始时刻
     * @param endTime   结束时刻
     * @return
     */
    @GetMapping("/data")
    public Map<String, Object> findByCondition(@RequestParam("czId") Integer czId,
                                               @RequestParam(value = "startTime", required = false) Date startTime,
                                               @RequestParam(value = "endTime", required = false) Date endTime) {
        List<JSONObject> jsonObjects = quDuanInfoService.findByCondition(czId, startTime, endTime);
//        byte[] result = ZipUtil.gzip(JSONArray.toJSON(jsonObjects).toString(), "utf-8");
        return ResponseDataUtil.ok("查询区段详情成功", jsonObjects);
    }

    /**
     * @param czId 车站id
     * @return
     */
    @GetMapping("/properties")
    public Map<String, Object> findNullProperties(@RequestParam("czId") Integer czId) {
        JSONObject jsonObjects = quDuanInfoService.findNullProperties(czId);
        return ResponseDataUtil.ok("查询区段属性成功", jsonObjects);
    }


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

}
