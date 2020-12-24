package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationStationUnitEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationUnitService;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationUnitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:15
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationStationUnitServiceImpl implements LineBaseInformationStationUnitService {
    @Autowired
    private LineBaseInformationStationUnitDao lineBaseInformationStationUnitDao;

    @Override
    public void add(LineBaseInformationStationUnitEntity entity) {
        lineBaseInformationStationUnitDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationStationUnitDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationStationUnitEntity entity) {
        lineBaseInformationStationUnitDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationStationUnitEntity findById(Integer id) {
        return lineBaseInformationStationUnitDao.selectByPrimaryKey(id);
    }

    @Override
    public void removeByLineBaseInformationStationId(Integer lineBaseInformationStationId) {
        lineBaseInformationStationUnitDao.deleteByLineBaseInformationStationId(lineBaseInformationStationId);
    }
}
