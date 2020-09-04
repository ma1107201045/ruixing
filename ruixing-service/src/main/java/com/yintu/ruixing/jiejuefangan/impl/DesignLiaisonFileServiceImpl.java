package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/7/3 11:17
 */
@Service
@Transactional
public class DesignLiaisonFileServiceImpl implements DesignLiaisonFileService {

    @Autowired
    private DesignLiaisonFileDao designLiaisonFileDao;

    @Autowired
    private DesignLiaisonService designLiaisonService;
    @Autowired
    private DesignLiaisonFileAuditorService designLiaisonFileAuditorService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Override
    public void add(DesignLiaisonFileEntity entity) {
        entity.setUploadDatetime(new Date());
        designLiaisonFileDao.insertSelective(entity);
    }


    @Override
    public void remove(Integer id) {
        designLiaisonFileDao.deleteByPrimaryKey(id);
        designLiaisonFileAuditorService.removeByDesignLiaisonFileId(id);
    }

    @Override
    public void edit(DesignLiaisonFileEntity entity) {
        designLiaisonFileDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public DesignLiaisonFileEntity findById(Integer id) {
        return designLiaisonFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(DesignLiaisonFileEntity designLiaisonFileEntity, Integer[] auditorIds, String trueName) {
        this.add(designLiaisonFileEntity);
        //审核人操作
        Integer id = designLiaisonFileEntity.getId();
        if (auditorIds != null) {
            List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Integer auditorId : auditorIds) {
                if (auditorId != null) {
                    DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                    designLiaisonFileAuditorEntity.setDesignLiaisonFileId(id);
                    designLiaisonFileAuditorEntity.setAuditorId(auditorId);
                    designLiaisonFileAuditorEntity.setIsPass((short) 1);
                    designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                }
            }
            if (designLiaisonFileAuditorEntities.size() > 0) {
                designLiaisonFileAuditorService.addMuch(designLiaisonFileAuditorEntities);
                DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());
                if (designLiaisonEntity != null) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(designLiaisonFileEntity.getCreateBy());
                    messageEntity.setCreateTime(designLiaisonFileEntity.getCreateTime());
                    messageEntity.setModifiedBy(designLiaisonFileEntity.getModifiedBy());
                    messageEntity.setModifiedTime(designLiaisonFileEntity.getModifiedTime());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + designLiaisonEntity.getProjectName() + "”项目中，“" + designLiaisonFileEntity.getName() + "”文件需要您审核！");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 3);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(designLiaisonFileEntity.getDesignLiaisonId());
                    messageEntity.setFileId(designLiaisonFileEntity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(designLiaisonFileAuditorEntities.get(0).getAuditorId());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
        }
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(designLiaisonFileEntity.getType() == 1 ? "输入文件" : designLiaisonFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(designLiaisonFileEntity.getName())
                .append("   文件状态：").append(designLiaisonFileEntity.getReleaseStatus() == 1 ? "录入" : designLiaisonFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
        if (auditorIds != null && auditorIds.length > 0) {
            UserEntity userEntity = userService.findById(auditorIds[0].longValue());
            if (userEntity != null)
                sb.append("   审核人：").append(userEntity.getTrueName())
                        .append("   审核状态：").append("待审核");
        }
        sb.append("   备注：").append(designLiaisonFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, designLiaisonFileEntity.getId(), sb.toString()));

    }

    @Override
    public void edit(DesignLiaisonFileEntity designLiaisonFileEntity, Integer[] auditorIds, String trueName) {
        DesignLiaisonFileEntity dlfSource = this.findById(designLiaisonFileEntity.getId());
        List<DesignLiaisonFileAuditorEntity> dlfaSources = designLiaisonFileAuditorService.findByDesignLiaisonFileId(designLiaisonFileEntity.getId());
        if (dlfSource != null) {
            this.edit(designLiaisonFileEntity);

            //审核人操作
            Integer id = designLiaisonFileEntity.getId();
            designLiaisonFileAuditorService.removeByDesignLiaisonFileId(id);
            if (auditorIds != null) {
                List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = new ArrayList<>(auditorIds.length);
                for (Integer auditorId : auditorIds) {
                    if (auditorId != null) {
                        DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                        designLiaisonFileAuditorEntity.setDesignLiaisonFileId(id);
                        designLiaisonFileAuditorEntity.setAuditorId(auditorId);
                        designLiaisonFileAuditorEntity.setIsPass((short) 1);
                        designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                    }
                }
                if (designLiaisonFileAuditorEntities.size() > 0) {
                    designLiaisonFileAuditorService.addMuch(designLiaisonFileAuditorEntities);
                    if (dlfaSources.isEmpty()) {
                        //添加审核人消息
                        DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());
                        if (designLiaisonEntity != null) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(designLiaisonFileEntity.getModifiedBy());
                            messageEntity.setCreateTime(designLiaisonFileEntity.getModifiedTime());
                            messageEntity.setModifiedBy(designLiaisonFileEntity.getModifiedBy());
                            messageEntity.setModifiedTime(designLiaisonFileEntity.getModifiedTime());
                            messageEntity.setTitle("文件");
                            messageEntity.setContext("“" + designLiaisonEntity.getProjectName() + "”项目中，“" + designLiaisonFileEntity.getName() + "”文件需要您审核！");
                            messageEntity.setType((short) 1);
                            messageEntity.setSmallType((short) 3);
                            messageEntity.setMessageType((short) 2);
                            messageEntity.setProjectId(designLiaisonFileEntity.getDesignLiaisonId());
                            messageEntity.setFileId(designLiaisonFileEntity.getId());
                            messageEntity.setSenderId(null);
                            messageEntity.setReceiverId(designLiaisonFileAuditorEntities.get(0).getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageService.sendMessage(messageEntity);
                        }
                    } else {
                        //更新审核人消息
                        MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                                (short) 1, (short) 1, (short) 2, designLiaisonFileEntity.getDesignLiaisonId(), designLiaisonFileEntity.getId(), null, null, null, null);
                        List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                        for (MessageEntity messageEntity : messageEntities) {
                            if (!messageEntity.getReceiverId().equals(designLiaisonFileAuditorEntities.get(0).getAuditorId())) {
                                messageEntity.setModifiedBy(designLiaisonFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(designLiaisonFileEntity.getModifiedTime());
                                messageEntity.setReceiverId(designLiaisonFileAuditorEntities.get(0).getAuditorId());
                                messageEntity.setStatus((short)1);
                                messageService.edit(messageEntity);
                            }
                        }
                    }
                }
            }
            //删除审核人消息
            if ((auditorIds == null || auditorIds.length == 0) && !dlfaSources.isEmpty()) {
                //添加审核人消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 1, (short) 2, designLiaisonFileEntity.getDesignLiaisonId(), designLiaisonFileEntity.getId(), null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    messageService.remove(messageEntity.getId());
                }
            }

            //项目日志记录
            DesignLiaisonFileEntity dlfTarget = BeanUtil.compareFieldValues(dlfSource, designLiaisonFileEntity, DesignLiaisonFileEntity.class);
            StringBuilder sb = new StringBuilder();
            if (dlfTarget.getType() != null) {
                sb.append("   文件类型：").append(designLiaisonFileEntity.getType() == 1 ? "输入文件" : designLiaisonFileEntity.getType() == 2 ? "输出文件" : "错误");
            }
            if (dlfTarget.getName() != null) {
                sb.append("   文件名：").append(designLiaisonFileEntity.getName());
            }
            if (dlfTarget.getReleaseStatus() != null) {
                sb.append("   文件状态：").append(designLiaisonFileEntity.getReleaseStatus() == 1 ? "录入" : designLiaisonFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
            }
            if ((auditorIds == null || auditorIds.length == 0) && !dlfaSources.isEmpty()) {
                sb.append("   审核人：").append("   审核状态：");
            }

            if (auditorIds != null && auditorIds.length > 0) {
                UserEntity userEntity = userService.findById(auditorIds[0].longValue());
                if (dlfaSources.isEmpty() || !auditorIds[0].equals(dlfaSources.get(0).getAuditorId())) {
                    sb.append("   审核人：").append(userEntity.getTrueName())
                            .append("   审核状态：").append("待审核");
                } else {
//                    sb.append("   审核人：").append(userEntity.getTrueName())
//                            .append("   审核状态：").append(psfaTarget.getIsPass() == 1 ? "待审核" : psfaTarget.getIsPass() == 2 ? "已审核未通过" : psfaTarget.getIsPass() == 3 ? "已审核未通过" : "错误");
                }
            }
            if (dlfTarget.getRemark() != null) {
                sb.append("   备注：").append(designLiaisonFileEntity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, dlfSource.getId(), sb.toString());
                solutionLogService.add(solutionLogEntity);
            }

        }
    }

    @Override
    public DesignLiaisonFileEntity findDesignLiaisonById(Integer id) {
        DesignLiaisonFileEntity designLiaisonFileEntity = this.findById(id);
        if (designLiaisonFileEntity != null) {
            Integer designLiaisonId = designLiaisonFileEntity.getDesignLiaisonId();
            if (designLiaisonId != null) {
                DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonId);
                designLiaisonFileEntity.setDesignLiaisonEntity(designLiaisonEntity);
            }
            List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByDesignLiaisonFileId(id);
            designLiaisonFileEntity.setDesignLiaisonFileAuditorEntities(designLiaisonFileAuditorEntities);
        }
        return designLiaisonFileEntity == null ? new DesignLiaisonFileEntity() : designLiaisonFileEntity;
    }


    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<DesignLiaisonFileEntity> findByDesignLiaisonIdIdAndNameAndType(Integer designLiaisonId, String name, String type, Integer userId) {
        return designLiaisonFileDao.selectByCondition(designLiaisonId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException {
        //excel标题
        String title = "设计联络及后续技术交流列表";
        //excel表名
        String[] headers = {"序号", "年份", "项目名称", "招标人", "项目状态", "任务状态", "会议状态", "变更状态", "文件类型", "文件名称"};
        //获取数据
        List<DesignLiaisonFileEntity> designLiaisonFileEntities = designLiaisonFileDao.selectByCondition(null, ids, null, null, userId, (short) 2);
        designLiaisonFileEntities = designLiaisonFileEntities.stream()
                .sorted(Comparator.comparing(DesignLiaisonFileEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[designLiaisonFileEntities.size()][headers.length];
        for (int i = 0; i < designLiaisonFileEntities.size(); i++) {
            DesignLiaisonFileEntity designLiaisonFileEntity = designLiaisonFileEntities.get(i);
            DesignLiaisonEntity designLiaisonEntity = designLiaisonFileEntity.getDesignLiaisonEntity();
            content[i][0] = designLiaisonFileEntity.getId().toString();
            content[i][1] = Integer.valueOf(designLiaisonEntity.getProjectDate().getYear() + 1900).toString();
            content[i][2] = designLiaisonEntity.getProjectName();
            content[i][3] = designLiaisonEntity.getBidder();
            Short projectStatus = designLiaisonEntity.getProjectStatus();
            content[i][4] = projectStatus.equals((short) 1) ? "未知" : projectStatus.equals((short) 2) ? "后续招标" : projectStatus.equals((short) 3) ? "确定采用" : projectStatus.equals((short) 4) ? "关闭" : "未知";
            Short taskStatus = designLiaisonEntity.getTaskStatus();
            content[i][5] = taskStatus.equals((short) 1) ? "正在进行" : taskStatus.equals((short) 2) ? "已完成" : "正在进行";
            Short meetingStatus = designLiaisonEntity.getMeetingStatus();
            content[i][6] = meetingStatus.equals((short) 1) ? "不召开会议" : meetingStatus.equals((short) 2) ? "尚未开会" : meetingStatus.equals((short) 3) ? "已召开设计联络会" : "不召开会议";
            Short changeStatus = designLiaisonEntity.getChangeStatus();
            content[i][7] = changeStatus.equals((short) 1) ? "无变更" : changeStatus.equals((short) 2) ? "变更设计中" : changeStatus.equals((short) 3) ? "变更已定型" : "无变更";
            Short type = designLiaisonFileEntity.getType();
            content[i][8] = type.equals((short) 1) ? "输入文件" : type.equals((short) 2) ? "输出文件" : "输入文件";
            content[i][9] = designLiaisonFileEntity.getName();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void audit(Integer id, Short isPass, String reason, Integer loginUserId, String userName, String trueName) {
        DesignLiaisonFileEntity designLiaisonFileEntity = this.findById(id);
        if (designLiaisonFileEntity != null) {
            List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByDesignLiaisonFileId(id);
            if (!designLiaisonFileAuditorEntities.isEmpty()) {
                DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = designLiaisonFileAuditorEntities.get(0);
                if (!designLiaisonFileAuditorEntity.getAuditorId().equals(loginUserId)) {
                    throw new BaseRuntimeException("您无权审核此文件");
                }
                if (designLiaisonFileAuditorEntity.getIsPass() != (short) 1) {
                    throw new BaseRuntimeException("此文件已审核，无需重复审核");
                }
                if (isPass == null)
                    throw new BaseRuntimeException("审核状态不能为空");
                if (isPass != 2 && isPass != 3) {
                    throw new BaseRuntimeException("此文件审核状态有误");
                }
                designLiaisonFileAuditorEntity.setIsPass(isPass);
                designLiaisonFileAuditorEntity.setReason(isPass == 2 ? reason : null);
                designLiaisonFileAuditorService.edit(designLiaisonFileAuditorEntity);

                DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());
                if (designLiaisonEntity != null) {
                    //给审核人发审核结果消息
                    MessageEntity messageEntity1 = new MessageEntity();
                    messageEntity1.setCreateBy(userName);
                    messageEntity1.setCreateTime(new Date());
                    messageEntity1.setModifiedBy(userName);
                    messageEntity1.setModifiedTime(new Date());
                    messageEntity1.setTitle("文件");
                    messageEntity1.setContext("“" + designLiaisonEntity.getProjectName() + "”项目中，“" + designLiaisonFileEntity.getName() + "”文件已被您审核！");
                    messageEntity1.setType((short) 1);
                    messageEntity1.setSmallType((short) 1);
                    messageEntity1.setMessageType((short) 2);
                    messageEntity1.setProjectId(designLiaisonFileEntity.getDesignLiaisonId());
                    messageEntity1.setFileId(designLiaisonFileEntity.getId());
                    messageEntity1.setSenderId(null);
                    messageEntity1.setReceiverId(designLiaisonFileAuditorEntity.getAuditorId());
                    messageEntity1.setStatus((short) 1);
                    messageService.sendMessage(messageEntity1);
                    //给被审核人发消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(userName);
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy(userName);
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + designLiaisonEntity.getProjectName() + "”项目中，“" + designLiaisonFileEntity.getName() + "”文件已被审核，请查看结果!");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 3);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(designLiaisonFileEntity.getDesignLiaisonId());
                    messageEntity.setFileId(designLiaisonFileEntity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(designLiaisonFileEntity.getUserId());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
                //文件日志记录
                StringBuilder sb = new StringBuilder();
                sb.append("   审核人：").append(trueName)
                        .append("   审核状态：").append(isPass == 2 ? "已审核未通过" : "已审核通过");
                if (isPass == 2) {
                    sb.append("   理由：").append(reason);
                }
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, id, sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }
    }

}
