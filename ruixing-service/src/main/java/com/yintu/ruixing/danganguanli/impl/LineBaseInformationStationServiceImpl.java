package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationDao;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationUnitDao;
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

    @Autowired
    private LineBaseInformationStationUnitService lineBaseInformationStationUnitService;

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
    public void add(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds) {
        this.add(lineBaseInformationStationEntity);
        for (Integer unitId : unitIds) {
            LineBaseInformationStationUnitEntity lineBaseInformationStationUnitEntity = new LineBaseInformationStationUnitEntity();
            lineBaseInformationStationUnitEntity.setCreateBy(lineBaseInformationStationEntity.getCreateBy());
            lineBaseInformationStationUnitEntity.setCreateTime(lineBaseInformationStationEntity.getCreateTime());
            lineBaseInformationStationUnitEntity.setModifiedBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setModifiedTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
            lineBaseInformationStationUnitEntity.setUnitId(unitId);
            lineBaseInformationStationUnitService.add(lineBaseInformationStationUnitEntity);
        }
    }

    @Override
    public void edit(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds) {
        this.edit(lineBaseInformationStationEntity);
        lineBaseInformationStationUnitService.removeByLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
        for (Integer unitId : unitIds) {
            LineBaseInformationStationUnitEntity lineBaseInformationStationUnitEntity = new LineBaseInformationStationUnitEntity();
            lineBaseInformationStationUnitEntity.setCreateBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setCreateTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setModifiedBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setModifiedTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
            lineBaseInformationStationUnitEntity.setUnitId(unitId);
            lineBaseInformationStationUnitService.add(lineBaseInformationStationUnitEntity);
        }
    }

    @Override
    public List<LineBaseInformationStationEntity> findHistoryByExample(Integer tid, Integer id, String name, Integer[] ids) {
        List<LineBaseInformationStationEntity> lineBaseInformationStationEntities = lineBaseInformationStationDao.selectByExample(tid, id, name, ids);
        for (LineBaseInformationStationEntity lineBaseInformationStationEntity : lineBaseInformationStationEntities) {
            lineBaseInformationStationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationStationEntity.getId()));
        }
        return lineBaseInformationStationEntities;
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return lineBaseInformationStationDao.selectDianWuDuanEntityById(id);
    }
}
