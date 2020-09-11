package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    public void editServiceStatusById(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username) {
        Date today=new Date();
        anZhuangTiaoShiXiangMuServiceStatusEntity.setUpdatename(username);
        anZhuangTiaoShiXiangMuServiceStatusEntity.setUpdatetime(today);
        anZhuangTiaoShiXiangMuServiceStatusDao.updateByPrimaryKeySelective(anZhuangTiaoShiXiangMuServiceStatusEntity);
    }

    @Override
    public void addServiceStatus(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username) {
        Date today=new Date();
        anZhuangTiaoShiXiangMuServiceStatusEntity.setCreatename(username);
        anZhuangTiaoShiXiangMuServiceStatusEntity.setCreatetime(today);
        anZhuangTiaoShiXiangMuServiceStatusDao.insertSelective(anZhuangTiaoShiXiangMuServiceStatusEntity);
    }
}
