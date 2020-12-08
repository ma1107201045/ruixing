package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationService;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:46
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationServiceImpl implements LineBaseInformationService {
    @Autowired
    private LineBaseInformationDao lineBaseInformationDao;

    @Override
    public void add(LineBaseInformationEntity entity) {
        lineBaseInformationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationEntity entity) {
        lineBaseInformationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationEntity findById(Integer id) {
        return lineBaseInformationDao.selectByPrimaryKey(id);
    }
}
