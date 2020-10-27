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

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusDao anZhuangTiaoShiXiangMuServiceStatusDao;

    @Override
    public void addXiangMuServiceChooseEntity(AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity) {
        anZhuangTiaoShiXiangMuServiceChooseDao.insertSelective(xiangMuServiceChooseEntity);
    }

    @Override
    public void addXiangMu(AnZhuangTiaoShiXiangMuEntity xiangMuEntity) {
        anZhuangTiaoShiXiangMuDao.addSanJiShuXiangMu(xiangMuEntity);
    }

    @Override
    public AnZhuangTiaoShiXiangMuServiceStatusEntity findServiceStatusById(Integer id) {
        return anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(id);
    }

    @Override
    public JSONObject findAllByXDidddd(Integer xdid) {
        JSONObject js = new JSONObject();
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> serviceStatusEntityList = new ArrayList<>();
        int q = 0;
        List<AnZhuangTiaoShiXiangMuServiceChooseEntity> chooseEntityList = anZhuangTiaoShiXiangMuServiceChooseDao.findAllChoose();
        for (AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity : chooseEntityList) {
            q++;
            AnZhuangTiaoShiXiangMuServiceStatusEntity serviceStatusEntity = new AnZhuangTiaoShiXiangMuServiceStatusEntity();
            String servicename = xiangMuServiceChooseEntity.getServicename();
            serviceStatusEntity.setId(q);
            serviceStatusEntity.setServicename(servicename);
            serviceStatusEntityList.add(serviceStatusEntity);
        }
        js.put("title", serviceStatusEntityList);
        List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntity = new ArrayList<>();
        List<Integer> CZid = anZhuangTiaoShiXiangMuServiceChooseDao.findCZidByXDid(xdid);
        Integer a = 0;
        for (int i = 0; i < CZid.size(); i++) {
            a++;
            AnZhuangTiaoShiXiangMuServiceChooseEntity serviceChooseEntity = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> serviceChooseEntities = new ArrayList<>();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> serviceChooseEntitiess = new ArrayList<>();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityList = anZhuangTiaoShiXiangMuServiceChooseDao.findAllByCZid(CZid.get(i));
            JSONObject jss = new JSONObject();
            for (AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntityy : xiangMuServiceChooseEntityList) {
                AnZhuangTiaoShiXiangMuServiceChooseEntity serviceChooseEntitys = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                if (xiangMuServiceChooseEntityy.getChoid() != null) {
                    Integer serid = xiangMuServiceChooseEntityy.getSerid();
                    String servicename = xiangMuServiceChooseEntityy.getServicename();
                    Integer typetime = xiangMuServiceChooseEntityy.getTypetime();
                    Integer xdid1 = xiangMuServiceChooseEntityy.getXdid();
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseEntity = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findOneChooseBySidid(serid);
                    serviceChooseEntitys.setChooselist(chooseEntity);
                    serviceChooseEntitys.setServicename(servicename);
                    serviceChooseEntitys.setTypetime(typetime);
                    serviceChooseEntitys.setXdid(xdid1);
                    serviceChooseEntities.add(serviceChooseEntitys);
                    jss.put(a.toString(), serviceChooseEntities);
                    serviceChooseEntity.setList(jss);
                    xiangMuServiceChooseEntity.add(a - 1, serviceChooseEntity);
                    System.out.println("0000"+xiangMuServiceChooseEntity);
                }
                if (xiangMuServiceChooseEntityy.getChoid() == null) {
                    Integer serid = xiangMuServiceChooseEntityy.getSerid();
                    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> shiXiangMuServiceChooseEntity = anZhuangTiaoShiXiangMuServiceChooseDao.findServiceChoose(serid, xdid, CZid.get(i));
                    for (AnZhuangTiaoShiXiangMuServiceChooseEntity anZhuangTiaoShiXiangMuServiceChooseEntity : shiXiangMuServiceChooseEntity) {
                        if (anZhuangTiaoShiXiangMuServiceChooseEntity.getPlanStartTime() != null) {
                            Date planStartTime = anZhuangTiaoShiXiangMuServiceChooseEntity.getPlanStartTime();
                            Date planEndTime = anZhuangTiaoShiXiangMuServiceChooseEntity.getPlanEndTime();
                            int PlanToalTime = (int) ((planEndTime.getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                            int PlanOneTime = (int) ((new Date().getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                            anZhuangTiaoShiXiangMuServiceChooseEntity.setPlanToalTime(PlanToalTime);
                            anZhuangTiaoShiXiangMuServiceChooseEntity.setPlanOneTime(PlanOneTime);
                        }
                        serviceChooseEntitiess.add(anZhuangTiaoShiXiangMuServiceChooseEntity);
                        jss.put(a.toString(), serviceChooseEntitiess);
                        serviceChooseEntity.setList(jss);
                    }
                    xiangMuServiceChooseEntity.add(a - 1, serviceChooseEntity);
                    System.out.println("11111"+xiangMuServiceChooseEntity);

                }
            }
        }
        js.put("tableData", xiangMuServiceChooseEntity);
        return js;
    }


    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findAllByXDid(Integer xdid) {
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


            AnZhuangTiaoShiXiangMuEntity xiangMuEntity = new AnZhuangTiaoShiXiangMuEntity();

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
                    xiangMuEntity.setTitlelist(titleList);
                    if (XiangMuServiceChooseEntity.getPlanStartTime() != null) {
                        Date planStartTime = XiangMuServiceChooseEntity.getPlanStartTime();
                        Date planEndTime = XiangMuServiceChooseEntity.getPlanEndTime();
                        int PlanToalTime = (int) ((planEndTime.getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                        int PlanOneTime = (int) ((new Date().getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                        XiangMuServiceChooseEntity.setPlanToalTime(PlanToalTime);
                        XiangMuServiceChooseEntity.setPlanOneTime(PlanOneTime);
                    }
                }
                if (XiangMuServiceChooseEntity.getChoid() != null) {
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
                    xiangMuEntity.setTitlelist(titleList);

                    Integer serid = XiangMuServiceChooseEntity.getSerid();
                    Integer choid = XiangMuServiceChooseEntity.getChoid();
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseList = new ArrayList<>();
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseEntity = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findOneChooseBySidid(serid);
                    for (AnZhuangTiaoShiXiangMuServiceStatusChooseEntity statusChooseEntity : chooseEntity) {
                        chooseList.add(statusChooseEntity);
                        XiangMuServiceChooseEntity.setChooselist(chooseList);
                    }
                }
            }
            aztsXiangMuServiceChooseEntityList.add(i, xiangMuEntity);
            System.out.println("1111111111111111" + aztsXiangMuServiceChooseEntityList);
        }
        return aztsXiangMuServiceChooseEntityList;
    }
















  /*xiangMuEntity.setTitlelist(titleList);

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
                    xiangMuEntity.setChooselist(chooseList);*/


}
