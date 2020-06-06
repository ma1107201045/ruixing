package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.GuZhangStudyDao;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.GuZhangStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-06-04 16
 */
@Service
@Transactional
public class GuZhangStudyServiceImpl implements GuZhangStudyService {
    @Autowired
    private GuZhangStudyDao guZhangStudyDao;

    @Override
    public List<GuZhangStudyEntity> findGuZhangList(Integer page, Integer size) {
        return guZhangStudyDao.findGuZhangList();
    }

    @Override
    public GuZhangStudyEntity findGuZhangById(Long id) {
        return guZhangStudyDao.selectByPrimaryKey(id);
    }

    @Override
    public void addGuZhang(GuZhangStudyEntity guZhangStudyEntity) {
        guZhangStudyDao.addGuZhang(guZhangStudyEntity);
    }

    @Override
    public void editGuZhang(Long id) {
        guZhangStudyDao.editGuZhang(id);
    }

    @Override
    public void deletGuZhang(Long id) {
        guZhangStudyDao.deletGuZhang(id);
    }

    @Override
    public void deletGuZhangList(int[] ids) {
        guZhangStudyDao.deletGuZhangList(ids);
    }


    public List<GuZhangStudyEntity> GuZhangListExcelDownloads() {
        return guZhangStudyDao.GuZhangListExcelDownloads();


    }

    @Override
    public List<GuZhangStudyEntity> GuZhangListExcelDownloadsById(Long id) {
        return guZhangStudyDao.GuZhangListExcelDownloadsById(id);
    }

    @Override
    public List<XianDuanEntity> getXianDuan(XianDuanEntity xianDuanEntity) {
        return guZhangStudyDao.getXianDuan(xianDuanEntity);
    }

    @Override
    public List<CheZhanEntity> getCheZhanByXid(Long xid) {
        return guZhangStudyDao.getCheZhanByXid(xid);
    }

    @Override
    public List<QuDuanBaseEntity> getQuDuanByXid(Long cid) {
        return guZhangStudyDao.getQuDuanByXid(cid);
    }

    @Override
    public List<QuDuanInfoEntity> findGuZhangKuData(Long id) {
        return guZhangStudyDao.findGuZhangKuData(id);
    }
}
