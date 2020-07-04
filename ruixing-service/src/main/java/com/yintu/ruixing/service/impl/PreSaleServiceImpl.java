package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.dao.PreSaleDao;
import com.yintu.ruixing.entity.PreSaleEntity;
import com.yintu.ruixing.service.PreSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author:mlf
 * @date:2020/6/30 18:53
 */
@Service
@Transactional
public class PreSaleServiceImpl implements PreSaleService {

    @Autowired
    private PreSaleDao preSaleDao;


    @Override
    public void add(PreSaleEntity entity) {
        preSaleDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        preSaleDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(PreSaleEntity entity) {
        preSaleDao.updateByPrimaryKeySelective(entity);

    }

    @Override
    public PreSaleEntity findById(Integer id) {
        return preSaleDao.selectByPrimaryKey(id);
    }

    @Override
    public List<PreSaleEntity> findAll() {
        return preSaleDao.selectAll();
    }

    @Override
    public List<PreSaleEntity> findByYear(Integer year) {
        return preSaleDao.selectByYear(year);
    }

    @Override
    public List<Integer> findByDistinctProjectDate() {
        return preSaleDao.selectByDistinctProjectDate();
    }


    @Override
    public List<TreeNodeUtil> findByTree() {
        List<Integer> years = this.findByDistinctProjectDate();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (Integer year : years) {
            List<PreSaleEntity> preSaleEntities = this.findByYear(year);
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();

            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(1L);
            firstTreeNodeUtil.setLabel(String.valueOf(year));
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);

            for (PreSaleEntity preSaleEntity : preSaleEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();

                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(preSaleEntity.getProjectName());
                Map<String, Object> map = new HashMap<>();
                map.put("id", preSaleEntity.getId());
                map.put("projectDate", preSaleEntity.getProjectDate());
                map.put("projectName", preSaleEntity.getProjectName());
                map.put("projectStatus", preSaleEntity.getProjectStatus());
                map.put("taskStatus", preSaleEntity.getTaskStatus());
                map.put("taskFinishStatus", preSaleEntity.getTaskFinishDate());
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
