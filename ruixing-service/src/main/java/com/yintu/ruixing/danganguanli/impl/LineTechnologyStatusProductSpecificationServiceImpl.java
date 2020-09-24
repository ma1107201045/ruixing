package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductSpecificationDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductSpecificationEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 18:13
 */
@Service
@Transactional
public class LineTechnologyStatusProductSpecificationServiceImpl implements LineTechnologyStatusProductSpecificationService {

    @Autowired
    private LineTechnologyStatusProductSpecificationDao lineTechnologyStatusProductSpecificationDao;

    @Override
    public void add(LineTechnologyStatusProductSpecificationEntity entity) {
        lineTechnologyStatusProductSpecificationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusProductSpecificationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusProductSpecificationEntity entity) {
        lineTechnologyStatusProductSpecificationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusProductSpecificationEntity findById(Integer id) {
        return lineTechnologyStatusProductSpecificationDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineTechnologyStatusProductSpecificationEntity> findAll() {
        return lineTechnologyStatusProductSpecificationDao.selectAll();
    }
}
