package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/16 16:49
 * @Version 1.0
 * 需求:安装调试现场作业的作业项
 */
@Service
@Transactional
public class AnZhuangTiaoShiWorkNameLibraryServiceImpl implements AnZhuangTiaoShiWorkNameLibraryService {

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryDao anZhuangTiaoShiWorkNameLibraryDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorksById(Integer id) {
        return anZhuangTiaoShiWorkNameLibraryDao.findAllWorksById(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorkName() {
        return anZhuangTiaoShiWorkNameLibraryDao.findAllWorkName();
    }

    @Override
    public void deleteWorkNameByIds(Integer[] ids) {
        anZhuangTiaoShiWorkNameLibraryDao.deleteWorkNameByIds(ids);
        anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.deleteWorkNameByIdss(ids);
    }

    @Override
    public void editWorkNameById(AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity) {
        anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorkNameLibraryEntity);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorkName(Integer page, Integer size, String workname) {
        return anZhuangTiaoShiWorkNameLibraryDao.findWorkName(workname);
    }

    @Override
    public void addWorkName(AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity) {
        anZhuangTiaoShiWorkNameLibraryDao.insertSelective(anZhuangTiaoShiWorkNameLibraryEntity);
    }
}
