package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return lineBaseInformationDao.selectDianWuDuanEntityById(id);
    }

    @Override
    public List<LineBaseInformationEntity> findByExample(Integer[] ids) {
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationDao.selectByExample(ids);
        for (LineBaseInformationEntity lineBaseInformationEntity : lineBaseInformationEntities) {
            lineBaseInformationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationEntity.getId()));
        }
        return lineBaseInformationEntities;
    }


}
