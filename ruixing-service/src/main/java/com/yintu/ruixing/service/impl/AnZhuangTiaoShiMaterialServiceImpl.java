package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.AnZhuangTiaoShiMaterialDao;
import com.yintu.ruixing.dao.AnZhuangTiaoShiMaterialOutInDao;
import com.yintu.ruixing.entity.AnZhuangTiaoShiMaterialEntity;
import com.yintu.ruixing.entity.AnZhuangTiaoShiMaterialOutInEntity;
import com.yintu.ruixing.service.AnZhuangTiaoShiMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/10 15:18
 * @Version 1.0
 * 需求: 安装调试 库存管理
 */
@Service
@Transactional
public class AnZhuangTiaoShiMaterialServiceImpl implements AnZhuangTiaoShiMaterialService {
    @Autowired
    private AnZhuangTiaoShiMaterialDao anZhuangTiaoShiMaterialDao;

    @Autowired
    private AnZhuangTiaoShiMaterialOutInDao anZhuangTiaoShiMaterialOutInDao;

    @Override
    public void editMaterial(AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity,Integer id) {
        anZhuangTiaoShiMaterialDao.editMaterial(anZhuangTiaoShiMaterialEntity.getTotalnumber(),id);
    }

    @Override
    public List<AnZhuangTiaoShiMaterialEntity> findAllMaterials() {
        return anZhuangTiaoShiMaterialDao.findAllMaterials();
    }

    @Override
    public List<AnZhuangTiaoShiMaterialOutInEntity> findAllInMaterial(Integer page, Integer size, String materialNumber) {
        return anZhuangTiaoShiMaterialOutInDao.findAllInMaterial(materialNumber);
    }

    @Override
    public void addInMaterial(AnZhuangTiaoShiMaterialOutInEntity anZhuangTiaoShiMaterialOutInEntity) {
        anZhuangTiaoShiMaterialOutInEntity.setInoutstate(1);
        anZhuangTiaoShiMaterialOutInDao.insertSelective(anZhuangTiaoShiMaterialOutInEntity);
    }

    @Override
    public AnZhuangTiaoShiMaterialEntity fiandMaterial(Integer id) {
        return anZhuangTiaoShiMaterialDao.selectByPrimaryKey(id);
    }

    @Override
    public void addOutMaterial(AnZhuangTiaoShiMaterialOutInEntity anZhuangTiaoShiMaterialOutInEntity) {
        anZhuangTiaoShiMaterialOutInEntity.setInoutstate(2);
        anZhuangTiaoShiMaterialOutInDao.insertSelective(anZhuangTiaoShiMaterialOutInEntity);
    }

    @Override
    public List<AnZhuangTiaoShiMaterialOutInEntity> findAllOutMaterial(Integer page, Integer size, String materialNumber) {
        return anZhuangTiaoShiMaterialOutInDao.findAllOutMaterial(materialNumber);
    }

    @Override
    public Integer totalNumber(Integer id) {
        return anZhuangTiaoShiMaterialDao.totalNumber(id);
    }

    @Override
    public void deleteMaterialByIds(Integer id) {
        anZhuangTiaoShiMaterialDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<AnZhuangTiaoShiMaterialEntity> findAllMaterialDatas(Integer page, Integer size, String materialNumber) {
        return anZhuangTiaoShiMaterialDao.findAllMaterialDatas(materialNumber);
    }

    @Override
    public void editMaterialById(AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity) {
        anZhuangTiaoShiMaterialDao.updateByPrimaryKeySelective(anZhuangTiaoShiMaterialEntity);
    }

    @Override
    public void addMaterial(AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity) {
        anZhuangTiaoShiMaterialEntity.setTotalnumber(0);
        anZhuangTiaoShiMaterialDao.insertSelective(anZhuangTiaoShiMaterialEntity);
    }

    @Override
    public List<AnZhuangTiaoShiMaterialEntity> findAllMaterial(Integer page, Integer size) {
        return anZhuangTiaoShiMaterialDao.findAllMaterial();
    }
}
