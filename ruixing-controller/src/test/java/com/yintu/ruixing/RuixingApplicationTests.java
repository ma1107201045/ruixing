package com.yintu.ruixing;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.yintu.ruixing.danganguanli.CustomerService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoTypesPropertyService;
import com.yintu.ruixing.guzhangzhenduan.SkylightTimeService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileService;
import com.yintu.ruixing.jiejuefangan.SolutionService;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import com.yintu.ruixing.xitongguanli.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.AntPathMatcher;

import java.util.*;
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
    private CustomerService customerService;

    @Autowired
    private SkylightTimeService skylightTimeService;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private DatabaseOperatingRecordService databaseOperatingRecordService;

    @Value("${spring.datasource.druid.master.url}")
    private String jdbcUrl;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Autowired
    private PreSaleFileService preSaleFileService;

    @Test
    void contextLoads() {
        List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>();
        PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
        preSaleFileAuditorEntity.setPreSaleFileId(0);
        preSaleFileAuditorEntity.setAuditorId(0);
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
        System.out.println(skylightTimeService.findByCondition(null, null, null, null, null));

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
        System.out.println(customerService.findByExample(null, null, null, null));
    }

    @Test
    void contextLoads10() {
        String startTime = "2020-10-08 08:08:00";
        String endTime = "2020-12-07 08:08:00";
        System.out.println(DateUtil.offsetMonth(DateUtil.endOfMonth(DateUtil.parseDate(startTime)), 1));
    }

    @Test
    void contextLoads11() {
//        System.out.println(StrUtil.);
//        System.out.println(StringUtil.getAssemblyId(11,new Date(),12));
        databaseOperatingRecordService.findLikeTableNames("db_dev_ruixing", "alarm%", "data%");
    }

    @Test
    void contextLoads12() {
        System.out.println(solutionService.findProjectSum());
    }

    @Test
    void contextLoads13() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("/abc/**", "/abc"));
    }

    @Test
    void contextLoads14() {
        long s1 = System.currentTimeMillis();
        permissionService.findPermissionAndRole();
        System.out.println((System.currentTimeMillis() - s1) / 1000.0);


        long s2 = System.currentTimeMillis();
        List<PermissionEntity> permissionEntities = permissionService.findByExample(new PermissionEntityExample());
        for (PermissionEntity permissionEntity : permissionEntities) {
            List<RoleEntity> roleEntities = roleService.findByPermissionId(permissionEntity.getId());
            permissionEntity.setRoleEntities(roleEntities);
        }
        System.out.println((System.currentTimeMillis() - s2) / 1000.0);
    }

    @Test
    void contextLoads15() {
        System.out.println(quDuanInfoDaoV2.selectByQidAndNV(1, "v3", "110.0", "data_applydata_11_20201212"));
    }

    @Test
    void contextLoads16() {
        System.out.println(preSaleFileService.findByExample(null, null, null, null, null, null));
    }
}