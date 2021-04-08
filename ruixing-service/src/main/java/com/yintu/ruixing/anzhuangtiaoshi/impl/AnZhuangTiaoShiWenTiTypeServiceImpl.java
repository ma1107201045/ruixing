package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiTypeEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiTypeService;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiWenTiTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/6 15:15
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class AnZhuangTiaoShiWenTiTypeServiceImpl implements AnZhuangTiaoShiWenTiTypeService {
    @Autowired
    private AnZhuangTiaoShiWenTiTypeDao anZhuangTiaoShiWenTiTypeDao;

    @Override
    public List<AnZhuangTiaoShiWenTiEntity> findAllType(Integer typeId) {
        return anZhuangTiaoShiWenTiTypeDao.findAllType(typeId);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        for (Integer id : ids) {
            anZhuangTiaoShiWenTiTypeDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<AnZhuangTiaoShiWenTiEntity> findWenTiType(String wenTiName, Integer typeId) {
        return anZhuangTiaoShiWenTiTypeDao.findWenTiType(wenTiName,typeId);
    }

    @Override
    public void editById(AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity) {
        anZhuangTiaoShiWenTiTypeDao.updateByPrimaryKeySelective(anZhuangTiaoShiWenTiTypeEntity);
    }

    @Override
    public void addWenTiType(AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity) {
        anZhuangTiaoShiWenTiTypeDao.insertSelective(anZhuangTiaoShiWenTiTypeEntity);
    }
}
