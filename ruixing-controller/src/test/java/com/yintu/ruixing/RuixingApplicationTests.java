package com.yintu.ruixing;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoTypesPropertyService;
import com.yintu.ruixing.guzhangzhenduan.SkylightTimeService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorService;
import com.yintu.ruixing.jiejuefangan.SolutionService;
import com.yintu.ruixing.xitongguanli.RoleEntity;
import com.yintu.ruixing.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.management.relation.Role;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RuixingApplicationTests {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PreSaleFileAuditorService preSaleFileAuditorService;
    @Autowired
    private QuDuanInfoTypesPropertyService quDuanInfoTypesPropertyService;
    @Autowired
    private QuDuanInfoService quDuanInfoService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SkylightTimeService skylightTimeService;

    @Autowired
    private SolutionService solutionService;


    @Test
    void contextLoads() {
        List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>();
        PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
        preSaleFileAuditorEntity.setPreSaleFileId(0);
        preSaleFileAuditorEntity.setAuditorId(0);
        preSaleFileAuditorEntity.setIsPass((short) 0);
        preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
        preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
    }

    @Test
    void contextLoads1() {
//        System.out.println(quDuanInfoTypesPropertyService.connectFindByCondition("1,3"));
//        System.out.println();
////        System.out.println(cheZhanService.findByczId(11));
        String types = quDuanInfoTypesPropertyService.countByType(Arrays.asList(1, 2));
        System.out.println(types);
    }

    @Test
    void contextLoads2() {
        System.out.println(quDuanInfoService.findPropertiesTree(11));
    }

    @Test
    void contextLoads3() {
        int month = DateUtil.thisMonth() + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        String tableName = "data_applydata_" + 11 + "_" + DateUtil.thisYear() + monthStr;
        System.out.println(tableName);

    }

    @Test
    void contextLoads4() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUsername("马龙飞");
        UserEntity userEntity2 = new UserEntity();
        BeanUtils.copyProperties(userEntity1, userEntity2);
        System.out.println(userEntity2);

    }

    @Test
    void contextLoads5() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUsername("马龙飞");
        UserEntity userEntity2 = new UserEntity();
        BeanUtils.copyProperties(userEntity1, userEntity2);
        System.out.println(userEntity2);
    }


    @Test
    void contextLoads6() {
        System.out.println(skylightTimeService.findByCondition(null, null, null, null));

    }

    @Test
    void contextLoads7() {
        QrCodeUtil.generate("https://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode.jpg"));

    }


    @Test
    void contextLoads8() {
        System.out.println(DateUtil.year(new Date()));
    }

    @Test
    void contextLoads9() {
        String obj = "[{\"addForm\":{\"statusdata\":[{\"id\":5,\"servicename\":\"是否完成静态验收\",\"choose\":\"\",\"timetype\":2,\"isNotOver\":0,\"checkbox\":true,\"planStartTimes\":\"2020-09-07T16:00:00.000Z\",\"planStartTime\":\"2020/9/8\",\"planEndTimes\":\"2020-09-15T16:00:00.000Z\",\"planEndTime\":\"2020/9/16\"},{\"id\":8,\"servicename\":\"是否完成配线\",\"choose\":\"\",\"timetype\":1,\"isNotOver\":0,\"checkbox\":true},{\"id\":9,\"servicename\":\"设备是否到货\",\"choose\":\"\",\"checkbox\":true,\"list\":[{\"id\":6,\"sid\":9,\"name\":\"机柜\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":7,\"sid\":9,\"name\":\"室外设备\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":8,\"sid\":9,\"name\":\"室内板卡\",\"isNotDaoHuo\":0,\"isNotChoose\":true}]},{\"id\":12,\"servicename\":\"是否开通\",\"choose\":\"\",\"timetype\":3,\"checkbox\":true,\"planOpenTimes\":\"2020-09-15T16:00:00.000Z\",\"planOpenTime\":\"2020/9/16\"}],\"xianduantime\":2020,\"tljName\":{\"value\":81,\"label\":\"哈尔滨局\"},\"dwdName\":{\"value\":52,\"label\":\"哈尔滨电务段\"},\"xdName\":{\"value\":165,\"label\":\"哈牡客专线\"},\"chezhanname\":{\"value\":98,\"label\":\"太平桥哈牡场\"},\"xdType\":\"2000R继电编码N+1区间轨道电路\",\"xdFenlei\":\"1\",\"worksid\":4,\"guanlianxiangmu\":\"111 / 111\"}},{\"addForm\":{\"statusdata\":[{\"id\":5,\"servicename\":\"是否完成静态验收\",\"choose\":\"\",\"timetype\":2,\"isNotOver\":0,\"checkbox\":true,\"planStartTimes\":\"2020-09-07T16:00:00.000Z\",\"planStartTime\":\"2020/9/8\",\"planEndTimes\":\"2020-09-15T16:00:00.000Z\",\"planEndTime\":\"2020/9/16\"},{\"id\":8,\"servicename\":\"是否完成配线\",\"choose\":\"\",\"timetype\":1,\"isNotOver\":0,\"checkbox\":true},{\"id\":9,\"servicename\":\"设备是否到货\",\"choose\":\"\",\"checkbox\":true,\"list\":[{\"id\":6,\"sid\":9,\"name\":\"机柜\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":7,\"sid\":9,\"name\":\"室外设备\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":8,\"sid\":9,\"name\":\"室内板卡\",\"isNotDaoHuo\":0,\"isNotChoose\":true}]},{\"id\":12,\"servicename\":\"是否开通\",\"choose\":\"\",\"timetype\":3,\"checkbox\":true,\"planOpenTimes\":\"2020-09-15T16:00:00.000Z\",\"planOpenTime\":\"2020/9/16\"}],\"xianduantime\":2020,\"tljName\":{\"value\":81,\"label\":\"哈尔滨局\"},\"dwdName\":{\"value\":52,\"label\":\"哈尔滨电务段\"},\"xdName\":{\"value\":165,\"label\":\"哈牡客专线\"},\"chezhanname\":{\"value\":98,\"label\":\"太平桥哈牡场\"},\"xdType\":\"2000R继电编码N+1区间轨道电路\",\"xdFenlei\":\"1\",\"worksid\":4,\"guanlianxiangmu\":\"111 / 111\"}}]";
        JSONArray ja = (JSONArray) JSONArray.parse(obj);
        for (Object o : ja) {
            System.out.println(o.getClass().getName());
        }

    }

}