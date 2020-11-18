package com.yintu.ruixing.guzhangzhenduan;


import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * TODO
 *
 * @description: 故障诊断地图页面
 * @author: Qiao
 * @time: 2020/5/21 17:17
 */
@RestController
@RequestMapping("/lieBiao")
public class ListController {
    @Autowired
    private ListService ls;
    @Autowired
    private DataStatsService dataStatsService;

    @GetMapping("/getLieBiao")
    public Object getLieBiao() {
        return ResponseDataUtil.ok("查询数据成功", ls.getMenuList());
    }

    @GetMapping("/getErJi")
    public Object getErJi() {
        return ResponseDataUtil.ok("查询数据成功", ls.getErJi());
    }

    @GetMapping("/getSanJi")
    public Object getSanJi() {
        return ResponseDataUtil.ok("查询数据成功", ls.getSanJi());
    }


    //获取第一级和第二级的数据
    @GetMapping("/findOneTwoDatas")
    public Map<String, Object> findOneTwoDatas() {
        List<TieLuJuEntity> datasList = ls.findOneTwoDatas();
        return ResponseDataUtil.ok("查询铁路局和电务段数据成功", datasList);
    }

    //根据电务段的id 查询对应的线段和车站
    @GetMapping("/findXDAndCZByDWDId/{dwdid}")
    public Map<String, Object> findXDAndCZByDWDId(@PathVariable Integer dwdid) {
        List<DianWuDuanEntity> dianWuDuanEntityList = ls.findXDAndCZByDWDId(dwdid);
        return ResponseDataUtil.ok("查询电务段下面的数据成功", dianWuDuanEntityList);
    }


    //根据电务段did 查询此电务段下的电务段配置json数据和车站信息
    @GetMapping("/findDWDJsonAndStationInfoByDid/{did}")
    public Map<String, Object> findDWDJsonAndChezhanInfoByDid(@PathVariable Integer did) {
        DianWuDuanEntity dianWuDuanEntity = dataStatsService.findDWDJsonAndChezhanInfoByDid(did);
        return ResponseDataUtil.ok("查询电务段json数据和车站信息成功", dianWuDuanEntity);
    }

    //根据线段xid 查询此线段下的线段配置json数据和车站信息
    @GetMapping("/findXDJsonByXid/{xid}")
    public Map<String, Object> findXDJsonByXid(@PathVariable Integer xid) {
        XianDuanEntity xianDuanJson = dataStatsService.findXDJsonByXid(xid);
        return ResponseDataUtil.ok("查询线段的json数据成功", xianDuanJson);
    }

    //根据车站cid 查询此车站下的区段配置json数据
    @GetMapping("/findQDJsonByCid/{cid}")
    public Map<String, Object> findQDJsonByCid(@PathVariable Integer cid) {
        CheZhanEntity qdJson = dataStatsService.findQDJsonAndQuDuanDatasByCid(cid);
        return ResponseDataUtil.ok("查询区段的json数据成功", qdJson);
    }

}
