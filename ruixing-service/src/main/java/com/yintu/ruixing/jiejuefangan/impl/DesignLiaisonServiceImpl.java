package com.yintu.ruixing.jiejuefangan.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonDao;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonEntity;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonService;
import com.yintu.ruixing.jiejuefangan.SolutionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/3 11:06
 */
@Service
@Transactional
public class DesignLiaisonServiceImpl implements DesignLiaisonService {
    @Autowired
    private DesignLiaisonDao designLiaisonDao;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private TieLuJuService tieLuJuService;


    @Override
    public void add(DesignLiaisonEntity entity) {
        designLiaisonDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        designLiaisonDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(DesignLiaisonEntity entity) {
        designLiaisonDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public DesignLiaisonEntity findById(Integer id) {
        DesignLiaisonEntity designLiaisonEntity = designLiaisonDao.selectByPrimaryKey(id);
        if (designLiaisonEntity != null) {
            designLiaisonEntity.setTieLuJuEntity(tieLuJuService.findTieLuJuById(designLiaisonEntity.getRailwayAdministrationId().longValue()));
        }
        return designLiaisonEntity;
    }

    @Override
    public List<DesignLiaisonEntity> findAll() {
        return designLiaisonDao.selectAll();
    }

    @Override
    public void add(DesignLiaisonEntity entity, String trueName) {
        this.add(entity);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(DesignLiaisonEntity entity, String trueName) {
        this.edit(entity);
    }


    @Override
    public List<DesignLiaisonEntity> findByYear(Integer year) {
        return designLiaisonDao.selectByYear(year);
    }

    @Override
    public List<DesignLiaisonEntity> findByExample(Integer year, String projectName) {
        return designLiaisonDao.selectByExample(year, projectName);
    }

    @Override
    public List<Integer> findByDistinctProjectDate() {
        return designLiaisonDao.selectByDistinctProjectDate();
    }

    @Override
    public List<TreeNodeUtil> findByTree() {
        List<Integer> years = this.findByDistinctProjectDate();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (Integer year : years) {
            List<DesignLiaisonEntity> designLiaisonEntities = this.findByYear(year);
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();
            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(1L);
            firstTreeNodeUtil.setLabel(String.valueOf(year));
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (DesignLiaisonEntity designLiaisonEntity : designLiaisonEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(designLiaisonEntity.getProjectName());
                Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSON(designLiaisonEntity).toString(), Map.class);
                secondTreeNodeUtil.setA_attr(map);
                secondTreeNodeUtil.setChildren(thirdTreeNodeUtils);
                secondTreeNodeUtils.add(secondTreeNodeUtil);
                TreeNodeUtil thirdTreeNodeUtil1 = new TreeNodeUtil();
                thirdTreeNodeUtil1.setId(3L);
                thirdTreeNodeUtil1.setLabel("输入文件");
                TreeNodeUtil thirdTreeNodeUtil2 = new TreeNodeUtil();
                thirdTreeNodeUtil2.setId(3L);
                thirdTreeNodeUtil2.setLabel("输出文件");
                thirdTreeNodeUtils.add(thirdTreeNodeUtil1);
                thirdTreeNodeUtils.add(thirdTreeNodeUtil2);
            }
        }
        return firstTreeNodeUtils;
    }
}
