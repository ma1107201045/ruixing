package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.BiddingFileDao;
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
 * @date:2020/7/2 14:25
 */
@Service
@Transactional
public class BiddingFileServiceImpl implements BiddingFileService {

    @Autowired
    private BiddingFileDao biddingFileDao;
    @Autowired
    private BiddingService biddingService;
    @Autowired
    private BiddingFileAuditorService biddingFileAuditorService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void add(BiddingFileEntity entity) {
        entity.setUploadDatetime(new Date());
        biddingFileDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        biddingFileDao.deleteByPrimaryKey(id);
        biddingFileAuditorService.removeByBiddingFileId(id);
    }

    @Override
    public void edit(BiddingFileEntity entity) {
        biddingFileDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public BiddingFileEntity findById(Integer id) {
        return biddingFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(BiddingFileEntity biddingFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        Short type = biddingFileEntity.getType();//文件类型
        Short releaseStatus = biddingFileEntity.getReleaseStatus();//发布状态
        Integer currentUserId = biddingFileEntity.getUserId();//当前文件创建者
        short auditStatus;//审核状态 1.待审核 2.审核中 3.已审核通过 4.已审核未通过
        if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
            auditStatus = 2;
            biddingFileEntity.setAuditStatus(auditStatus);
            this.add(biddingFileEntity);

            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>();
            if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 2, (short) 1, (short) 1);//查询已经配置好的审核人集
                List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                    if (!currentUserId.equals(auditConfigurationUserEntity.getUserId().intValue())) {//排除当前创建文件用户
                        BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                        biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                        biddingFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                        Integer sort = auditConfigurationUserEntity.getSort();
                        biddingFileAuditorEntity.setSort(sort);
                        if (sort == 1) {
                            biddingFileAuditorEntity.setActivate((short) 1);
                        } else {
                            biddingFileAuditorEntity.setActivate((short) 0);
                        }
                        biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                    }

                }
            } else { //没有提前配置好需要按照前台配好的审批流走
                if (sorts != null && auditorIds.length == sorts.length) {
                    for (int i = 0; i < auditorIds.length; i++) {
                        if (!currentUserId.equals(auditorIds[i].intValue())) {//排除当前创建文件用户
                            BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                            biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                            biddingFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                            Integer sort = sorts[i];
                            biddingFileAuditorEntity.setSort(sort);
                            if (sort == 1) {
                                biddingFileAuditorEntity.setActivate((short) 1);
                            } else {
                                biddingFileAuditorEntity.setActivate((short) 0);
                            }
                            biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                        }

                    }
                }
            }
            if (!biddingFileAuditorEntities.isEmpty()) {
                biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
                //添加审核人消息
                BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());//查询文件所在项目
                if (biddingEntity != null) {
                    for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {
                        if (!currentUserId.equals(biddingFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                            if (biddingFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(biddingFileEntity.getModifiedBy());
                                messageEntity.setCreateTime(biddingFileEntity.getModifiedTime());
                                messageEntity.setModifiedBy(biddingFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(biddingFileEntity.getModifiedTime());
                                messageEntity.setTitle("文件");
                                messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件需要您审核！");
                                messageEntity.setType((short) 1);
                                messageEntity.setSmallType((short) 2);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                                messageEntity.setFileId(biddingFileEntity.getId());
                                messageEntity.setSenderId(null);
                                messageEntity.setReceiverId(biddingFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);
                            }
                        }
                    }
                }

            }
        } else {
            auditStatus = 1;
            biddingFileEntity.setAuditStatus(auditStatus);
            this.add(biddingFileEntity);
        }

        //文件日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(biddingFileEntity.getType() == 1 ? "输入文件" : biddingFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(biddingFileEntity.getName())
                .append("   文件状态：").append(biddingFileEntity.getReleaseStatus() == 1 ? "录入" : biddingFileEntity.getReleaseStatus() == 2 ? "发布" : "错误")
                .append("    文件审核状态：").append(biddingFileEntity.getAuditStatus() == 1 ? "待审核" : biddingFileEntity.getAuditStatus() == 2 ? "审核中" : "错误")
                .append("   备注：").append(biddingFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, biddingFileEntity.getId(), sb.toString()));
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(BiddingFileEntity biddingFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        BiddingFileEntity bfSource = this.findById(biddingFileEntity.getId());
        if (bfSource.getReleaseStatus() != 2) {//发布状态不能修改数据
            Short type = biddingFileEntity.getType();//文件类型
            Short releaseStatus = biddingFileEntity.getReleaseStatus();//发布状态
            Integer currentUserId = bfSource.getUserId();//当前文件创建者
            short auditStatus;//审核状态 1.待审核 2.审核中 3.已审核通过 4.已审核未通过
            if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
                auditStatus = 2;
                biddingFileEntity.setAuditStatus(auditStatus);
                this.edit(biddingFileEntity);

                List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>();
                if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                    List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 2, (short) 1, (short) 1);//查询已经配置好的审核人集
                    List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                    for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                        if (!currentUserId.equals(auditConfigurationUserEntity.getUserId().intValue())) {//排除当前创建文件用户
                            BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                            biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                            biddingFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                            Integer sort = auditConfigurationUserEntity.getSort();
                            auditConfigurationUserEntity.setSort(sort);
                            if (sort == 1) {
                                biddingFileAuditorEntity.setActivate((short) 1);
                            } else {
                                biddingFileAuditorEntity.setActivate((short) 0);
                            }
                            biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                        }

                    }
                } else { //没有提前配置好需要按照前台配好的审批流走
                    if (sorts != null && auditorIds.length == sorts.length) {
                        for (int i = 0; i < auditorIds.length; i++) {
                            if (!currentUserId.equals(auditorIds[i].intValue())) {//排除当前创建文件用户
                                BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                                biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                                biddingFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                                Integer sort = sorts[i];
                                biddingFileAuditorEntity.setSort(sort);
                                if (sort == 1) {
                                    biddingFileAuditorEntity.setActivate((short) 1);
                                } else {
                                    biddingFileAuditorEntity.setActivate((short) 0);
                                }
                                biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                            }

                        }
                    }
                }
                if (!biddingFileAuditorEntities.isEmpty()) {
                    biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
                    //添加审核人消息
                    BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());//查询文件所在项目
                    if (biddingEntity != null) {
                        for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {
                            if (!currentUserId.equals(biddingFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                                if (biddingFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
                                    MessageEntity messageEntity = new MessageEntity();
                                    messageEntity.setCreateBy(biddingFileEntity.getModifiedBy());
                                    messageEntity.setCreateTime(biddingFileEntity.getModifiedTime());
                                    messageEntity.setModifiedBy(biddingFileEntity.getModifiedBy());
                                    messageEntity.setModifiedTime(biddingFileEntity.getModifiedTime());
                                    messageEntity.setTitle("文件");
                                    messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件需要您审核！");
                                    messageEntity.setType((short) 1);
                                    messageEntity.setSmallType((short) 2);
                                    messageEntity.setMessageType((short) 2);
                                    messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                                    messageEntity.setFileId(biddingFileEntity.getId());
                                    messageEntity.setSenderId(null);
                                    messageEntity.setReceiverId(biddingFileAuditorEntity.getAuditorId());
                                    messageEntity.setStatus((short) 1);
                                    messageService.sendMessage(messageEntity);
                                }
                            }
                        }
                    }

                }
            } else {
                auditStatus = 1;
                biddingFileEntity.setAuditStatus(auditStatus);
                this.edit(biddingFileEntity);
            }

            //文件日志记录
            BiddingFileEntity bfTarget = BeanUtil.compareFieldValues(bfSource, biddingFileEntity, BiddingFileEntity.class);
            StringBuilder sb = new StringBuilder();
            if (bfTarget.getType() != null) {
                sb.append("   文件类型：").append(biddingFileEntity.getType() == 1 ? "输入文件" : biddingFileEntity.getType() == 2 ? "输出文件" : "错误");
            }
            if (bfTarget.getName() != null) {
                sb.append("   文件名：").append(biddingFileEntity.getName());
            }
            if (bfTarget.getReleaseStatus() != null) {
                sb.append("   文件状态：").append(biddingFileEntity.getReleaseStatus() == 1 ? "录入" : biddingFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
            }
            if (bfTarget.getAuditStatus() != null) {
                sb.append("    文件审核状态：").append(biddingFileEntity.getAuditStatus() == 1 ? "待审核" : biddingFileEntity.getAuditStatus() == 2 ? "审核中" : "错误");
            }
            if (bfTarget.getRemark() != null) {
                sb.append("   备注：").append(biddingFileEntity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, bfSource.getId(), sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }
    }

    @Override
    public BiddingFileEntity findBiddingById(Integer id) {
        BiddingFileEntity biddingFileEntity = this.findById(id);
        if (biddingFileEntity != null) {
            Integer biddingId = biddingFileEntity.getBiddingId();
            if (biddingId != null) {
                BiddingEntity biddingEntity = biddingService.findById(biddingId);
                biddingFileEntity.setBiddingEntity(biddingEntity);
            }
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), null, null, (short) 1);
            biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorEntities);
        }
        return biddingFileEntity == null ? new BiddingFileEntity() : biddingFileEntity;
    }

    @Override
    public List<BiddingFileEntity> findByBiddingIdAndNameAndType(Integer biddingId, String name, String type, Integer userId) {
        List<BiddingFileEntity> biddingFileEntities = biddingFileDao.selectByCondition(biddingId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
        for (BiddingFileEntity biddingFileEntity : biddingFileEntities) {
            biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorService.findByExample(biddingFileEntity.getId(), null, null, (short) 1));
        }
        return biddingFileEntities;
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException {
        //excel标题
        String title = "投招标技术支持列表";
        //excel表名
        String[] headers = {"序号", "年份", "项目名称", "招标人", "项目状态", "任务状态", "文件类型", "文件名称"};
        //获取数据
        List<BiddingFileEntity> biddingFileEntities = biddingFileDao.selectByCondition(null, ids, null, null, userId, (short) 2);
        biddingFileEntities = biddingFileEntities.stream()
                .sorted(Comparator.comparing(BiddingFileEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[biddingFileEntities.size()][headers.length];
        for (int i = 0; i < biddingFileEntities.size(); i++) {
            BiddingFileEntity biddingFileEntity = biddingFileEntities.get(i);
            BiddingEntity biddingEntity = biddingFileEntity.getBiddingEntity();
            content[i][0] = biddingFileEntity.getId().toString();
            content[i][1] = Integer.valueOf(biddingEntity.getProjectDate().getYear() + 1900).toString();
            content[i][2] = biddingEntity.getProjectName();
            content[i][3] = biddingEntity.getBidder();
            Short projectStatus = biddingEntity.getProjectStatus();
            content[i][4] = projectStatus.equals((short) 1) ? "未知" : projectStatus.equals((short) 2) ? "后续招标" : projectStatus.equals((short) 3) ? "确定采用" : projectStatus.equals((short) 4) ? "关闭" : "未知";
            Short taskStatus = biddingEntity.getTaskStatus();
            content[i][5] = taskStatus.equals((short) 1) ? "正在进行" : taskStatus.equals((short) 2) ? "已完成" : "正在进行";
            Short type = biddingFileEntity.getType();
            content[i][6] = type.equals((short) 1) ? "输入文件" : type.equals((short) 2) ? "输出文件" : "输入文件";
            content[i][7] = biddingFileEntity.getName();
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
        BiddingFileEntity biddingFileEntity = this.findById(id);
        if (biddingFileEntity != null) {//判断文件是否存在
            BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());
            if (biddingEntity != null) {//判断项目是否存在
                //查询当前人是否有审核权限
                List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), loginUserId, null, (short) 1);
                if (biddingFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
                }
                //取出当前审核人
                BiddingFileAuditorEntity now = biddingFileAuditorEntities.get(0);
                biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), null, null, (short) 1);
                if (biddingFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("此文件已审核，无需重复审核");
                }

                for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                    biddingFileAuditorEntity.setActivate((short) 0);
                    biddingFileAuditorService.edit(biddingFileAuditorEntity);
                }
                Integer sort = biddingFileAuditorEntities.get(0).getSort();//取出里边的任意顺序，进行下一批人审批
                //转交时候
                if (isPass == 5) {
                    if (passUserId == null)
                        throw new BaseRuntimeException("转交人id不能为空");
                    BiddingFileAuditorEntity b = new BiddingFileAuditorEntity();
                    b.setBiddingFileId(id);
                    b.setAuditorId(passUserId);
                    b.setSort(sort);
                    b.setActivate((short) 1);
                    biddingFileAuditorService.add(b);
                    //转交只给这个人发信息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(biddingFileEntity.getModifiedBy());
                    messageEntity.setCreateTime(biddingFileEntity.getModifiedTime());
                    messageEntity.setModifiedBy(biddingFileEntity.getModifiedBy());
                    messageEntity.setModifiedTime(biddingFileEntity.getModifiedTime());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件需要您审核！");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 1);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                    messageEntity.setFileId(biddingFileEntity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(passUserId);
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                    return;
                }

                now.setContext(context);
                now.setAccessoryName(accessoryName);
                now.setAccessoryPath(accessoryPath);
                biddingFileAuditorService.add(now);//记录每次审核人审核回复以及上传的附件

                //判断是否通过 通过继续让下一批人审批，直到所有人审批通过,不通过此次文件审核流程结束。
                if (isPass == 3) {

                    biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), null, sort + 1, null);
                    if (biddingFileAuditorEntities.isEmpty()) {
                        biddingFileEntity.setModifiedBy(userName);
                        biddingFileEntity.setModifiedTime(new Date());
                        biddingFileEntity.setAuditStatus((short) 3);
                        this.edit(biddingFileEntity);//更改文件审核状态
                        //文件日志记录
                        StringBuilder sb = new StringBuilder();
                        sb.append("   审核人：").append(trueName)
                                .append("   文件审核状态：").append("已审核已通过");
                        SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, id, sb.toString());
                        solutionLogService.add(solutionLogEntity);
                        //给发起文件审核的人发消息
                        MessageEntity messageEntity = new MessageEntity();
                        messageEntity.setCreateBy(userName);
                        messageEntity.setCreateTime(new Date());
                        messageEntity.setModifiedBy(userName);
                        messageEntity.setModifiedTime(new Date());
                        messageEntity.setTitle("文件");
                        messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件已被审核，请查看结果!");
                        messageEntity.setType((short) 1);
                        messageEntity.setSmallType((short) 2);
                        messageEntity.setMessageType((short) 2);
                        messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                        messageEntity.setFileId(biddingFileEntity.getId());
                        messageEntity.setSenderId(null);
                        messageEntity.setReceiverId(biddingFileEntity.getUserId());
                        messageEntity.setStatus((short) 1);
                        messageService.sendMessage(messageEntity);
                    } else {
                        for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                            biddingFileAuditorEntity.setActivate((short) 1);
                            biddingFileAuditorService.edit(biddingFileAuditorEntity);
                            Integer currentUserId = biddingFileEntity.getUserId();
                            if (!currentUserId.equals(biddingFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(biddingFileEntity.getModifiedBy());
                                messageEntity.setCreateTime(biddingFileEntity.getModifiedTime());
                                messageEntity.setModifiedBy(biddingFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(biddingFileEntity.getModifiedTime());
                                messageEntity.setTitle("文件");
                                messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件需要您审核！");
                                messageEntity.setType((short) 1);
                                messageEntity.setSmallType((short) 2);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                                messageEntity.setFileId(biddingFileEntity.getId());
                                messageEntity.setSenderId(null);
                                messageEntity.setReceiverId(biddingFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);//给下一批人开始发消息审核
                            }
                        }
                    }

                } else {
                    biddingFileEntity.setModifiedBy(userName);
                    biddingFileEntity.setModifiedTime(new Date());
                    biddingFileEntity.setAuditStatus((short) 4);
                    //biddingFileEntity.setReason(reason);
                    this.edit(biddingFileEntity);//更改文件审核状态
                    //文件日志记录
                    StringBuilder sb = new StringBuilder();
                    sb.append("   审核人：").append(trueName)
                            .append("   文件审核状态：").append("已审核未通过");
                    // .append("   理由：").append(reason);
                    SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, id, sb.toString());
                    solutionLogService.add(solutionLogEntity);
                    //给发起文件审核的人发消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(userName);
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy(userName);
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件已被审核，请查看结果!");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 2);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                    messageEntity.setFileId(biddingFileEntity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(biddingFileEntity.getUserId());
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
                messageEntity.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件已被您审核！");
                messageEntity.setType((short) 1);
                messageEntity.setSmallType((short) 2);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(biddingFileEntity.getBiddingId());
                messageEntity.setFileId(biddingFileEntity.getId());
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(loginUserId);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
    }

    @Override
    public List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId) {
        List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByBiddingFileIdId(id);
        List<Long> auditorIds = biddingFileAuditorEntities.stream().map(biddingFileAuditorEntity -> Long.valueOf(biddingFileAuditorEntity.getAuditorId())).collect(Collectors.toList());
        auditorIds.add(loginUserId);

        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdNotIn(auditorIds);
        return userService.findByExample(userEntityExample);
    }
}
