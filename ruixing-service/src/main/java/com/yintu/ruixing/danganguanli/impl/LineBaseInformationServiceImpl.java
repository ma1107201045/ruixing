package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> findRailwaysBureauTid() {
        return lineBaseInformationDao.selectRailwaysBureauTid();
    }

    @Override
    public List<Map<String, Object>> findByTid(Integer tid) {
        return lineBaseInformationDao.selectByTid(tid);
    }

    @Override
    public List<Map<String, Object>> findStationById(Integer id) {
        return lineBaseInformationDao.selectStationById(id);
    }

    @Override
    public List<TreeNodeUtil> findTree() {
        List<Map<String, Object>> maps1 = this.findRailwaysBureauTid();
        List<TreeNodeUtil> first = new ArrayList<>();
        for (Map<String, Object> map1 : maps1) {
            Integer tid = (Integer) map1.get("tid");
            List<Map<String, Object>> maps2 = this.findByTid(tid);
            List<TreeNodeUtil> second = new ArrayList<>();
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setId((long) tid);
            treeNodeUtil1.setValue(String.valueOf(tid - 1000000L));
            treeNodeUtil1.setLabel((String) map1.get("tlj_name"));
            treeNodeUtil1.setChildren(second);
            for (Map<String, Object> map2 : maps2) {
                Integer id = (Integer) map2.get("id");
                List<Map<String, Object>> maps3 = this.findStationById(id);
                List<TreeNodeUtil> third = new ArrayList<>();
                TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                treeNodeUtil2.setId((long) id);
                treeNodeUtil2.setValue(String.valueOf(id));
                treeNodeUtil2.setLabel((String) map2.get("name"));
                treeNodeUtil2.setChildren(third);
                for (Map<String, Object> map3 : maps3) {
                    Integer stationId = (Integer) map3.get("id");
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil3.setId((long) stationId);
                    treeNodeUtil3.setValue(String.valueOf(stationId + 1000000L));
                    treeNodeUtil3.setLabel((String) map3.get("name"));
                    third.add(treeNodeUtil3);
                }
                second.add(treeNodeUtil2);
            }
            first.add(treeNodeUtil1);
        }
        return first;
    }

    @Override
    public List<LineBaseInformationEntity> findByExample(Integer[] ids) {
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationDao.selectByExample(ids);
        for (LineBaseInformationEntity lineBaseInformationEntity : lineBaseInformationEntities) {
            lineBaseInformationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationEntity.getId()));
        }
        return lineBaseInformationEntities;
    }


    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return lineBaseInformationDao.selectDianWuDuanEntityById(id);
    }


}
