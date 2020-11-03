package com.yintu.ruixing.common;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuService;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuService;
import com.yintu.ruixing.common.enumobject.EnumAuthType;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.jiejuefangan.SolutionService;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanService;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/11/3 13:58
 */
@RestController
@RequestMapping("/indexs")
public class IndexController {
    @Autowired
    private SolutionService solutionService;

    @Autowired
    private ChanPinJiaoFuXiangMuService chanPinJiaoFuXiangMuService;

    @Autowired
    private AnZhuangTiaoShiXiangMuService anZhuangTiaoShiXiangMuService;

    @Autowired
    private UserService userService;
    @Autowired
    private RemoteSupportAlarmService remoteSupportAlarmService;

    @Autowired
    private PaiGongGuanLiPaiGongDanService paiGongGuanLiPaiGongDanService;

    @Autowired
    private CheZhanService cheZhanService;

    /**
     * 统计项目总和
     *
     * @return
     */
    @GetMapping("/project/statistics")
    public Map<String, Object> getProjectSum() {
        List<Object[]> list = new ArrayList<>();
        Object[] projectTypes = new Object[3];
        Object[] projectSums = new Object[3];
        projectTypes[0] = "解决方案";
        projectSums[0] = solutionService.findProjectSum();
        projectTypes[1] = "产品交付";
        projectSums[1] = chanPinJiaoFuXiangMuService.findProjectSum();
        projectTypes[2] = "安装调试";
        projectSums[2] = anZhuangTiaoShiXiangMuService.findProjectSum();
        list.add(projectTypes);
        list.add(projectSums);
        return ResponseDataUtil.ok("获取项目总数统计成功", list);
    }

    @GetMapping("/quantity/statistics")
    public Map<String, Object> getQuantitySum() {
        List<Object[]> list = new ArrayList<>();
        Object[] userSum = new Object[]{userService.findUserSum()};
        Object[] alarmSum = new Object[]{remoteSupportAlarmService.findAlarmSum()};
        Object[] workOrderSum = new Object[]{paiGongGuanLiPaiGongDanService.findWorkOrderSum(), userSum[0]};
        Object[] stationConfigurationSum = new Object[]{cheZhanService.findStationConfigurationSum(), cheZhanService.findStationSum()};
        list.add(userSum);
        list.add(alarmSum);
        list.add(workOrderSum);
        list.add(stationConfigurationSum);
        return ResponseDataUtil.ok("获取数量统计成功", list);
    }
}
