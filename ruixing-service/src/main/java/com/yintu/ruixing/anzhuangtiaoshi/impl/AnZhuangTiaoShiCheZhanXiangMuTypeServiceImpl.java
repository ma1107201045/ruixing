package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 17:38
 * @Version 1.0
 * 需求:安装调试项目类型
 */
@Service
@Transactional
public class AnZhuangTiaoShiCheZhanXiangMuTypeServiceImpl implements AnZhuangTiaoShiCheZhanXiangMuTypeService {
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeDao anZhuangTiaoShiCheZhanXiangMuTypeDao;

    @Override
    public List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findXiangMuTypeByName(String xmname, Integer page, Integer size) {
        return anZhuangTiaoShiCheZhanXiangMuTypeDao.findXiangMuTypeByName(xmname);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAll() {
        return this.findXiangMuTypeByName("", null, null);
    }

    @Override
    public void deletXiangMuTypeByIds(Integer[] ids) {
        anZhuangTiaoShiCheZhanXiangMuTypeDao.deletXiangMuTypeByIds(ids);
    }

    @Override
    public void editXiangMuTypeById(AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity) {
        anZhuangTiaoShiCheZhanXiangMuTypeDao.editXiangMuTypeById(anZhuangTiaoShiCheZhanXiangMuTypeEntity);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAllXiangMuType(Integer page, Integer size) {
        return anZhuangTiaoShiCheZhanXiangMuTypeDao.findAllXiangMuType();
    }

    @Override
    public void addXiangMuType(AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity) {
        anZhuangTiaoShiCheZhanXiangMuTypeDao.addXiangMuType(anZhuangTiaoShiCheZhanXiangMuTypeEntity);
    }
}
