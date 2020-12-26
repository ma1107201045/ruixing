package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.DesignLiaisonFileDao;
import com.yintu.ruixing.xitongguanli.*;
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
    private AuditConfigurationService auditConfigurationService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

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
    public void add(DesignLiaisonFileEntity designLiaisonFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        Short type = designLiaisonFileEntity.getType();//文件类型
        Short releaseStatus = designLiaisonFileEntity.getReleaseStatus();//发布状态
        short auditStatus;//审核状态 1.待审核 2.审核中 3.已审核通过 4.已审核未通过
        if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
            auditStatus = 2;
            designLiaisonFileEntity.setAuditStatus(auditStatus);
            this.add(designLiaisonFileEntity);
            List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = new ArrayList<>();
            if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 3, (short) 1, (short) 1);//查询已经配置好的审核人集
                List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                    DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                    designLiaisonFileAuditorEntity.setDesignLiaisonFileId(designLiaisonFileEntity.getId());
                    designLiaisonFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                    Integer sort = auditConfigurationUserEntity.getSort();
                    designLiaisonFileAuditorEntity.setSort(sort);
                    if (sort == 1) {
                        designLiaisonFileAuditorEntity.setActivate((short) 1);
                    } else {
                        designLiaisonFileAuditorEntity.setActivate((short) 0);
                    }
                    designLiaisonFileAuditorEntity.setIsDispose((short) 0);
                    designLiaisonFileAuditorEntity.setAuditStatus((short) 2);
                    designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                }
            } else { //没有提前配置好需要按照前台配好的审批流走
                if (sorts != null && auditorIds.length == sorts.length) {
                    for (int i = 0; i < auditorIds.length; i++) {
                        DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                        designLiaisonFileAuditorEntity.setDesignLiaisonFileId(designLiaisonFileEntity.getId());
                        designLiaisonFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                        Integer sort = sorts[i];
                        designLiaisonFileAuditorEntity.setSort(sort);
                        if (sort == 1) {
                            designLiaisonFileAuditorEntity.setActivate((short) 1);
                        } else {
                            designLiaisonFileAuditorEntity.setActivate((short) 0);
                        }
                        designLiaisonFileAuditorEntity.setIsDispose((short) 0);
                        designLiaisonFileAuditorEntity.setAuditStatus((short) 2);
                        designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                    }
                }
            }
            if (!designLiaisonFileAuditorEntities.isEmpty()) {
                designLiaisonFileAuditorService.addMuch(designLiaisonFileAuditorEntities);
                //添加审核人消息
                DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());//查询文件所在项目
                if (designLiaisonEntity != null) {
                    for (DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity : designLiaisonFileAuditorEntities) {
                        if (designLiaisonFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
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
                            messageEntity.setReceiverId(designLiaisonFileAuditorEntity.getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageService.sendMessage(messageEntity);
                        }
                    }
                }
            }
        } else {
            auditStatus = 1;
            designLiaisonFileEntity.setAuditStatus(auditStatus);
            this.add(designLiaisonFileEntity);
        }

        //文件日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(designLiaisonFileEntity.getType() == 1 ? "输入文件" : designLiaisonFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(designLiaisonFileEntity.getName())
                .append("   文件状态：").append(designLiaisonFileEntity.getReleaseStatus() == 1 ? "录入" : designLiaisonFileEntity.getReleaseStatus() == 2 ? "发布" : "错误")
                .append("    文件审核状态：").append(designLiaisonFileEntity.getAuditStatus() == 1 ? "待审核" : designLiaisonFileEntity.getAuditStatus() == 2 ? "审核中" : "错误")
                .append("   备注：").append(designLiaisonFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, designLiaisonFileEntity.getId(), sb.toString()));
    }

    @Override
    public void edit(DesignLiaisonFileEntity designLiaisonFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        DesignLiaisonFileEntity dlfSource = this.findById(designLiaisonFileEntity.getId());
        if (dlfSource.getReleaseStatus() != 2) {//发布状态不能修改数据
            Short type = designLiaisonFileEntity.getType();//文件类型
            Short releaseStatus = designLiaisonFileEntity.getReleaseStatus();//发布状态
            short auditStatus;//审核状态 1.待审核 2.审核中 3.已审核通过 4.已审核未通过
            if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
                auditStatus = 2;
                designLiaisonFileEntity.setAuditStatus(auditStatus);
                this.edit(designLiaisonFileEntity);
                List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = new ArrayList<>();
                if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                    List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 3, (short) 1, (short) 1);//查询已经配置好的审核人集
                    List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                    for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                        DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                        designLiaisonFileAuditorEntity.setDesignLiaisonFileId(designLiaisonFileEntity.getId());
                        designLiaisonFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                        Integer sort = auditConfigurationUserEntity.getSort();
                        designLiaisonFileAuditorEntity.setSort(sort);
                        if (sort == 1) {
                            designLiaisonFileAuditorEntity.setActivate((short) 1);
                        } else {
                            designLiaisonFileAuditorEntity.setActivate((short) 0);
                        }
                        designLiaisonFileAuditorEntity.setIsDispose((short) 0);
                        designLiaisonFileAuditorEntity.setAuditStatus((short) 2);
                        designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                    }
                } else { //没有提前配置好需要按照前台配好的审批流走
                    if (sorts != null && auditorIds.length == sorts.length) {
                        for (int i = 0; i < auditorIds.length; i++) {
                            DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity = new DesignLiaisonFileAuditorEntity();
                            designLiaisonFileAuditorEntity.setDesignLiaisonFileId(designLiaisonFileEntity.getId());
                            designLiaisonFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                            Integer sort = sorts[i];
                            designLiaisonFileAuditorEntity.setSort(sort);
                            if (sort == 1) {
                                designLiaisonFileAuditorEntity.setActivate((short) 1);
                            } else {
                                designLiaisonFileAuditorEntity.setActivate((short) 0);
                            }
                            designLiaisonFileAuditorEntity.setIsDispose((short) 0);
                            designLiaisonFileAuditorEntity.setAuditStatus((short) 2);
                            designLiaisonFileAuditorEntities.add(designLiaisonFileAuditorEntity);
                        }
                    }
                }
                if (!designLiaisonFileAuditorEntities.isEmpty()) {
                    designLiaisonFileAuditorService.addMuch(designLiaisonFileAuditorEntities);
                    //添加审核人消息
                    DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());//查询文件所在项目
                    if (designLiaisonEntity != null) {
                        for (DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity : designLiaisonFileAuditorEntities) {
                            if (designLiaisonFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
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
                                messageEntity.setReceiverId(designLiaisonFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);
                            }
                        }
                    }

                }
            } else {
                auditStatus = 1;
                designLiaisonFileEntity.setAuditStatus(auditStatus);
                this.edit(designLiaisonFileEntity);
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
            if (dlfTarget.getAuditStatus() != null) {
                sb.append("    文件审核状态：").append(designLiaisonFileEntity.getAuditStatus() == 1 ? "待审核" : designLiaisonFileEntity.getAuditStatus() == 2 ? "审核中" : "错误");
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
            List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByExample(designLiaisonFileEntity.getId(), null, null, (short) 1);
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
        List<DesignLiaisonFileEntity> designLiaisonFileEntities = designLiaisonFileDao.selectByCondition(designLiaisonId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
        for (DesignLiaisonFileEntity designLiaisonFileEntity : designLiaisonFileEntities) {
            designLiaisonFileEntity.setDesignLiaisonFileAuditorEntities(designLiaisonFileAuditorService.findByExample(designLiaisonFileEntity.getId(), null, null, (short) 1));
        }
        return designLiaisonFileEntities;
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
    public void audit(Integer id, Short isPass, String context, String accessoryName, String accessoryPath, Integer passUserId, Integer loginUserId, String userName, String trueName) {
        if (isPass == null)
            throw new BaseRuntimeException("审核状态不能为空");
        if (isPass != 3 && isPass != 4 && isPass != 5) {
            throw new BaseRuntimeException("此文件审核状态有误");
        }
        DesignLiaisonFileEntity designLiaisonFileEntity = this.findById(id);
        if (designLiaisonFileEntity != null) {//判断文件是否存在
            DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(designLiaisonFileEntity.getDesignLiaisonId());
            if (designLiaisonEntity != null) {//判断项目是否存在
                //查询当前人是否有审核权限
                List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByExample(designLiaisonFileEntity.getId(), loginUserId, null, (short) 1);
                if (designLiaisonFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
                }
                //取出当前审核人
                DesignLiaisonFileAuditorEntity now = designLiaisonFileAuditorEntities.get(0);

                designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByExample(designLiaisonFileEntity.getId(), null, null, (short) 1);
                if (designLiaisonFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("此文件已审核，无需重复审核");
                }

                for (DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity : designLiaisonFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                    designLiaisonFileAuditorEntity.setActivate((short) 0);
                    designLiaisonFileAuditorEntity.setIsDispose((short) 1);
                    designLiaisonFileAuditorEntity.setAuditStatus(isPass);
                    designLiaisonFileAuditorEntity.setAuditFinishTime(new Date());
                    designLiaisonFileAuditorService.edit(designLiaisonFileAuditorEntity);
                }
                Integer sort = now.getSort();//取出当前人审核顺序
                //转交时候
                if (isPass == 5) {
                    if (passUserId == null)
                        throw new BaseRuntimeException("转交人id不能为空");
                    now.setActivate((short) 0);
                    now.setIsDispose((short) 1);
                    now.setAuditStatus((short) 5);
                    designLiaisonFileAuditorService.edit(now);

                    DesignLiaisonFileAuditorEntity d = new DesignLiaisonFileAuditorEntity();
                    d.setDesignLiaisonFileId(id);
                    d.setAuditorId(passUserId);
                    d.setSort(sort);
                    d.setActivate((short) 1);
                    d.setIsDispose((short) 0);
                    d.setAuditStatus((short) 2);
                    designLiaisonFileAuditorService.add(d);
                    //转交只给这个人发信息
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
                    messageEntity.setReceiverId(passUserId);
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                    return;
                }
                now.setActivate((short) 0);
                now.setIsDispose((short) 1);
                now.setAuditStatus(isPass);
                now.setAuditFinishTime(new Date());
                now.setContext(context);
                now.setAccessoryName(accessoryName);
                now.setAccessoryPath(accessoryPath);
                designLiaisonFileAuditorService.edit(now);//记录每次审核人审核回复以及上传的附件


                //判断是否通过 通过继续让下一批人审批，直到所有人审批通过,不通过此次文件审核流程结束。
                if (isPass == 3) {
                    designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByExample(designLiaisonFileEntity.getId(), null, sort + 1, null);
                    if (designLiaisonFileAuditorEntities.isEmpty()) {
                        designLiaisonFileEntity.setModifiedBy(userName);
                        designLiaisonFileEntity.setModifiedTime(new Date());
                        designLiaisonFileEntity.setAuditStatus((short) 3);
                        designLiaisonFileEntity.setAuditFinishTime(new Date());
                        this.edit(designLiaisonFileEntity);//更改文件审核状态
                        //文件日志记录
                        StringBuilder sb = new StringBuilder();
                        sb.append("   审核人：").append(trueName)
                                .append("   文件审核状态：").append("已审核已通过");
                        SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, id, sb.toString());
                        solutionLogService.add(solutionLogEntity);
                        //给发起文件审核的人发消息
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
                    } else {
                        for (DesignLiaisonFileAuditorEntity designLiaisonFileAuditorEntity : designLiaisonFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                            designLiaisonFileAuditorEntity.setActivate((short) 1);
                            designLiaisonFileAuditorService.edit(designLiaisonFileAuditorEntity);
                            Integer currentUserId = designLiaisonFileEntity.getUserId();
                            if (!currentUserId.equals(designLiaisonFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
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
                                messageEntity.setReceiverId(designLiaisonFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);//给下一批人开始发消息审核
                            }
                        }
                    }

                } else {
                    designLiaisonFileEntity.setModifiedBy(userName);
                    designLiaisonFileEntity.setModifiedTime(new Date());
                    designLiaisonFileEntity.setAuditStatus((short) 4);
                    designLiaisonFileEntity.setAuditFinishTime(new Date());
                    this.edit(designLiaisonFileEntity);//更改文件审核状态
                    //文件日志记录
                    StringBuilder sb = new StringBuilder();
                    sb.append("   审核人：").append(trueName)
                            .append("   文件审核状态：").append("已审核未通过");
//                            .append("   理由：").append(reason);
                    SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 3, (short) 2, id, sb.toString());
                    solutionLogService.add(solutionLogEntity);
                    //给发起文件审核的人发消息
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
                //给审核人发审核结果消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(userName);
                messageEntity.setCreateTime(new Date());
                messageEntity.setModifiedBy(userName);
                messageEntity.setModifiedTime(new Date());
                messageEntity.setTitle("文件");
                messageEntity.setContext("“" + designLiaisonEntity.getProjectName() + "”项目中，“" + designLiaisonFileEntity.getName() + "”文件已被您审核！");
                messageEntity.setType((short) 1);
                messageEntity.setSmallType((short) 3);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(designLiaisonFileEntity.getDesignLiaisonId());
                messageEntity.setFileId(designLiaisonFileEntity.getId());
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(loginUserId);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
    }

    @Override
    public List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId) {
        DesignLiaisonFileEntity designLiaisonFileEntity = this.findById(id);
        if (designLiaisonFileEntity == null)
            throw new BaseRuntimeException("审核内容不存在");
        List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities = designLiaisonFileAuditorService.findByDesignLiaisonFileId(id);
        List<Long> auditorIds = designLiaisonFileAuditorEntities.stream().map(designLiaisonFileAuditorEntity -> Long.valueOf(designLiaisonFileAuditorEntity.getAuditorId())).collect(Collectors.toList());
        auditorIds.add(loginUserId);
        auditorIds.add(designLiaisonFileEntity.getUserId().longValue());
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdNotIn(auditorIds);

        userEntityExample.setOrderByClause("id DESC");
        return userService.findByExample(userEntityExample);
    }

    @Override
    public List<AuditTotalVo> findByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose) {
        return designLiaisonFileDao.selectByExample(search, userId, auditStatus, auditorId, activate, isDispose);
    }
}
