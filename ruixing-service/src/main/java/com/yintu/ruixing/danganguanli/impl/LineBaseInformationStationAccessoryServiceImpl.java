package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationAccessoryEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationAccessoryService;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationAccessoryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:37
 * @Version: 1.0
 */
public class LineBaseInformationStationAccessoryServiceImpl implements LineBaseInformationStationAccessoryService {
    @Autowired
    private LineBaseInformationStationAccessoryDao lineBaseInformationStationAccessoryDao;


    @Override
    public void add(LineBaseInformationStationAccessoryEntity entity) {
        lineBaseInformationStationAccessoryDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationStationAccessoryDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationStationAccessoryEntity entity) {
        lineBaseInformationStationAccessoryDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationStationAccessoryEntity findById(Integer id) {
        return lineBaseInformationStationAccessoryDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineBaseInformationStationAccessoryEntity> findByExample(LineBaseInformationStationAccessoryEntity query) {
        return lineBaseInformationStationAccessoryDao.selectByExample(query);
    }
}
