package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryService;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationAccessoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 9:26
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationAccessoryServiceImpl implements LineBaseInformationAccessoryService {

    @Autowired
    private LineBaseInformationAccessoryDao lineBaseInformationAccessoryDao;

    @Override
    public void add(LineBaseInformationAccessoryEntity entity) {
        lineBaseInformationAccessoryDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationAccessoryDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationAccessoryEntity entity) {
        lineBaseInformationAccessoryDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationAccessoryEntity findById(Integer id) {
        return lineBaseInformationAccessoryDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineBaseInformationAccessoryEntity> findByExample(LineBaseInformationAccessoryEntity query) {
        return lineBaseInformationAccessoryDao.selectByExample(query);
    }
}
