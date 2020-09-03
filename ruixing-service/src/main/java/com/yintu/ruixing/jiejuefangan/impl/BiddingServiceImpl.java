package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
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
 * @date:2020/7/2 11:13
 */
@Service
@Transactional
public class BiddingServiceImpl implements BiddingService {
    @Autowired
    private BiddingDao biddingDao;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TieLuJuService tieLuJuService;

    @Override
    public void add(BiddingEntity entity) {
        biddingDao.insertSelective(entity);

    }

    @Override
    public void remove(Integer id) {
        biddingDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(BiddingEntity entity) {
        biddingDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public BiddingEntity findById(Integer id) {
        BiddingEntity biddingEntity = biddingDao.selectByPrimaryKey(id);
        if (biddingEntity != null) {
            biddingEntity.setTieLuJuEntity(tieLuJuService.findByTljId(biddingEntity.getRailwayAdministrationId().longValue()));
        }
        return biddingEntity;

    }

    @Override
    public List<BiddingEntity> findAll() {
        return biddingDao.selectAll();
    }

    @Override
    public void add(BiddingEntity entity, String trueName) {
        this.add(entity);
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   项目创建日期：").append(DateUtil.formatDate(entity.getProjectDate()))
                .append("   投标人：").append(entity.getBidder())
                .append("   项目名称：").append(entity.getProjectName())
                .append("   所属路局：").append(tieLuJuService.findByTljId(entity.getRailwayAdministrationId().longValue()).getTljName())
                .append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误")
                .append("   项目状态：").append(entity.getProjectStatus() == 1 ? "正在投标" : entity.getProjectStatus() == 2 ? "未中标" : entity.getProjectStatus() == 3 ? "已中标" : entity.getProjectStatus() == 4 ? "流标再投" : "错误")
                .append("   备注：").append(entity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 1, entity.getId(), sb.toString()));

        //投招标支持项目状态为3时发送消息
        if (entity.getProjectStatus().equals((short) 3)) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(entity.getCreateBy());
            messageEntity.setCreateTime(entity.getCreateTime());
            messageEntity.setModifiedBy(entity.getModifiedBy());
            messageEntity.setModifiedTime(entity.getModifiedTime());
            messageEntity.setTitle("项目");
            messageEntity.setContext("“" + entity.getProjectName() + "”项目已中标，请关注项目进展情况，及时进行设计联络！");
            messageEntity.setType((short) 1);
            messageEntity.setSmallType((short) 2);
            messageEntity.setMessageType((short) 1);
            messageEntity.setProjectId(entity.getId());
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
    public void edit(BiddingEntity entity, String trueName) {
        BiddingEntity source = this.findById(entity.getId());
        if (source != null) {
            this.edit(entity);
            //项目日志记录
            BiddingEntity target = BeanUtil.compareFieldValues(source, entity, BiddingEntity.class);
            StringBuilder sb = new StringBuilder();
            if (target.getBidder() != null) {
                sb.append("   投标人：").append(entity.getBidder());
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
            if (target.getProjectStatus() != null) {
                sb.append("   项目状态：").append(entity.getProjectStatus() == 1 ? "正在投标" : entity.getProjectStatus() == 2 ? "未中标" : entity.getProjectStatus() == 3 ? "已中标" : entity.getProjectStatus() == 4 ? "流标再投" : "错误");
            }
            if (target.getRemark() != null) {
                sb.append("   备注：").append(entity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 1, source.getId(), sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }
    }

    @Override
    public List<BiddingEntity> findByYear(Integer year) {
        return biddingDao.selectByYear(year);
    }

    @Override
    public List<BiddingEntity> findByExample(Integer year, String projectName) {
        return biddingDao.selectByExample(year, projectName);
    }

    @Override
    public List<Integer> findByDistinctProjectDate() {
        return biddingDao.selectByDistinctProjectDate();
    }

    @Override
    public List<TreeNodeUtil> findByTree() {
        List<Integer> years = this.findByDistinctProjectDate();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (Integer year : years) {
            List<BiddingEntity> biddingEntities = this.findByYear(year);
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();

            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(1L);
            firstTreeNodeUtil.setLabel(String.valueOf(year));
            firstTreeNodeUtil.setValue(IdUtil.simpleUUID());
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);

            for (BiddingEntity biddingEntity : biddingEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(biddingEntity.getProjectName());
                secondTreeNodeUtil.setValue(IdUtil.simpleUUID());
                Map<String, Object> secondMap = JSONObject.parseObject(JSONObject.toJSON(biddingEntity).toString(), Map.class);
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
