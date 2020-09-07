package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyDao;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/7 10:08
 */
@Service
@Transactional
public class QuDuanInfoPropertyServiceImpl implements QuDuanInfoPropertyService {
    @Autowired
    private QuDuanInfoPropertyDao quDuanInfoPropertyDao;

    @Override
    public void add(QuDuanInfoPropertyEntity entity) {
        quDuanInfoPropertyDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        quDuanInfoPropertyDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(QuDuanInfoPropertyEntity entity) {
        quDuanInfoPropertyDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public QuDuanInfoPropertyEntity findById(Integer id) {
        return quDuanInfoPropertyDao.selectByPrimaryKey(id);
    }

    @Override
    public List<QuDuanInfoPropertyEntity> findByType(Short type) {
        return quDuanInfoPropertyDao.selectByType(type);
    }

    @Override
    public List<QuDuanInfoPropertyEntity> findByIds(Integer[] ids) {
        return quDuanInfoPropertyDao.selectByIds(ids);
    }

    @Override
    public List<QuDuanInfoPropertyEntity> finByName(String name) {
        return quDuanInfoPropertyDao.selectByName(name);
    }
}
