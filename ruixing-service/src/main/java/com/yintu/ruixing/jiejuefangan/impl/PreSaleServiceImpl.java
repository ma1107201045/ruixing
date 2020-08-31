package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
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
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;


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
    public void add(PreSaleEntity entity, String trueName) {
        this.add(entity);
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   项目名称：").append(entity.getProjectName())
                .append("   项目创建日期：").append(DateUtil.formatDate(entity.getProjectDate()))
                .append("   项目状态：").append(entity.getProjectStatus() == 1 ? "未知" : entity.getProjectStatus() == 2 ? "后续招标" : entity.getProjectStatus() == 3 ? "确定采用" : entity.getProjectStatus() == 4 ? "关闭" : "错误")
                .append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误")
                .append("   备注：").append(entity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 1, entity.getId(), sb.toString()));

        //售后技术支持项目状态为3时发送消息
        if (entity.getProjectStatus() == 3) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle("");
            messageEntity.setContext("“" + entity.getProjectName() + "”项目已中标，请关注项目进展情况，及时进行设计联络！");
            messageEntity.setType((short) 1);
            messageEntity.setSmallType((short) 1);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }


    @Override
    public void edit(PreSaleEntity entity, String trueName) {
        SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 1, entity.getId(), null);
        PreSaleEntity source = this.findById(entity.getId());
        if (source != null) {
            this.edit(entity);
            //项目日志记录
            PreSaleEntity target = BeanUtil.compareFieldValues(source, entity, PreSaleEntity.class);
            StringBuilder sb = new StringBuilder();
            if (target.getProjectName() != null) {
                sb.append("   项目名称：").append(entity.getProjectName());
            }
            if (target.getProjectStatus() != null) {
                sb.append("   项目状态：").append(entity.getProjectStatus() == 1 ? "未知" : entity.getProjectStatus() == 2 ? "后续招标" : entity.getProjectStatus() == 3 ? "确定采用" : entity.getProjectStatus() == 4 ? "关闭" : "错误");
            }
            if (target.getTaskStatus() != null) {
                sb.append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误");
            }
            if (target.getRemark() != null) {
                sb.append("   备注：").append(entity.getRemark());
            }
            if (!"".equals(sb.toString().trim())) {
                solutionLogEntity.setContext(sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }

    }

    @Override
    public List<PreSaleEntity> findByYear(Integer year) {
        return preSaleDao.selectByYear(year);
    }

    @Override
    public List<PreSaleEntity> findByExample(Integer year, String projectName) {
        return preSaleDao.selectByExample(year, projectName);
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
                Map<String,Object> map = JSONObject.parseObject(JSONObject.toJSON(preSaleEntity).toString(), Map.class);
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
