package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    public Map<String, Object> addXiangMuServiceChoose(@RequestBody JSONArray cheZhanDatas) throws Exception {
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
            Long xianduanTime = (long) label.get("xianduantime");
            Integer worksid = (Integer) label.get("worksid");
            String xdFenlei = (String) label.get("xdFenlei");
            String guanlianxiangmu = (String) label.get("guanlianxiangmu");
            String xdType = (String) label.get("xdType");

            Date date = new Date(xianduanTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date xianduantime = sdf.parse(sdf.format(date));
            //新增项目
            AnZhuangTiaoShiXiangMuEntity xiangMuEntity = new AnZhuangTiaoShiXiangMuEntity();
            xiangMuEntity.setTljName(tljname);
            xiangMuEntity.setDwdName(dwdname);
            xiangMuEntity.setXdName(xdname);
            xiangMuEntity.setXianduantime(xianduantime);
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
                Integer titleid = (Integer) statusdatu.get("id");//服务状态标识id
                String servicename = (String) statusdatu.get("servicename");//服务状态标识名
                AnZhuangTiaoShiXiangMuServiceStatusEntity serviceStatusEntity = anZhuangTiaoShiXiangMuServiceChooseService.findServiceStatusById(titleid);
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 2) {//有状态标识  且有计划和实际开始结束时间
                    Integer timetype = (Integer) statusdatu.get("timetype");
                    Long planStartTime = (long) statusdatu.get("planStartTime");
                    Long planEndTime = (long) statusdatu.get("planEndTime");
                    Date datee = new Date(planStartTime);
                    Date dateee = new Date(planEndTime);
                    Date planStarttime = sdf.parse(sdf.format(datee));
                    Date planEndtime = sdf.parse(sdf.format(dateee));
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");

                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity=new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xmid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(2);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setPlanStartTime(planStarttime);
                    xiangMuServiceChooseEntity.setPlanEndTime(planEndtime);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 1) {//有状态标识  没有计划和实际开始结束时间
                    Integer timetype = (Integer) statusdatu.get("timetype");
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");
                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity=new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xmid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(1);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);

                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 3) {
                    Integer timetype = (Integer) statusdatu.get("timetype");
                    Long planOpenTime = (long) statusdatu.get("planOpenTime");
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");
                    Date datee = new Date(planOpenTime);
                    Date planOpentime = sdf.parse(sdf.format(datee));
                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity=new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xmid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(3);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setPlanOpenTime(planOpentime);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
                }
                if (!serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == null) {
                    List list = (List) statusdatu.get("list");
                    for (Object chroose : list) {
                        Map<String, Object> chroosee = (Map) chroose;
                        Integer chrooseid = (Integer) chroosee.get("id");
                        Integer isnot = (Integer) chroosee.get("isnot");
                        AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity=new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                        xiangMuServiceChooseEntity.setXdid(xmid);
                        xiangMuServiceChooseEntity.setSerid(titleid);
                        xiangMuServiceChooseEntity.setCzid(czid);
                        xiangMuServiceChooseEntity.setChezhanname(czname);
                        xiangMuServiceChooseEntity.setTypetime(null);
                        xiangMuServiceChooseEntity.setIsnot(isnot);
                        xiangMuServiceChooseEntity.setChoid(chrooseid);
                        xiangMuServiceChooseEntity.setCreatename(username);
                        xiangMuServiceChooseEntity.setCreatetime(today);
                        anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
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
