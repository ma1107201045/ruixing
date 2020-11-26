package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.QuDuanBaseDao;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/15 19:32
 */
@Service
@Transactional
public class QuDuanBaseServiceImpl implements QuDuanBaseService {

    @Autowired
    private QuDuanBaseDao quDuanBaseDao;

    @Override
    public void add(QuDuanBaseEntity quDuanBaseEntity) {
        quDuanBaseDao.insertSelective(quDuanBaseEntity);
    }

    @Override
    public void remove(Integer id) {
        quDuanBaseDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(QuDuanBaseEntity quDuanBaseEntity) {
        quDuanBaseDao.updateByPrimaryKeySelective(quDuanBaseEntity);
    }

    @Override
    public QuDuanBaseEntity findById(Integer id) {
        return quDuanBaseDao.selectByPrimaryKey(id);
    }

    @Override
    public QuDuanBaseEntity findByCzIdAndQuduanyunyingName(Integer czId, String quDuanYunYingName) {
        return quDuanBaseDao.selectByCzIdAndQuduanyunyingName(czId, quDuanYunYingName);
    }

    @Override
    public List<QuDuanBaseEntity> findByCzIdAndQdId(Integer czId, Integer qdid, Boolean isDianMaHua) {
        return quDuanBaseDao.selectByCzIdAndQdId(czId, qdid, isDianMaHua);
    }

    @Override
    public List<QuDuanBaseEntity> findByQdIdAndQuDuanYunYingName( String quDuanYunYingName) {
        return quDuanBaseDao.selectByQdIdAndQuDuanYunYingName(quDuanYunYingName);
    }
}
