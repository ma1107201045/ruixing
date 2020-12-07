package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonEntity;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonService;
import com.yintu.ruixing.jiejuefangan.SolutionLogEntity;
import com.yintu.ruixing.jiejuefangan.SolutionLogService;
import com.yintu.ruixing.master.jiejuefangan.DesignLiaisonDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

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
    private MessageService messageService;
    @Autowired
    private TieLuJuService tieLuJuService;
    @Autowired
    private UserService userService;


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
        if (entity.getProjectStatus() != 3) {
            List<UserEntity> userEntities = userService.findByTruename(null);
            for (UserEntity userEntity : userEntities) {
                //消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(entity.getCreateBy());
                messageEntity.setCreateTime(entity.getCreateTime());
                messageEntity.setModifiedBy(entity.getModifiedBy());
                messageEntity.setModifiedTime(entity.getModifiedTime());
                messageEntity.setTitle("项目");
                messageEntity.setContext("请关注“" + entity.getProjectName() + "”项目进展情况，及时确认产品和服务需求，在SAP中下达！");
                messageEntity.setType((short) 1);
                messageEntity.setSmallType((short) 3);
                messageEntity.setMessageType((short) 1);
                messageEntity.setProjectId(entity.getId());
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(userEntity.getId().intValue());
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
        if (entity.getChangeStatus() == 2) {
            List<UserEntity> userEntities = userService.findByTruename(null);
            for (UserEntity userEntity : userEntities) {
                //消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(entity.getCreateBy());
                messageEntity.setCreateTime(entity.getCreateTime());
                messageEntity.setModifiedBy(entity.getModifiedBy());
                messageEntity.setModifiedTime(entity.getModifiedTime());
                messageEntity.setTitle("项目");
                messageEntity.setContext("请关注“" + entity.getProjectName() + "”项目变更情况，及时调整需求！");
                messageEntity.setType((short) 1);
                messageEntity.setSmallType((short) 3);
                messageEntity.setMessageType((short) 1);
                messageEntity.setProjectId(entity.getId());
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(userEntity.getId().intValue());
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
        //售前技术支持或者投招标技术支持消息更改


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
            //项目状态
            if (entity.getProjectStatus() == 3 && source.getProjectStatus() != 3) {
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 3, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    if (messageEntity.getContext().contains("项目进展情况")) {
                        messageEntity.setModifiedBy(entity.getModifiedBy());
                        messageEntity.setModifiedTime(entity.getModifiedTime());
                        messageEntity.setStatus((short) 3);
                        messageService.edit(messageEntity);
                    }
                }
            }
            if (entity.getProjectStatus() != 3 && source.getProjectStatus() == 3) {
                List<UserEntity> userEntities = userService.findByTruename(null);
                for (UserEntity userEntity : userEntities) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(entity.getCreateBy());
                    messageEntity.setCreateTime(entity.getCreateTime());
                    messageEntity.setModifiedBy(entity.getModifiedBy());
                    messageEntity.setModifiedTime(entity.getModifiedTime());
                    messageEntity.setTitle("项目");
                    messageEntity.setContext("请关注“" + entity.getProjectName() + "”项目进展情况，及时确认产品和服务需求，在SAP中下达！");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 3);
                    messageEntity.setMessageType((short) 1);
                    messageEntity.setProjectId(entity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(userEntity.getId().intValue());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
            if (entity.getProjectStatus() != 3 && source.getProjectStatus() != 3 && !entity.getProjectName().equals(source.getProjectName())) {
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 3, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    if (messageEntity.getContext().contains("项目进展情况")) {
                        messageEntity.setModifiedBy(entity.getModifiedBy());
                        messageEntity.setModifiedTime(entity.getModifiedTime());
                        messageEntity.setContext("请关注“" + entity.getProjectName() + "”项目进展情况，及时确认产品和服务需求，在SAP中下达！");
                        messageService.edit(messageEntity);
                    }
                }
            }


            //变更状态
            if (entity.getChangeStatus() == 3 && source.getChangeStatus() == 2) {
                //消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 3, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    if (messageEntity.getContext().contains("项目变更情况")) {
                        messageEntity.setModifiedBy(entity.getModifiedBy());
                        messageEntity.setModifiedTime(entity.getModifiedTime());
                        messageEntity.setStatus((short) 3);
                        messageService.edit(messageEntity);
                    }
                }
            }
            if (entity.getChangeStatus() != 3 && source.getChangeStatus() == 2) {
                //消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 3, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    if (messageEntity.getContext().contains("项目变更情况")) {
                        messageService.remove(messageEntity.getId());
                    }
                }
            }
            if (entity.getChangeStatus() == 2 && source.getChangeStatus() == 2 && !entity.getProjectName().equals(source.getProjectName())) {
                //消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 3, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    if (messageEntity.getContext().contains("项目变更情况")) {
                        messageEntity.setModifiedBy(entity.getModifiedBy());
                        messageEntity.setModifiedTime(entity.getModifiedTime());
                        messageEntity.setContext("请关注“" + entity.getProjectName() + "”项目变更情况，及时调整需求！");
                        messageService.edit(messageEntity);
                    }
                }
            }
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
        return designLiaisonDao.selectByExample(null, year, projectName, null);
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
            firstTreeNodeUtil.setValue(String.valueOf(year + 100000));
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (DesignLiaisonEntity designLiaisonEntity : designLiaisonEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(designLiaisonEntity.getProjectName());
                secondTreeNodeUtil.setValue(String.valueOf(designLiaisonEntity.getId()));
                Map<String, Object> secondMap = cn.hutool.core.bean.BeanUtil.beanToMap(designLiaisonEntity);
                secondTreeNodeUtil.setA_attr(secondMap);
                secondTreeNodeUtil.setChildren(thirdTreeNodeUtils);
                secondTreeNodeUtils.add(secondTreeNodeUtil);
                TreeNodeUtil thirdTreeNodeUtil1 = new TreeNodeUtil();
                thirdTreeNodeUtil1.setId(3L);
                thirdTreeNodeUtil1.setLabel("输入文件");
                thirdTreeNodeUtil1.setValue(String.valueOf(designLiaisonEntity.getId() + 1000000));
                TreeNodeUtil thirdTreeNodeUtil2 = new TreeNodeUtil();
                thirdTreeNodeUtil2.setId(3L);
                thirdTreeNodeUtil2.setLabel("输出文件");
                thirdTreeNodeUtil2.setValue(String.valueOf(designLiaisonEntity.getId() + 1000001));
                thirdTreeNodeUtils.add(thirdTreeNodeUtil1);
                thirdTreeNodeUtils.add(thirdTreeNodeUtil2);
            }
        }
        return firstTreeNodeUtils;
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "招标投标技术支持-项目列表";
        //excel表名
        String[] headers = {"序号", "项目名称", "项目状态", "项目创建日期", "任务状态", "任务完成时间", "会议状态", "变更状态", "所属路局", "备注"};
        //获取数据
        List<DesignLiaisonEntity> designLiaisonEntities = designLiaisonDao.selectByExample(ids, null, null, null);
        designLiaisonEntities = designLiaisonEntities.stream()
                .sorted(Comparator.comparing(DesignLiaisonEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[designLiaisonEntities.size()][headers.length];
        for (int i = 0; i < designLiaisonEntities.size(); i++) {
            DesignLiaisonEntity designLiaisonEntity = designLiaisonEntities.get(i);
            content[i][0] = designLiaisonEntity.getId().toString();
            content[i][1] = designLiaisonEntity.getProjectName();
            Short projectStatus = designLiaisonEntity.getProjectStatus();
            content[i][2] = projectStatus == 1 ? "未知" : projectStatus == 2 ? "后续招标" : projectStatus == 3 ? "确定采用" : "关闭";
            Short taskStatus = designLiaisonEntity.getTaskStatus();
            content[i][3] = DateUtil.formatDate(designLiaisonEntity.getProjectDate());
            content[i][4] = taskStatus == 1 ? "正在进行" : "已完成";
            content[i][5] = DateUtil.formatDate(designLiaisonEntity.getTaskFinishDate());
            Short meetingStatus = designLiaisonEntity.getMeetingStatus();
            content[i][6] = meetingStatus == 1 ? "不召开会议" : meetingStatus == 2 ? "尚未开会" : "已召开设计联络会";
            Short changeStatus = designLiaisonEntity.getChangeStatus();
            content[i][7] = changeStatus == 1 ? "无变更" : changeStatus == 2 ? "变更设计中" : "变更已定型";
            content[i][8] = designLiaisonEntity.getTieLuJuEntity().getTljName();
            content[i][9] = designLiaisonEntity.getRemark();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public List<Map<String, Object>> findProjectsByProjectStatus() {
        return designLiaisonDao.selectProjectsByProjectStatus((short) 3);
    }
}
