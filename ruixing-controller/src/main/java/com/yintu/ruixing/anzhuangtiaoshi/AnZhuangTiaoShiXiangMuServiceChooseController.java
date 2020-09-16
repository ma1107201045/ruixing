package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:35
 * @Version 1.0
 * 需求:安装调试 项目
 */
@RestController
@RequestMapping("/XiangMuServiceChooseAll")
public class AnZhuangTiaoShiXiangMuServiceChooseController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseService anZhuangTiaoShiXiangMuServiceChooseService;


    //根据线段id  查询对应的车站数据
    @GetMapping("/findAllByXDid/{xdid}")
    public Map<String, Object> findAllByXDid(@PathVariable Integer xdid) {
        // PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuServiceChooseEntityList = anZhuangTiaoShiXiangMuServiceChooseService.findAllByXDid(xdid);
        // PageInfo<AnZhuangTiaoShiXiangMuEntity> xiangMuServiceChooseEntityPageInfo=new PageInfo<>(xiangMuServiceChooseEntityList);
        return ResponseDataUtil.ok("查询车站数据成功", xiangMuServiceChooseEntityList);
    }

    //新增车站数据
    @PostMapping("/addXiangMuServiceChoose")
    public Map<String, Object> addXiangMuServiceChoose(@RequestBody JSONArray cheZhanDatas) {
       /* String aa="[{\"addForm\":{\"statusdata\":[{\"id\":5,\"servicename\":\"是否完成静态验收\",\"choose\":\"\",\"timetype\":2,\"isNotOver\":0,\"checkbox\":true,\"planStartTimes\":\"2020-09-07T16:00:00.000Z\",\"planStartTime\":\"2020/9/8\",\"planEndTimes\":\"2020-09-15T16:00:00.000Z\",\"planEndTime\":\"2020/9/16\"},{\"id\":8,\"servicename\":\"是否完成配线\",\"choose\":\"\",\"timetype\":1,\"isNotOver\":0,\"checkbox\":true},{\"id\":9,\"servicename\":\"设备是否到货\",\"choose\":\"\",\"checkbox\":true,\"list\":[{\"id\":6,\"sid\":9,\"name\":\"机柜\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":7,\"sid\":9,\"name\":\"室外设备\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":8,\"sid\":9,\"name\":\"室内板卡\",\"isNotDaoHuo\":0,\"isNotChoose\":true}]},{\"id\":12,\"servicename\":\"是否开通\",\"choose\":\"\",\"timetype\":3,\"checkbox\":true,\"planOpenTimes\":\"2020-09-15T16:00:00.000Z\",\"planOpenTime\":\"2020/9/16\"}],\"xianduantime\":2020,\"tljName\":{\"value\":81,\"label\":\"哈尔滨局\"},\"dwdName\":{\"value\":52,\"label\":\"哈尔滨电务段\"},\"xdName\":{\"value\":165,\"label\":\"哈牡客专线\"},\"chezhanname\":{\"value\":98,\"label\":\"太平桥哈牡场\"},\"xdType\":\"2000R继电编码N+1区间轨道电路\",\"xdFenlei\":\"1\",\"worksid\":4,\"guanlianxiangmu\":\"111 / 111\"}},{\"addForm\":{\"statusdata\":[{\"id\":5,\"servicename\":\"是否完成静态验收\",\"choose\":\"\",\"timetype\":2,\"isNotOver\":0,\"checkbox\":true,\"planStartTimes\":\"2020-09-07T16:00:00.000Z\",\"planStartTime\":\"2020/9/8\",\"planEndTimes\":\"2020-09-15T16:00:00.000Z\",\"planEndTime\":\"2020/9/16\"},{\"id\":8,\"servicename\":\"是否完成配线\",\"choose\":\"\",\"timetype\":1,\"isNotOver\":0,\"checkbox\":true},{\"id\":9,\"servicename\":\"设备是否到货\",\"choose\":\"\",\"checkbox\":true,\"list\":[{\"id\":6,\"sid\":9,\"name\":\"机柜\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":7,\"sid\":9,\"name\":\"室外设备\",\"isNotDaoHuo\":0,\"isNotChoose\":true},{\"id\":8,\"sid\":9,\"name\":\"室内板卡\",\"isNotDaoHuo\":0,\"isNotChoose\":true}]},{\"id\":12,\"servicename\":\"是否开通\",\"choose\":\"\",\"timetype\":3,\"checkbox\":true,\"planOpenTimes\":\"2020-09-15T16:00:00.000Z\",\"planOpenTime\":\"2020/9/16\"}],\"xianduantime\":2020,\"tljName\":{\"value\":81,\"label\":\"哈尔滨局\"},\"dwdName\":{\"value\":52,\"label\":\"哈尔滨电务段\"},\"xdName\":{\"value\":165,\"label\":\"哈牡客专线\"},\"chezhanname\":{\"value\":98,\"label\":\"太平桥哈牡场\"},\"xdType\":\"2000R继电编码N+1区间轨道电路\",\"xdFenlei\":\"1\",\"worksid\":4,\"guanlianxiangmu\":\"111 / 111\"}}]";
         cheZhanDatas=(JSONArray)(JSONArray.parse(aa));*/
        /*JSONArray o1 =(JSONArray) JSONArray.toJSON(cheZhanDatas);
        System.out.println("dfadf"+o1);*/
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        Date today = new Date();
        for (Object chezhandata : cheZhanDatas) {
            Map<String, Object> chezhandat = (Map) chezhandata;
            Map<String, Object> label = (Map) chezhandat.get("addForm");
            Map<String, Object> tljName = (Map) label.get("tljName");
            Map<String, Object> dwdName = (Map) label.get("dwdName");
            Map<String, Object> xdName = (Map) label.get("xdName");
            Map<String, Object> czName = (Map) label.get("chezhanname");
            List statusdata = (List) label.get("statusdata");


            String tljname = (String) tljName.get("label");
            String dwdname = (String) dwdName.get("label");
            String xdname = (String) xdName.get("label");
            Integer xdid = (Integer) xdName.get("value");
            String czname = (String) czName.get("label");
            Integer czid = (Integer) czName.get("value");
            Integer xianduanTime = (Integer) label.get("xianduantime");
            Integer worksid = (Integer) label.get("worksid");
            String xdFenlei = (String) label.get("xdFenlei");
            String guanlianxiangmu = (String) label.get("guanlianxiangmu");
            String xdType = (String) label.get("xdType");

            AnZhuangTiaoShiXiangMuEntity xiangMuEntity = new AnZhuangTiaoShiXiangMuEntity();
            xiangMuEntity.setTljName(tljname);
            xiangMuEntity.setDwdName(dwdname);
            xiangMuEntity.setXdName(xdname);
            //xiangMuEntity.setXianduantime((Date) xianduanTime);
            xiangMuEntity.setXdFenlei(Integer.parseInt(xdFenlei));
            xiangMuEntity.setWorksid(worksid);
            xiangMuEntity.setXdType(xdType);
            xiangMuEntity.setGuanlianxiangmu(guanlianxiangmu);
            xiangMuEntity.setCreatename(username);
            xiangMuEntity.setCreatetime(today);
            anZhuangTiaoShiXiangMuServiceChooseService.addXiangMu(xiangMuEntity);
            Integer xmid = xiangMuEntity.getId();

            for (Object statusdatum : statusdata) {
                Map<String, Object> statusdatu = (Map) statusdatum;
                Integer titleid = (Integer) statusdatu.get("id");
                String servicename = (String) statusdatu.get("servicename");
                AnZhuangTiaoShiXiangMuServiceStatusEntity serviceStatusEntity = anZhuangTiaoShiXiangMuServiceChooseService.findServiceStatusById(titleid);
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 2) {
                    Integer timetype = (Integer) statusdatu.get("timetype");
                    Integer planStartTime = (Integer) statusdatu.get("planStartTime");
                    Integer planEndTime = (Integer) statusdatu.get("planEndTime");

                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 1) {
                    Integer timetype = (Integer) statusdatu.get("timetype");


                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 3) {
                    Integer timetype = (Integer) statusdatu.get("timetype");
                    Integer planOpenTime = (Integer) statusdatu.get("planOpenTime");

                }
                if (!serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == null) {
                    List list = (List) statusdatu.get("list");
                    for (Object chroose : list) {
                        Map<String, Object> chroosee = (Map) chroose;
                        Integer chrooseid = (Integer) chroosee.get("id");
                        Integer isNotDaoHuo = (Integer) chroosee.get("isNotDaoHuo");
                    }
                }
                System.out.println(servicename);
            }

            System.out.println(tljname);
            System.out.println(dwdname);
            System.out.println(xdname);
            System.out.println(statusdata);


        }

        return ResponseDataUtil.ok("新增成功");
    }

}
