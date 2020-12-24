package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationDao;
import jdk.nashorn.internal.ir.EmptyNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:07
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationStationServiceImpl implements LineBaseInformationStationService {
    @Autowired
    private LineBaseInformationStationDao lineBaseInformationStationDao;

    @Override
    public void add(LineBaseInformationStationEntity entity) {
        lineBaseInformationStationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationStationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationStationEntity entity) {
        lineBaseInformationStationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationStationEntity findById(Integer id) {
        return lineBaseInformationStationDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds) {

    }

    @Override
    public void edit(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds) {

    }

    @Override
    public List<LineBaseInformationEntity> findHistoryByExample(Integer tid, Integer id, String name, Integer[] ids) {
        return null;
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return null;
    }
}
