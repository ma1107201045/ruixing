package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationUnitEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationUnitService;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationUnitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:50
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationUnitServiceImpl implements LineBaseInformationUnitService {
    @Autowired
    private LineBaseInformationUnitDao lineBaseInformationUnitDao;

    @Override
    public void add(LineBaseInformationUnitEntity entity) {
        lineBaseInformationUnitDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationUnitDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationUnitEntity entity) {
        lineBaseInformationUnitDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationUnitEntity findById(Integer id) {
        return lineBaseInformationUnitDao.selectByPrimaryKey(id);
    }

    @Override
    public void removeByLineBaseInformationId(Integer lineBaseInformationId) {
        lineBaseInformationUnitDao.deleteByLineBaseInformationId(lineBaseInformationId);
    }
}
