package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.master.danganguanli.LineTechnologyStatusXiangemutypeDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusXiangemutypeEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusXiangemutypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/23 11:07
 */
@Service
@Transactional
public class LineTechnologyStatusXiangemutypeServiceImpl implements LineTechnologyStatusXiangemutypeService {

    @Autowired
    private LineTechnologyStatusXiangemutypeDao lineTechnologyStatusXiangemutypeDao;

    @Override
    public void add(LineTechnologyStatusXiangemutypeEntity entity) {
        lineTechnologyStatusXiangemutypeDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusXiangemutypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusXiangemutypeEntity entity) {
        lineTechnologyStatusXiangemutypeDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusXiangemutypeEntity findById(Integer id) {
        return lineTechnologyStatusXiangemutypeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineTechnologyStatusXiangemutypeEntity> findByExample(Integer lineTechnologyStatusId) {
        return lineTechnologyStatusXiangemutypeDao.selectByExample(lineTechnologyStatusId);
    }
}
