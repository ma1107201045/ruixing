package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductModelNumberDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductModelNumberEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductModelNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 17:28
 */
@Service
@Transactional
public class LineTechnologyStatusProductModelNumberServiceImpl implements LineTechnologyStatusProductModelNumberService {

    @Autowired
    private LineTechnologyStatusProductModelNumberDao lineTechnologyStatusProductModelNumberDao;

    @Override
    public void add(LineTechnologyStatusProductModelNumberEntity entity) {
        lineTechnologyStatusProductModelNumberDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusProductModelNumberDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusProductModelNumberEntity entity) {
        lineTechnologyStatusProductModelNumberDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusProductModelNumberEntity findById(Integer id) {
        return lineTechnologyStatusProductModelNumberDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineTechnologyStatusProductModelNumberEntity> findAll() {
        return lineTechnologyStatusProductModelNumberDao.selectAll();
    }
}
