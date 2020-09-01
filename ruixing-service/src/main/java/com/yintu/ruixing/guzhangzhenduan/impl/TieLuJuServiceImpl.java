package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.TieLuJuDao;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 铁路局的serviceimpl
 */
@Service
@Transactional
public class TieLuJuServiceImpl implements TieLuJuService {


    @Autowired
    private TieLuJuDao tieLuJuDao;

    @Override
    public TieLuJuEntity findTieLuJuById(Long tid) {
        return tieLuJuDao.findTieLuJuById(tid);
    }

    @Override
    public void addTieLuJU(TieLuJuEntity tieLuJuEntity) {
        tieLuJuDao.addTieLuJU(tieLuJuEntity);
    }

    @Override
    public void editTieLuJuById(TieLuJuEntity tieLuJuEntity) {
        tieLuJuDao.editTieLuJuById(tieLuJuEntity);
    }

    @Override
    public void delTieLuJu(Long tid) {
        tieLuJuDao.delTieLuJu(tid);
    }

    @Override
    public List<Integer> findId(Long tid) {
        return tieLuJuDao.findId(tid);
    }

    @Override
    public List<TieLuJuEntity> findAllTieLuJu() {
        return tieLuJuDao.findAllTieLuJu();
    }

    @Override
    public TieLuJuEntity findByTljId(Long tljId) {
        return tieLuJuDao.selectByTljId(tljId);
    }
}
