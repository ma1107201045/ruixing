package com.yintu.ruixing.guzhangzhenduan;

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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * 数据分析
     *
     * @param qid 区段id
     * @return 实时的数据
     */
    @GetMapping("/dataanalysis")
    public Map<String, Object> findLastByQid(@RequestParam("qid") Integer qid) {
        QuDuanInfoEntityV2 quDuanInfoEntity = quDuanInfoService.findLastByQid(qid);
        return ResponseDataUtil.ok("查询区段详情", quDuanInfoEntity);
    }


    /**
     * 实时报表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @return
     */
    @GetMapping("/realreport")
    public Map<String, Object> findByCzId(@RequestParam("page_number") Integer pageNumber,
                                          @RequestParam("page_size") Integer pageSize,
                                          @RequestParam(value = "order_by", required = false, defaultValue = "qb.id ASC") String orderBy,
                                          @RequestParam("cz_id") Integer cZid) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 5);
        List<QuDuanInfoEntityV2> quDuanInfoEntities = quDuanInfoService.findByCzIdAndTime(cZid, c.getTime());
        PageInfo<QuDuanInfoEntityV2> pageInfo = new PageInfo<>(quDuanInfoEntities);
        return ResponseDataUtil.ok("查询实时报表成功", pageInfo);
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
                                                    @RequestParam(value = "order_by", required = false, defaultValue = "qb.id ASC") String orderBy,
                                                    @RequestParam("cz_id") Integer cZid,
                                                    @RequestParam("time") Date time) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<Map<String, Object>> maps = quDuanInfoService.findStatisticsByCzIdAndTime(cZid, time);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);
        return ResponseDataUtil.ok("查询日报表成功", pageInfo);
    }


    /*----------------------------------------------分割线---------------------------------------------------------------
       ---------------------------------------------v2版本----------------------------------------------------------------
     */

    /**
     * @param czId 车站id
     * @param time 时刻
     * @return
     */
    @GetMapping("/data")
    public Map<String, Object> findByCondition(@RequestParam("czId") Integer czId,
                                               @RequestParam(value = "time", required = false) Date time) {
        List<JSONObject> jsonObjects = quDuanInfoService.findByCondition(czId, time);
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
     * @param pageNumber 页码
     * @param pageSize   页数
     * @return
     */
    @GetMapping("/realreport/v2")
    public Map<String, Object> realreport(@RequestParam("page_number") Integer pageNumber,
                                          @RequestParam("page_size") Integer pageSize,
                                          @RequestParam(value = "order_by", required = false) String orderBy,
                                          @RequestParam("properties") Integer[] properties,
                                          @RequestParam("cz_id") Integer czId) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<JSONObject> jsonObjects = quDuanInfoService.realTimeReport(properties, czId);
        PageInfo<JSONObject> pageInfo = new PageInfo<>(jsonObjects);
        return ResponseDataUtil.ok("查询实时报表成功", pageInfo);
    }


}
