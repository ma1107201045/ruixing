package com.yintu.ruixing.guzhangzhenduan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntityV2;
import com.yintu.ruixing.guzhangzhenduan.ZhanNeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        //System.out.println("111111" + quDuanBaseEntities);
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
            if (quDuanBaseEntity.getDianmahuaguihao() != null && quDuanBaseEntity.getDianmahuaguihao() != "") {
                list.add(quDuanBaseEntity);
            }
        }
        return ResponseDataUtil.ok("查询电码化数据成功", list);
    }

    //根据电码化的区段id查询出数据展示在列表
    @GetMapping("/findDianMaHuaDatabById/{id}")
    public Map<String, Object> findDianMaHuaDatabById(@PathVariable Integer id) {
        List<QuDuanInfoEntity> quDuanInfoEntities = zhanNeiService.findDianMaHuaDatabById(id);
        return ResponseDataUtil.ok("查询单单个数据成功", quDuanInfoEntities);
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
        System.out.println("1111" + cheZhanEntity);
        zhanNeiService.editWangLuoLianJieById(cheZhanEntity);
        System.out.println("22222" + cheZhanEntity);
        return ResponseDataUtil.ok("修改信息成功");
    }


    //根据车站专用id  和时间查询对应的区段数据
    @GetMapping("/findDianMaHuaDatasByCZid/{czid}")
    public Map<String, Object> findDianMaHuaDatasByCZid(@PathVariable Integer czid,
                                                        @RequestParam(value = "startTime", required = false) Date startTime,
                                                        @RequestParam(value = "endTime", required = false) Date endTime) {
        if (startTime != null && endTime != null) {
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2List = zhanNeiService.findDianMaHuaDatasByCZid(czid, startTime, endTime);
            return ResponseDataUtil.ok("查询电码化数据成功", quDuanInfoEntityV2List);
        } else {
            String tableName = StringUtil.getTableName(czid, new Date());
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2List = zhanNeiService.findDianMaHuaDatasByCZids(czid, tableName);
            return ResponseDataUtil.ok("查询电码化数据成功", quDuanInfoEntityV2List);
        }

    }
}


/*

 for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2List) {
         List<BigDecimal> datas1=new ArrayList<>();
        List<BigDecimal> datas2=new ArrayList<>();
        List<BigDecimal> datas3=new ArrayList<>();
        List<BigDecimal> datas4=new ArrayList<>();
        List<BigDecimal> datas5=new ArrayList<>();
        List<Integer> datas6=new ArrayList<>();
        List<String> datas7=new ArrayList<>();
        List<Integer> datas8=new ArrayList<>();
        Map map=new HashMap();
        k++;
        Integer qid1 = quDuanInfoEntityV2.getQid();
        System.out.println(qid1);
        BigDecimal vOutZhu = quDuanInfoEntityV2.getVOutZhu();
        BigDecimal vOutBei = quDuanInfoEntityV2.getVOutBei();
        datas1.add(vOutZhu);
        datas1.add(vOutBei);
        map.put("vOutZhu"+qid1,datas1);
        BigDecimal maOutZhu = quDuanInfoEntityV2.getMaOutZhu();
        BigDecimal maOutBei = quDuanInfoEntityV2.getMaOutBei();
        datas2.add(maOutZhu);
        datas2.add(maOutBei);
        map.put("maOutZhu"+qid1,datas2);
        BigDecimal hzDownZhu = quDuanInfoEntityV2.getHzDownZhu();
        BigDecimal hzDownBei = quDuanInfoEntityV2.getHzDownBei();
        datas3.add(hzDownZhu);
        datas3.add(hzDownBei);
        map.put("hzDownZhu"+qid1,datas3);
        BigDecimal hzUpZhu = quDuanInfoEntityV2.getHzUpZhu();
        BigDecimal hzUpBei = quDuanInfoEntityV2.getHzUpBei();
        datas4.add(hzUpZhu);
        datas4.add(hzUpBei);
        map.put("hzUpZhu"+qid1,datas4);
        BigDecimal hzInLowZhu = quDuanInfoEntityV2.getHzInLowZhu();
        BigDecimal hzLowBei = quDuanInfoEntityV2.getHzLowBei();
        datas5.add(hzInLowZhu);
        datas5.add(hzLowBei);
        map.put("hzInLowZhu"+qid1,datas5);
        Integer fbjDriveZhu = quDuanInfoEntityV2.getFbjDriveZhu();
        Integer fbjDriveBei = quDuanInfoEntityV2.getFbjDriveBei();
        datas6.add(fbjDriveZhu);
        datas6.add(fbjDriveBei);
        map.put("fbjDriveZhu"+qid1,datas6);
        String fbjCollectionZhu = quDuanInfoEntityV2.getFbjCollectionZhu();
        String fbjCollectionBei = quDuanInfoEntityV2.getFbjCollectionBei();
        datas7.add(fbjCollectionZhu);
        datas7.add(fbjCollectionBei);
        Integer qid = quDuanInfoEntityV2.getQid();
        Integer time1 = quDuanInfoEntityV2.getTime();
        datas8.add(qid);
        map.put("qdid"+qid1,datas8);
        map.put("time"+qid1,time1);
        map.put("fbjCollectionZhu"+qid1,datas7);
        mapp.put("dianmahua"+qid1,map);
        }*/
