package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.DeleteDao;
import com.yintu.ruixing.guzhangzhenduan.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author:lcy
 * @date:2020-06-02 20
 * 分页
 */
@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {
    @Autowired
    private DeleteDao deleteDao;
/*
    @Override
    public PageInfo<DataStatsEntity> tljPage(String id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all=pageDao.tljPage();
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(all);
        return pageInfo;

    }

    @Override
    public PageInfo<DataStatsEntity> dwdPage(String id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all=pageDao.dwdPage();
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(all);
        return pageInfo;
    }

    @Override
    public PageInfo<DataStatsEntity> czPage(String id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all=pageDao.czPage();
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(all);
        return pageInfo;
    }

    @Override
    public PageInfo<DataStatsEntity> xdPage(String id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all=pageDao.xdPage();
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(all);
        return pageInfo;
    }*/

    @Override
    public int delTieLuJU(int[] ids) {
        return deleteDao.delTieLuJU(ids);
    }

    @Override
    public int delDianDuDuan(int[] ids) {
        return deleteDao.delDianDuDuan(ids);
    }

    @Override
    public int delXianDuan(int[] ids) {
        return deleteDao.delXianDuan(ids);
    }

    @Override
    public int delCheZhan(int[] ids) {
        return deleteDao.delCheZhan(ids);
    }
}
