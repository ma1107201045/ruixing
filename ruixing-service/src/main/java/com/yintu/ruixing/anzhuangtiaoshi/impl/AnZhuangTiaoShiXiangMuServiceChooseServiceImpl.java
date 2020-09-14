package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceChooseDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceChooseEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceChooseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllByXDid(Integer xdid, Integer page, Integer size) {
        return anZhuangTiaoShiXiangMuServiceChooseDao.findAllByXDid(xdid);
    }
}
