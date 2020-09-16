package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:36
 * @Version 1.0
 * 需求:安装调试 项目
 */
@Service
@Transactional
public class AnZhuangTiaoShiXiangMuServiceChooseServiceImpl implements AnZhuangTiaoShiXiangMuServiceChooseService {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseDao anZhuangTiaoShiXiangMuServiceChooseDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuDao anZhuangTiaoShiXiangMuDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusChooseDao anZhuangTiaoShiXiangMuServiceStatusChooseDao;

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findAllByXDid(Integer xdid, Integer page, Integer size) {
        AnZhuangTiaoShiXiangMuEntity xiangMuEntityList = anZhuangTiaoShiXiangMuDao.findXiangMuById(xdid);
        Integer id = xiangMuEntityList.getId();
        Integer worksid = xiangMuEntityList.getWorksid();
        Date xianduantime = xiangMuEntityList.getXianduantime();
        String tljName = xiangMuEntityList.getTljName();
        String dwdName = xiangMuEntityList.getDwdName();
        String xdName = xiangMuEntityList.getXdName();
        String xdType = xiangMuEntityList.getXdType();
        Integer xdFenlei = xiangMuEntityList.getXdFenlei();
        String guanlianxiangmu = xiangMuEntityList.getGuanlianxiangmu();

        List<AnZhuangTiaoShiXiangMuEntity> aztsXiangMuServiceChooseEntityList = new ArrayList<>();
        List<Integer> CZid = anZhuangTiaoShiXiangMuServiceChooseDao.findCZidByXDid(xdid);
        for (int i = 0; i < CZid.size(); i++) {
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> titleList = new ArrayList<>();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> chooseList = new ArrayList<>();
            AnZhuangTiaoShiXiangMuEntity xiangMuEntity=new AnZhuangTiaoShiXiangMuEntity();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityList = anZhuangTiaoShiXiangMuServiceChooseDao.findAllByCZid(CZid.get(i));
            for (AnZhuangTiaoShiXiangMuServiceChooseEntity XiangMuServiceChooseEntity : xiangMuServiceChooseEntityList) {

                if (XiangMuServiceChooseEntity.getChoid() == null) {
                    titleList.add(XiangMuServiceChooseEntity);
                    xiangMuEntity.setId(id);
                    xiangMuEntity.setWorksid(worksid);
                    xiangMuEntity.setXianduantime(xianduantime);
                    xiangMuEntity.setTljName(tljName);
                    xiangMuEntity.setDwdName(dwdName);
                    xiangMuEntity.setXdName(xdName);
                    xiangMuEntity.setXdType(xdType);
                    xiangMuEntity.setXdFenlei(xdFenlei);
                    xiangMuEntity.setGuanlianxiangmu(guanlianxiangmu);
                    if (XiangMuServiceChooseEntity.getPlanStartTime()!=null){
                        Date planStartTime = XiangMuServiceChooseEntity.getPlanStartTime();
                        Date planEndTime = XiangMuServiceChooseEntity.getPlanEndTime();
                        int PlanToalTime =(int)((planEndTime.getTime()-planStartTime.getTime())/(1000*3600*24))+1;
                        int PlanOneTime = (int) ((new Date().getTime() - planStartTime.getTime()) / (1000*3600*24))+1;
                        XiangMuServiceChooseEntity.setPlanToalTime(PlanToalTime);
                        XiangMuServiceChooseEntity.setPlanOneTime(PlanOneTime);
                    }
                    xiangMuEntity.setTitlelist(titleList);

                } else {
                    Integer choid = XiangMuServiceChooseEntity.getChoid();
                    String chroosname=anZhuangTiaoShiXiangMuServiceStatusChooseDao.findNameBysid(choid);
                    XiangMuServiceChooseEntity.setChroosname(chroosname);
                    chooseList.add(XiangMuServiceChooseEntity);
                    xiangMuEntity.setId(id);
                    xiangMuEntity.setWorksid(worksid);
                    xiangMuEntity.setXianduantime(xianduantime);
                    xiangMuEntity.setTljName(tljName);
                    xiangMuEntity.setDwdName(dwdName);
                    xiangMuEntity.setXdName(xdName);
                    xiangMuEntity.setXdType(xdType);
                    xiangMuEntity.setXdFenlei(xdFenlei);
                    xiangMuEntity.setGuanlianxiangmu(guanlianxiangmu);
                    xiangMuEntity.setChooselist(chooseList);
                }
            }
            aztsXiangMuServiceChooseEntityList.add(i,xiangMuEntity);
           // PageHelper.startPage(page,size);
            //PageInfo<AnZhuangTiaoShiXiangMuEntity> shiXiangMuEntityPage=new PageInfo<>(aztsXiangMuServiceChooseEntityList);
        }
        return aztsXiangMuServiceChooseEntityList;
    }





}
