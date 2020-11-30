package com.yintu.ruixing.anzhuangtiaoshi.impl;


import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusChooseDao;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/11 18:56
 * @Version 1.0
 * 需求: 安装调试  服务状态标识
 */
@Service
@Transactional
public class AnZhuangTiaoShiXiangMuServiceStatusServiceImpl implements AnZhuangTiaoShiXiangMuServiceStatusService {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusDao anZhuangTiaoShiXiangMuServiceStatusDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusChooseDao anZhuangTiaoShiXiangMuServiceStatusChooseDao;

    @Override
    public void deleteServiceStatusByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            AnZhuangTiaoShiXiangMuServiceStatusEntity serviceStatusEntityList = anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(ids[i]);
            if (!serviceStatusEntityList.getChoose().equals("是否")) {
                anZhuangTiaoShiXiangMuServiceStatusChooseDao.deleteBySid(serviceStatusEntityList.getId());
            }
            anZhuangTiaoShiXiangMuServiceStatusDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuServiceStatusEntity> findAllServiceStatus() {
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> serviceStatusEntityList = anZhuangTiaoShiXiangMuServiceStatusDao.findAllServiceStatus();
        for (AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity : serviceStatusEntityList) {
            anZhuangTiaoShiXiangMuServiceStatusEntity.setCheckbox(true);
            if (anZhuangTiaoShiXiangMuServiceStatusEntity.getChoose().equals("是否") && anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype() == 2) {
                anZhuangTiaoShiXiangMuServiceStatusEntity.setPlanStartTimes("");
                anZhuangTiaoShiXiangMuServiceStatusEntity.setPlanEndTimes("");
                anZhuangTiaoShiXiangMuServiceStatusEntity.setIsNotFinish(1);
            }
            if (anZhuangTiaoShiXiangMuServiceStatusEntity.getChoose().equals("是否") && anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype() == 3) {
                anZhuangTiaoShiXiangMuServiceStatusEntity.setPlanOpenTimes("");
                anZhuangTiaoShiXiangMuServiceStatusEntity.setIsNotFinish(1);
            }
            if (anZhuangTiaoShiXiangMuServiceStatusEntity.getChoose().equals("是否") && anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype() == 1) {
                anZhuangTiaoShiXiangMuServiceStatusEntity.setIsNotFinish(1);
            }
            anZhuangTiaoShiXiangMuServiceStatusEntity.setChoose("");

            Integer id = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
            List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseEntityList = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findAllBySid(id);
            List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseList = new ArrayList<>();
            if (chooseEntityList.size() != 0) {
                for (AnZhuangTiaoShiXiangMuServiceStatusChooseEntity chooseEntity : chooseEntityList) {
                    chooseEntity.setIsNotChoose(false);
                    chooseEntity.setIsnot(0);
                    chooseList.add(chooseEntity);
                }
                anZhuangTiaoShiXiangMuServiceStatusEntity.setList(chooseList);
            } else {
                anZhuangTiaoShiXiangMuServiceStatusEntity.setList(null);
            }
        }
        return serviceStatusEntityList;
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuServiceStatusEntity> findAllOrSomething(Integer page, Integer size, String serviceName) {
        return anZhuangTiaoShiXiangMuServiceStatusDao.findAllOrSomething(serviceName);
    }

    @Override
    public void editServiceStatusById(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username, Integer id) {
        Date today = new Date();
        anZhuangTiaoShiXiangMuServiceStatusEntity.setUpdatename(username);
        anZhuangTiaoShiXiangMuServiceStatusEntity.setUpdatetime(today);
        String choose = anZhuangTiaoShiXiangMuServiceStatusEntity.getChoose();
        if (!choose.equals("是否")) {
            anZhuangTiaoShiXiangMuServiceStatusEntity.setTimetype(1);
        }
        anZhuangTiaoShiXiangMuServiceStatusDao.updateByPrimaryKeySelective(anZhuangTiaoShiXiangMuServiceStatusEntity);
        Integer statusEntityId = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
        if (!choose.equals("是否")) {
            anZhuangTiaoShiXiangMuServiceStatusChooseDao.deleteBySid(id);
            for (String s : choose.split(",")) {
                AnZhuangTiaoShiXiangMuServiceStatusChooseEntity chooseEntity = new AnZhuangTiaoShiXiangMuServiceStatusChooseEntity();
                chooseEntity.setSid(statusEntityId);
                chooseEntity.setName(s);
                chooseEntity.setCreatetime(today);
                chooseEntity.setCreatename(username);
                anZhuangTiaoShiXiangMuServiceStatusChooseDao.insertSelective(chooseEntity);
            }
        }

    }

    @Override
    public void addServiceStatus(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username) {
        Date today = new Date();
        anZhuangTiaoShiXiangMuServiceStatusEntity.setCreatename(username);
        anZhuangTiaoShiXiangMuServiceStatusEntity.setCreatetime(today);
        anZhuangTiaoShiXiangMuServiceStatusDao.insertSelective(anZhuangTiaoShiXiangMuServiceStatusEntity);
        Integer statusEntityId = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
        String choose = anZhuangTiaoShiXiangMuServiceStatusEntity.getChoose();
        if (!choose.equals("是否")) {
            for (String s : choose.split(",")) {
                AnZhuangTiaoShiXiangMuServiceStatusChooseEntity chooseEntity = new AnZhuangTiaoShiXiangMuServiceStatusChooseEntity();
                chooseEntity.setSid(statusEntityId);
                chooseEntity.setName(s);
                chooseEntity.setCreatetime(today);
                chooseEntity.setCreatename(username);
                anZhuangTiaoShiXiangMuServiceStatusChooseDao.insertSelective(chooseEntity);
            }
        }
    }
}
