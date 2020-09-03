package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import com.yintu.ruixing.jiejuefangan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
            designLiaisonEntity.setTieLuJuEntity(tieLuJuService.findByTljId(designLiaisonEntity.getRailwayAdministrationId().longValue()));
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
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   项目创建日期：").append(DateUtil.formatDate(entity.getProjectDate()))
                .append("   投标人：").append(entity.getBidder())
                .append("   项目名称：").append(entity.getProjectName())
                .append("   所属路局：").append(tieLuJuService.findByTljId(entity.getRailwayAdministrationId().longValue()).getTljName())
                .append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误")
                .append("   会议状态：").append(entity.getMeetingStatus() == 1 ? "不召开会议" : entity.getMeetingStatus() == 2 ? "尚未开会" : entity.getMeetingStatus() == 3 ? "已召开设计联络会" : "错误")
                .append("   项目状态：").append(entity.getProjectStatus() == 1 ? "待确认需求" : entity.getProjectStatus() == 2 ? "已确认部分需求" : entity.getProjectStatus() == 3 ? "已确认全部需求" : "错误")
                .append("   变更状态：").append(entity.getChangeStatus() == 1 ? "无变更" : entity.getChangeStatus() == 2 ? "变更设计中" : entity.getChangeStatus() == 3 ? "变更已定型" : "错误")
                .append("   备注：").append(entity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 1, entity.getId(), sb.toString()));
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(DesignLiaisonEntity entity, String trueName) {
        DesignLiaisonEntity source = this.findById(entity.getId());
        if (source != null) {
            this.edit(entity);
            //项目日志记录
            DesignLiaisonEntity target = BeanUtil.compareFieldValues(source, entity, DesignLiaisonEntity.class);
            StringBuilder sb = new StringBuilder();
            if (target.getBidder() != null) {
                sb.append("   招标人：").append(entity.getBiddingId());
            }
            if (target.getProjectName() != null) {
                sb.append("   项目名称：").append(entity.getProjectName());
            }
            if (target.getRailwayAdministrationId() != null) {
                sb.append("   所属路局：").append(tieLuJuService.findByTljId(entity.getRailwayAdministrationId().longValue()).getTljName());
            }
            if (target.getTaskStatus() != null) {
                sb.append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误");
            }
            if (target.getMeetingStatus() != null) {
                sb.append("   会议状态：").append(entity.getMeetingStatus() == 1 ? "不召开会议" : entity.getMeetingStatus() == 2 ? "尚未开会" : entity.getMeetingStatus() == 3 ? "已召开设计联络会" : "错误");
            }
            if (target.getProjectStatus() != null) {
                sb.append("   项目状态：").append(entity.getProjectStatus() == 1 ? "未知" : entity.getProjectStatus() == 2 ? "后续招标" : entity.getProjectStatus() == 3 ? "确定采用" : entity.getProjectStatus() == 4 ? "关闭" : "错误");
            }
            if (target.getChangeStatus() != null) {
                sb.append("   变更状态：").append(entity.getChangeStatus() == 1 ? "无变更" : entity.getChangeStatus() == 2 ? "变更设计中" : entity.getChangeStatus() == 3 ? "变更已定型" : "错误");
            }
            if (target.getRemark() != null) {
                sb.append("   备注：").append(entity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 1, source.getId(), sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }
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
            firstTreeNodeUtil.setValue(IdUtil.simpleUUID());
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (DesignLiaisonEntity designLiaisonEntity : designLiaisonEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(designLiaisonEntity.getProjectName());
                secondTreeNodeUtil.setValue(IdUtil.simpleUUID());
                Map<String, Object> secondMap = JSONObject.parseObject(JSONObject.toJSON(designLiaisonEntity).toString(), Map.class);
                secondTreeNodeUtil.setA_attr(secondMap);
                secondTreeNodeUtil.setChildren(thirdTreeNodeUtils);
                secondTreeNodeUtils.add(secondTreeNodeUtil);
                TreeNodeUtil thirdTreeNodeUtil1 = new TreeNodeUtil();
                thirdTreeNodeUtil1.setId(3L);
                thirdTreeNodeUtil1.setLabel("输入文件");
                thirdTreeNodeUtil1.setValue(IdUtil.simpleUUID());
                TreeNodeUtil thirdTreeNodeUtil2 = new TreeNodeUtil();
                thirdTreeNodeUtil2.setId(3L);
                thirdTreeNodeUtil2.setLabel("输出文件");
                thirdTreeNodeUtil2.setValue(IdUtil.simpleUUID());
                thirdTreeNodeUtils.add(thirdTreeNodeUtil1);
                thirdTreeNodeUtils.add(thirdTreeNodeUtil2);
            }
        }
        return firstTreeNodeUtils;
    }
}
