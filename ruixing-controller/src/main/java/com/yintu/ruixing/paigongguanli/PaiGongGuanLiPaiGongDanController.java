package com.yintu.ruixing.paigongguanli;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:31
 * @Version 1.0
 * 需求:  派工单
 */
@RestController
@RequestMapping("/PaiGongDanAll")
public class PaiGongGuanLiPaiGongDanController {
    @Autowired
    private PaiGongGuanLiPaiGongDanService paiGongGuanLiPaiGongDanService;

    //自动创建派工单编号
    @GetMapping("/findPaiGongDanNum")
    public Map<String, Object> findPaiGongDanNum(String suoxie) {
        Date today = new Date();
        int month = DateUtil.month(today) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        int year = DateUtil.year(today);
        String paigongdannum = paiGongGuanLiPaiGongDanService.findPaiGongDanNum(suoxie);
        if (paigongdannum == null) {
            String paiGongDanNum = suoxie + year + monthStr + "0001";
            return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
        } else {
            String substring = paigongdannum.substring(8,12);
            Integer i = Integer.parseInt(substring) + 1;
            String paiGongDanNum = suoxie + year + monthStr + i.toString();
            return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
        }
    }


    //新增派工单
    @PostMapping("/addPaiGongDan")
    public Map<String,Object>addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity){
        paiGongGuanLiPaiGongDanService.addPaiGongDan(paiGongGuanLiPaiGongDanEntity);
        return ResponseDataUtil.ok("新增派工单成功");
    }

    //根据派工单编号  查询关联单据
    @GetMapping("/findOnePaiGongDanByNum")
    public Map<String,Object>findOnePaiGongDanByNum(String PaiGongDanNum){
        List<PaiGongGuanLiPaiGongDanEntity>paiGongDanEntityList=paiGongGuanLiPaiGongDanService.findOnePaiGongDanByNum(PaiGongDanNum);
        return ResponseDataUtil.ok("查询管理单据成功",paiGongDanEntityList);
    }
























    public static void main(String[] args) {
        Date today = new Date();
        int month = DateUtil.month(today) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        int year = DateUtil.year(today);
        System.out.println(month);
        System.out.println(year);
        String num = "sl 2020 08 0001";
        String substring1 = num.substring(8, 12);
        String substring = num.substring(8);
        System.out.println(substring);
        System.out.println(substring1);
    }
}
