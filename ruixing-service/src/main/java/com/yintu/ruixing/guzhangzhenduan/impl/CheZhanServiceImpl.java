package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.CheZhanDao;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseDao;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO
 *
 * @description:
 * @author: Qiao
 * @time: 2020/5/25 17:10
 */
@Service
@Transactional
public class CheZhanServiceImpl implements CheZhanService {

    @Autowired
    private CheZhanDao cheZhanDao;

    @Autowired
    private QuDuanBaseDao quDuanBaseDao;

    @Override
    public List<CheZhanEntity> findXiangLinTwoCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinTwoCheZhanByXdid(xdid);
    }

    @Override
    public List<CheZhanEntity> findXiangLinThreeCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinThreeCheZhanByXdid(xdid);
    }

    @Override
    public List<CheZhanEntity> findXiangLinFourCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinFourCheZhanByXdid(xdid);
    }

    @Override
    public List<CheZhanEntity> findXiangLinFiveCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinFiveCheZhanByXdid(xdid);
    }

    @Override
    public List<CheZhanEntity> findXiangLinSixCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinSixCheZhanByXdid(xdid);
    }


    @Override
    public List<CheZhanEntity> findXiangLinOneCheZhanByXdid(Integer xdid) {
        return cheZhanDao.findXiangLinOneCheZhanByXdid(xdid);
    }

    @Override
    public void add(CheZhanEntity cheZhanEntity) {
        long xid = cheZhanEntity.getXid();
        cheZhanEntity.setCzState(0);
        cheZhanEntity.setXdCzId(xid);
        cheZhanEntity.setPublicMessage(0);
        cheZhanDao.insertChezhan(cheZhanEntity);
    }

    @Override
    public void update(CheZhanEntity cheZhanEntity) {
        cheZhanDao.updateCheZhan(cheZhanEntity);
    }

    @Override
    public CheZhanEntity findByCheZhanId(Long cid) {
        return cheZhanDao.selectByCheZhan(cid);
    }

    @Override
    public void delCheZhan(Long cid) {
        cheZhanDao.delCheZhan(cid);
    }

    @Override
    public List<CheZhanEntity> findAll() {
        return cheZhanDao.selectAll();
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByCid(Long cid) {
        return quDuanBaseDao.findQuDuanByCid(cid);
    }

    @Override
    public List<CheZhanEntity> findByCzName(String czName) {
        return cheZhanDao.selectByCzName(czName);
    }

    @Override
    public String findJsonByCzId(Integer czId) {
        return cheZhanDao.selectJsonByCzId(czId);
    }

    @Override
    public CheZhanEntity findByczId(Integer czId) {
        return cheZhanDao.selectByczId(czId);
    }

    @Override
    public Boolean findCzStutrs(Long id, Boolean isPrimaryKey) {
        Integer czStutrs = cheZhanDao.selectCzStutrs(id, isPrimaryKey);
        return !(czStutrs == null || czStutrs == 0);
    }

}
