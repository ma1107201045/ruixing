package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-06-06 19
 * 站内相关
 */
@RestController
@RequestMapping("/ZhanNei")
public class ZhanNeiController {

    @Autowired
    private ZhanNeiService zhanNeiService;

    //根据车站id 查询车站内电码化的信息
    @GetMapping("/findAllDianMaHua/{id}")
    public Map<String, Object> findAllDianMaHua(@PathVariable Long id) {
        List<QuDuanBaseEntity> list = new ArrayList<>();
        List<QuDuanBaseEntity> quDuanBaseEntities = zhanNeiService.findAllDianMaHua(id);
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
            if (quDuanBaseEntity.getDianmahuaguihao() != null && quDuanBaseEntity.getDianmahuaguihao() != "") {
                list.add(quDuanBaseEntity);
            }
        }
        return ResponseDataUtil.ok("查询电码化数据成功", list);
    }


    //网络连接
    @GetMapping("/findAllWangLuoLianJie")
    public Map<String, Object> findAllWangLuoLianJie(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CheZhanEntity> all = zhanNeiService.findTieLuJuById(page, size);
        PageInfo<CheZhanEntity> pageInfo = new PageInfo<>(all);
        return ResponseDataUtil.ok("查询车站信息成功", pageInfo);
    }

    //根据id更改车站的各个状态
    @PutMapping("/editWangLuoLianJieById/{cid}")
    public Map<String, Object> editWangLuoLianJieById(@PathVariable Long cid, CheZhanEntity cheZhanEntity) {
        zhanNeiService.editWangLuoLianJieById(cheZhanEntity);
        return ResponseDataUtil.ok("修改信息成功");
    }


    //根据车站专用id  和时间查询对应的区段数据
    @GetMapping("/findDianMaHuaDatasByCZid/{czid}")
    public Map<String, Object> findDianMaHuaDatasByCZid(@PathVariable Integer czid,
                                                        @RequestParam(value = "startTime", required = false) Date startTime,
                                                        @RequestParam(value = "endTime", required = false) Date endTime) {
        if (startTime != null && endTime != null) {
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2List = zhanNeiService.findDianMaHuaDatasByCZid(czid, startTime, endTime);
            List<JSONObject> js = new ArrayList<>();
            for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2List) {
                JSONObject o = (JSONObject) JSONObject.toJSON(quDuanInfoEntityV2);
                String fbjCollectionBei = o.getString("fbjCollectionBei");
                if ("1".equals(fbjCollectionBei)) {
                    o.put("fbjCollectionBei", "吸起");
                }
                if ("1".equals(fbjCollectionBei)) {
                    o.put("fbjCollectionBei", "落下");
                } else {
                    o.put("fbjCollectionBei", "无效");
                }
                BigDecimal fbjCollectionZhu = o.getBigDecimal("fbjCollectionZhu");
                if ("1".equals(fbjCollectionZhu)) {
                    o.put("fbjCollectionZhu", "吸起");
                }
                if ("2".equals(fbjCollectionZhu)) {
                    o.put("fbjCollectionZhu", "落下");
                } else {
                    o.put("fbjCollectionZhu", "无效");
                }
                Integer fbjDriveBei = o.getInteger("fbjDriveBei");
                if (fbjDriveBei == 1) {
                    o.put("fbjDriveBei", "正常");
                }
                if (fbjDriveBei == 2) {
                    o.put("fbjDriveBei", "无");
                } else {
                    o.put("fbjDriveBei", "无效");
                }
                Integer fbjDriveZhu = o.getInteger("fbjDriveZhu");
                if (fbjDriveZhu == 1) {
                    o.put("fbjDriveZhu", "正常");
                }
                if (fbjDriveZhu == 2) {
                    o.put("fbjDriveZhu", "无");
                } else {
                    o.put("fbjDriveZhu", "无效");
                }
                js.add(o);
            }
            return ResponseDataUtil.ok("查询电码化数据成功", js);
        } else {
            String tableName = StringUtil.getTableName(czid, new Date());
            List<JSONObject> quDuanInfoEntityV2List = zhanNeiService.findDianMaHuaDatasByCZids(czid, tableName);
            return ResponseDataUtil.ok("查询电码化数据成功", quDuanInfoEntityV2List);
        }

    }
}