package com.yintu.ruixing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.dao.DataStatsDao;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.DataStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */

@Service
@Transactional
public class DataStatsServiceImpl implements DataStatsService {

    @Autowired
    private DataStatsDao dataStatsDao;
    //查询所有数据
    @Override
    public List<DataStatsEntity> findAll() {
        return dataStatsDao.findAll();
    }


    @Override
    public List<DataStatsEntity> findTieLuJuById(Long tid, Integer page, Integer size) {
       List<DataStatsEntity> tieLuJuEntities= dataStatsDao.findTieLuJuById(tid);
        return tieLuJuEntities;
    }

    @Override
    public List<DataStatsEntity> findDianWuDuanCheZhanById(Long did, Integer page, Integer size) {
        List<DataStatsEntity> dianwuduan= dataStatsDao.findDianWuDuanCheZhanById(did);
        return dianwuduan;
    }

    @Override
    public List<DataStatsEntity> findXianDuanCheZhanById(Long xid, Integer page, Integer size) {
        List<DataStatsEntity> xianduan= dataStatsDao.findXianDuanCheZhanById(xid);
        return xianduan;
    }

    @Override
    public List<DataStatsEntity> findCheZhanById(Long cid, Integer page, Integer size) {
        List<DataStatsEntity> chezhan= dataStatsDao.findCheZhanById(cid);
        return chezhan;
    }


    @Override
    public void editStateByXid(XianDuanEntity xianDuanEntity) {
        dataStatsDao.editStateByXid(xianDuanEntity);
    }
    @Override
    public void editStateByCid(CheZhanEntity cheZhanEntity) {
        dataStatsDao.editStateByCid(cheZhanEntity);
    }









    @Override
    public List<TieLuJuEntity> findAllTieLuJu(TieLuJuEntity tieLuJuEntity) {
        return dataStatsDao.findAllTieLuJu(tieLuJuEntity);
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanByTid(Integer tid) {
        return dataStatsDao.findDianWuDuanByTid(tid);
    }

    @Override
    public List<XianDuanEntity> findXianDuanByDid(Integer did) {
        return dataStatsDao.findXianDuanByDid(did);
    }

    @Override
    public List<CheZhanEntity> findCheZhanByXid(Integer xid) {
        return dataStatsDao.findCheZhanByXid(xid);
    }


    @Override
    public List<DataStatsEntity> findAllCheZhan(Integer page, Integer size) {
        return dataStatsDao.findAllCheZhan();
    }









    @Override
    public List<DataStatsEntity> findDianWuDuanById(Long tid, Long did, Integer page, Integer size) {
         List<DataStatsEntity> dataStatEntities =dataStatsDao.findDianWuDuanById(tid,did);
        System.out.println("从后端传来的数据"+ dataStatEntities);
        return dataStatEntities;
    }

    @Override
    public List<DataStatsEntity> findXianDuanById(Long tid, Long did, Long xid, Integer page, Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsDao.findXianDuanById(tid,did,xid);
        return dataStatEntities;
    }

    @Override
    public List<DataStatsEntity> findCheZhanById(Long tid, Long did, Long xid, Long cid, Integer page, Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsDao.findCheZhanById(tid,did,xid,cid);
        System.out.println("后端车站信息"+ dataStatEntities);
        return dataStatEntities;
    }

    @Override
    public int  delCheZhanListById(int[] ids) {
      //  String[] id = ids.split(",");
        return dataStatsDao.delCheZhanListById(ids);
    }





    //分页查询
    @Override
    public PageInfo<DataStatsEntity> findPage(Integer page, Integer size) {
        //分页
        PageHelper.startPage(page,size);
        //集合查询
        List<DataStatsEntity> all = dataStatsDao.findAll();

        return new PageInfo<DataStatsEntity>(all);
    }


}
