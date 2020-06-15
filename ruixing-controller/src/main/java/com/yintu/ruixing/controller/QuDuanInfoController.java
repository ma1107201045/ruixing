package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.QuDuanBaseEntity;
import com.yintu.ruixing.entity.QuDuanInfoEntity;
import com.yintu.ruixing.service.QuDuanBaseService;
import com.yintu.ruixing.service.QuDuanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/3 12:00
 */
@RestController
@RequestMapping("/quduans")
public class QuDuanInfoController extends BaseController {
    @Autowired
    private QuDuanInfoService quDuanInfoService;

    @Autowired
    private QuDuanBaseService quDuanBaseService;

    /**
     * 查询车站基础信息集
     *
     * @param xid 线段id
     * @param cid 车站id
     * @return 车站基础信息集
     */
    @GetMapping
    public Map<String, Object> findByXidAndCid(@RequestParam(value = "xid") Integer xid,
                                               @RequestParam(value = "cid") Integer cid) {
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByXidAndCid(xid, cid);
        return ResponseDataUtil.ok("查询区段详情", quDuanBaseEntities);
    }


    /**
     * @param qid  区段id
     * @param time 时间
     * @return 实时的数据
     */
    @GetMapping("/{qid}")
    public Map<String, Object> findQidAndTime(@PathVariable Integer qid, @RequestParam("time") Date time) {
        List<QuDuanInfoEntity> quDuanInfoEntities = quDuanInfoService.findQidAndTime(qid, time);
        return ResponseDataUtil.ok("查询区段详情", quDuanInfoEntities);
    }

    /**
     * 实时报表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @param xid        线段id
     * @param cid        车站id
     * @return
     */
    @GetMapping("/realreport")
    public Map<String, Object> findByXidAndCidAndTime(@RequestParam(value = "page_number") Integer pageNumber,
                                                      @RequestParam(value = "page_size") Integer pageSize,
                                                      @RequestParam(value = "xid") Integer xid,
                                                      @RequestParam(value = "cid") Integer cid) {
        PageHelper.startPage(pageNumber, pageSize);
        List<QuDuanInfoEntity> quDuanInfoEntities = quDuanInfoService.findByXidAndCidAndTime(xid, cid, new Date());
        PageInfo<QuDuanInfoEntity> pageInfo = new PageInfo<>(quDuanInfoEntities);
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
    public Map<String, Object> findStatisticsByDate(@RequestParam(value = "page_number") Integer pageNumber,
                                                    @RequestParam(value = "page_size") Integer pageSize,
                                                    @RequestParam("time") Date time) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Map<String, Object>> maps = quDuanInfoService.findStatisticsByDate(time);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);
        return ResponseDataUtil.ok("查询日报表成功", pageInfo);
    }


}
