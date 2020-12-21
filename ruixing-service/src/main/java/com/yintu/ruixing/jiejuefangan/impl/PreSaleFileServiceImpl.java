package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.PreSaleDao;
import com.yintu.ruixing.master.jiejuefangan.PreSaleFileDao;
import com.yintu.ruixing.xitongguanli.*;
import com.yintu.ruixing.xitongguanli.impl.AuditConfigurationServiceImpl;
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
 * @date:2020/6/30 18:58
 */
@Service
@Transactional
public class PreSaleFileServiceImpl implements PreSaleFileService {
    @Autowired
    private PreSaleFileDao preSaleFileDao;
    @Autowired
    private PreSaleService preSaleService;
    @Autowired
    private PreSaleFileAuditorService preSaleFileAuditorService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;


    @Override
    public void add(PreSaleFileEntity entity) {
        entity.setUploadDatetime(new Date());
        preSaleFileDao.insertSelective(entity);
    }


    @Override
    public void edit(PreSaleFileEntity entity) {
        preSaleFileDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public void remove(Integer id) {
        preSaleFileDao.deleteByPrimaryKey(id);
        preSaleFileAuditorService.removeByPreSaleFileId(id);
    }

    @Override
    public PreSaleFileEntity findById(Integer id) {
        return preSaleFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        Short type = preSaleFileEntity.getType();//文件类型
        Short releaseStatus = preSaleFileEntity.getReleaseStatus();//发布状态
        Integer currentUserId = preSaleFileEntity.getUserId();//当前文件创建者
        short auditStatus;//审核状态 1.正常 2.审核中 3.已审核通过 4.已审核未通过
        if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
            auditStatus = 2;
            preSaleFileEntity.setAuditStatus(auditStatus);
            this.add(preSaleFileEntity);

            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>();
            if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 1, (short) 1, (short) 1);//查询已经配置好的审核人集
                List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                    if (!currentUserId.equals(auditConfigurationUserEntity.getUserId().intValue())) {//排除当前创建文件用户
                        PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                        preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                        preSaleFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                        Integer sort = auditConfigurationUserEntity.getSort();
                        preSaleFileAuditorEntity.setSort(sort);
                        if (sort == 1) {
                            preSaleFileAuditorEntity.setActivate((short) 1);
                        } else {
                            preSaleFileAuditorEntity.setActivate((short) 0);
                        }
                        preSaleFileAuditorEntity.setIsDispose((short) 0);
                        preSaleFileAuditorEntity.setAuditStatus((short) 2);
                        preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                    }

                }
            } else { //没有提前配置好需要按照前台配好的审批流走
                if (sorts != null && auditorIds.length == sorts.length) {
                    for (int i = 0; i < auditorIds.length; i++) {
                        if (!currentUserId.equals(auditorIds[i].intValue())) {//排除当前创建文件用户
                            PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                            preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                            preSaleFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                            Integer sort = sorts[i];
                            preSaleFileAuditorEntity.setSort(sort);
                            if (sort == 1) {
                                preSaleFileAuditorEntity.setActivate((short) 1);
                            } else {
                                preSaleFileAuditorEntity.setActivate((short) 0);
                            }
                            preSaleFileAuditorEntity.setIsDispose((short) 0);
                            preSaleFileAuditorEntity.setAuditStatus((short) 2);
                            preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                        }

                    }
                }
            }
            if (!preSaleFileAuditorEntities.isEmpty()) {
                preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                //添加审核人消息
                PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());//查询文件所在项目
                if (preSaleEntity != null) {
                    for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {
                        if (!currentUserId.equals(preSaleFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                            if (preSaleFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(preSaleFileEntity.getModifiedBy());
                                messageEntity.setCreateTime(preSaleFileEntity.getModifiedTime());
                                messageEntity.setModifiedBy(preSaleFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(preSaleFileEntity.getModifiedTime());
                                messageEntity.setTitle("文件");
                                messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件需要您审核！");
                                messageEntity.setType((short) 1);
                                messageEntity.setSmallType((short) 1);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                                messageEntity.setFileId(preSaleFileEntity.getId());
                                messageEntity.setSenderId(null);
                                messageEntity.setReceiverId(preSaleFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);
                            }
                        }
                    }
                }

            }


        } else {
            auditStatus = 1;
            preSaleFileEntity.setAuditStatus(auditStatus);
            this.add(preSaleFileEntity);
        }

        //文件日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(preSaleFileEntity.getName())
                .append("   文件状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误")
                .append("    文件审核状态：").append(preSaleFileEntity.getAuditStatus() == 1 ? "待审核" : preSaleFileEntity.getAuditStatus() == 2 ? "审核中" : "错误")
                .append("   备注：").append(preSaleFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, preSaleFileEntity.getId(), sb.toString()));
    }

    @Override
    public void edit(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, Integer[] sorts, String trueName) {
        PreSaleFileEntity psfSource = this.findById(preSaleFileEntity.getId());
        if (psfSource.getReleaseStatus() != 2) {//发布状态不能修改数据
            Short type = preSaleFileEntity.getType();//文件类型
            Short releaseStatus = preSaleFileEntity.getReleaseStatus();//发布状态
            Integer currentUserId = psfSource.getUserId();//当前文件创建者
            short auditStatus;//审核状态 1.待审核 2.审核中 3.已审核通过 4.已审核未通过
            if (type == 2 && releaseStatus == 2) {  //输出发布状态情况下
                auditStatus = 2;
                preSaleFileEntity.setAuditStatus(auditStatus);
                this.edit(preSaleFileEntity);

                List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>();
                if (auditorIds == null || auditorIds.length == 0) {//此审核流程已提前配置好，需要查询配置信息 同步到 当前文件的审核流程
                    List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample((short) 1, (short) 1, (short) 1);//查询已经配置好的审核人集
                    List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationEntities.get(0).getAuditConfigurationUserEntities();
                    for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                        if (!currentUserId.equals(auditConfigurationUserEntity.getUserId().intValue())) {//排除当前创建文件用户
                            PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                            preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                            preSaleFileAuditorEntity.setAuditorId(auditConfigurationUserEntity.getUserId().intValue());
                            Integer sort = auditConfigurationUserEntity.getSort();
                            preSaleFileAuditorEntity.setSort(sort);
                            if (sort == 1) {
                                preSaleFileAuditorEntity.setActivate((short) 1);
                            } else {
                                preSaleFileAuditorEntity.setActivate((short) 0);
                            }
                            preSaleFileAuditorEntity.setIsDispose((short) 0);
                            preSaleFileAuditorEntity.setAuditStatus((short) 2);
                            preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                        }
                    }
                } else { //没有提前配置好需要按照前台配好的审批流走
                    if (sorts != null && auditorIds.length == sorts.length) {
                        for (int i = 0; i < auditorIds.length; i++) {
                            if (!currentUserId.equals(auditorIds[i].intValue())) {//排除当前创建文件用户
                                PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                                preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                                preSaleFileAuditorEntity.setAuditorId(auditorIds[i].intValue());
                                Integer sort = sorts[i];
                                preSaleFileAuditorEntity.setSort(sort);
                                if (sort == 1) {
                                    preSaleFileAuditorEntity.setActivate((short) 1);
                                } else {
                                    preSaleFileAuditorEntity.setActivate((short) 0);
                                }
                                preSaleFileAuditorEntity.setIsDispose((short) 0);
                                preSaleFileAuditorEntity.setAuditStatus((short) 2);
                                preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                            }

                        }
                    }
                }
                if (!preSaleFileAuditorEntities.isEmpty()) {
                    preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                    //添加审核人消息
                    PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());//查询文件所在项目
                    if (preSaleEntity != null) {
                        for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {
                            if (!currentUserId.equals(preSaleFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                                if (preSaleFileAuditorEntity.getSort() == 1) {//从第一批人开始发消息
                                    MessageEntity messageEntity = new MessageEntity();
                                    messageEntity.setCreateBy(preSaleFileEntity.getModifiedBy());
                                    messageEntity.setCreateTime(preSaleFileEntity.getModifiedTime());
                                    messageEntity.setModifiedBy(preSaleFileEntity.getModifiedBy());
                                    messageEntity.setModifiedTime(preSaleFileEntity.getModifiedTime());
                                    messageEntity.setTitle("文件");
                                    messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件需要您审核！");
                                    messageEntity.setType((short) 1);
                                    messageEntity.setSmallType((short) 1);
                                    messageEntity.setMessageType((short) 2);
                                    messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                                    messageEntity.setFileId(preSaleFileEntity.getId());
                                    messageEntity.setSenderId(null);
                                    messageEntity.setReceiverId(preSaleFileAuditorEntity.getAuditorId());
                                    messageEntity.setStatus((short) 1);
                                    messageService.sendMessage(messageEntity);
                                }
                            }
                        }
                    }

                }
            } else {
                auditStatus = 1;
                preSaleFileEntity.setAuditStatus(auditStatus);
                this.edit(preSaleFileEntity);
            }

            //文件日志记录
            PreSaleFileEntity psfTarget = BeanUtil.compareFieldValues(psfSource, preSaleFileEntity, PreSaleFileEntity.class);
            StringBuilder sb = new StringBuilder();
            if (psfTarget.getType() != null) {
                sb.append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误");
            }
            if (psfTarget.getName() != null) {
                sb.append("   文件名：").append(preSaleFileEntity.getName());
            }
            if (psfTarget.getReleaseStatus() != null) {
                sb.append("   文件状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
            }
            if (psfTarget.getAuditStatus() != null) {
                sb.append("    文件审核状态：").append(preSaleFileEntity.getAuditStatus() == 1 ? "待审核" : preSaleFileEntity.getAuditStatus() == 2 ? "审核中" : "错误");
            }
            if (psfTarget.getRemark() != null) {
                sb.append("   备注：").append(preSaleFileEntity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, psfSource.getId(), sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }
    }


    @Override
    public PreSaleFileEntity findPreSaleById(Integer id) {
        PreSaleFileEntity preSaleFileEntity = this.findById(id);
        if (preSaleFileEntity != null) {
            Integer preSaleId = preSaleFileEntity.getPreSaleId();
            if (preSaleId != null) {
                PreSaleEntity preSaleEntity = preSaleService.findById(preSaleId);
                preSaleFileEntity.setPreSaleEntity(preSaleEntity);
            }
            preSaleFileEntity.setPreSaleFileAuditorEntities(preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), null, null, (short) 1));
        }
        return preSaleFileEntity == null ? new PreSaleFileEntity() : preSaleFileEntity;
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<PreSaleFileEntity> findByPreSaleIdAndNameAndType(Integer preSaleId, String name, String
            type, Integer userId) {
        List<PreSaleFileEntity> preSaleFileEntities = preSaleFileDao.selectByCondition(preSaleId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
        for (PreSaleFileEntity preSaleFileEntity : preSaleFileEntities) {
            preSaleFileEntity.setPreSaleFileAuditorEntities(preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), null, null, (short) 1));
        }
        return preSaleFileEntities;
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException {
        //excel标题
        String title = "售前技术支持列表";
        //excel表名
        String[] headers = {"序号", "年份", "项目名称", "任务状态", "项目状态", "文件类型", "文件名称", "文件状态", "审核状态", "备注"};
        //获取数据
        List<PreSaleFileEntity> preSaleFileEntities = preSaleFileDao.selectByCondition(null, ids, null, null, userId, (short) 2);
        preSaleFileEntities = preSaleFileEntities.stream()
                .sorted(Comparator.comparing(PreSaleFileEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[preSaleFileEntities.size()][headers.length];
        for (int i = 0; i < preSaleFileEntities.size(); i++) {
            PreSaleFileEntity preSaleFileEntity = preSaleFileEntities.get(i);
            PreSaleEntity preSaleEntity = preSaleFileEntity.getPreSaleEntity();
            content[i][0] = preSaleFileEntity.getId().toString();
            content[i][1] = Integer.valueOf(preSaleEntity.getProjectDate().getYear() + 1900).toString();
            content[i][2] = preSaleEntity.getProjectName();
            Short taskStatus = preSaleEntity.getTaskStatus();
            content[i][3] = taskStatus.equals((short) 1) ? "正在进行" : taskStatus.equals((short) 2) ? "已完成" : "正在进行";
            Short projectStatus = preSaleEntity.getProjectStatus();
            content[i][4] = projectStatus.equals((short) 1) ? "未知" : projectStatus.equals((short) 2) ? "后续招标" : projectStatus.equals((short) 3) ? "确定采用" : projectStatus.equals((short) 4) ? "关闭" : "未知";
            Short type = preSaleFileEntity.getType();
            content[i][5] = type.equals((short) 1) ? "输入文件" : type.equals((short) 2) ? "输出文件" : "输入文件";
            content[i][6] = preSaleFileEntity.getName();
            content[i][7] = preSaleFileEntity.getReleaseStatus().equals((short) 1) ? "录入" : preSaleFileEntity.getReleaseStatus().equals((short) 2) ? "发布" : "录入";
            content[i][8] = preSaleFileEntity.getAuditStatus() == 1 ? "待审核" : preSaleFileEntity.getAuditStatus() == 2 ? "审核中" : preSaleFileEntity.getAuditStatus() == 3 ? "已审核通过" : "已审核未通过";
            content[i][9] = preSaleFileEntity.getRemark();
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
        PreSaleFileEntity preSaleFileEntity = this.findById(id);
        if (preSaleFileEntity != null) {//判断文件是否存在
            PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
            if (preSaleEntity != null) {//判断项目是否存在
                //查询当前人是否有审核权限
                List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), loginUserId, null, (short) 1);
                if (preSaleFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
                }
                //取出当前审核人
                PreSaleFileAuditorEntity now = preSaleFileAuditorEntities.get(0);

                preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), null, null, (short) 1);
                if (preSaleFileAuditorEntities.isEmpty()) {
                    throw new BaseRuntimeException("此文件已审核，无需重复审核");
                }
                for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                    preSaleFileAuditorEntity.setActivate((short) 0);
                    preSaleFileAuditorEntity.setIsDispose((short) 1);
                    preSaleFileAuditorEntity.setAuditStatus(isPass);
                    preSaleFileAuditorEntity.setAuditFinishTime(new Date());
                    preSaleFileAuditorService.edit(preSaleFileAuditorEntity);
                }
                Integer sort = now.getSort();//取出当前人审核顺序
                //转交时候
                if (isPass == 5) {
                    if (passUserId == null)
                        throw new BaseRuntimeException("转交人id不能为空");
                    now.setActivate((short) 0);
                    now.setIsDispose((short) 1);
                    now.setAuditStatus((short) 5);
                    preSaleFileAuditorService.edit(now);
                    PreSaleFileAuditorEntity p = new PreSaleFileAuditorEntity();
                    p.setPreSaleFileId(id);
                    p.setAuditorId(passUserId);
                    p.setSort(sort);
                    p.setActivate((short) 1);
                    p.setIsDispose((short) 0);
                    p.setAuditStatus((short) 2);
                    preSaleFileAuditorService.add(p);
                    //转交只给这个人发信息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(preSaleFileEntity.getModifiedBy());
                    messageEntity.setCreateTime(preSaleFileEntity.getModifiedTime());
                    messageEntity.setModifiedBy(preSaleFileEntity.getModifiedBy());
                    messageEntity.setModifiedTime(preSaleFileEntity.getModifiedTime());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件需要您审核！");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 1);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                    messageEntity.setFileId(preSaleFileEntity.getId());
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
                preSaleFileAuditorService.edit(now);//记录每次审核人审核回复以及上传的附件

                //判断是否通过 通过继续让下一批人审批，直到所有人审批通过,不通过此次文件审核流程结束。
                if (isPass == 3) {
                    preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), null, sort + 1, null);
                    if (preSaleFileAuditorEntities.isEmpty()) {
                        preSaleFileEntity.setModifiedBy(userName);
                        preSaleFileEntity.setModifiedTime(new Date());
                        preSaleFileEntity.setAuditStatus((short) 3);
                        preSaleFileEntity.setAuditFinishTime(new Date());
                        this.edit(preSaleFileEntity);//更改文件审核状态
                        //文件日志记录
                        StringBuilder sb = new StringBuilder();
                        sb.append("   审核人：").append(trueName)
                                .append("   文件审核状态：").append("已审核已通过");
                        SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, id, sb.toString());
                        solutionLogService.add(solutionLogEntity);
                        //给发起文件审核的人发消息
                        MessageEntity messageEntity = new MessageEntity();
                        messageEntity.setCreateBy(userName);
                        messageEntity.setCreateTime(new Date());
                        messageEntity.setModifiedBy(userName);
                        messageEntity.setModifiedTime(new Date());
                        messageEntity.setTitle("文件");
                        messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件已被审核，请查看结果!");
                        messageEntity.setType((short) 1);
                        messageEntity.setSmallType((short) 1);
                        messageEntity.setMessageType((short) 2);
                        messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                        messageEntity.setFileId(preSaleFileEntity.getId());
                        messageEntity.setSenderId(null);
                        messageEntity.setReceiverId(preSaleFileEntity.getUserId());
                        messageEntity.setStatus((short) 1);
                        messageService.sendMessage(messageEntity);
                    } else {
                        for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {//更改当前顺序的审核人群的激活状态
                            preSaleFileAuditorEntity.setActivate((short) 1);
                            preSaleFileAuditorService.edit(preSaleFileAuditorEntity);
                            Integer currentUserId = preSaleFileEntity.getUserId();
                            if (!currentUserId.equals(preSaleFileAuditorEntity.getAuditorId())) {//排除当前创建文件用户
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(preSaleFileEntity.getModifiedBy());
                                messageEntity.setCreateTime(preSaleFileEntity.getModifiedTime());
                                messageEntity.setModifiedBy(preSaleFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(preSaleFileEntity.getModifiedTime());
                                messageEntity.setTitle("文件");
                                messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件需要您审核！");
                                messageEntity.setType((short) 1);
                                messageEntity.setSmallType((short) 1);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                                messageEntity.setFileId(preSaleFileEntity.getId());
                                messageEntity.setSenderId(null);
                                messageEntity.setReceiverId(preSaleFileAuditorEntity.getAuditorId());
                                messageEntity.setStatus((short) 1);
                                messageService.sendMessage(messageEntity);//给下一批人开始发消息审核
                            }
                        }
                    }

                } else {
                    preSaleFileEntity.setModifiedBy(userName);
                    preSaleFileEntity.setModifiedTime(new Date());
                    preSaleFileEntity.setAuditStatus((short) 4);
                    preSaleFileEntity.setAuditFinishTime(new Date());
                    this.edit(preSaleFileEntity);//更改文件审核状态
                    //文件日志记录
                    StringBuilder sb = new StringBuilder();
                    sb.append("   审核人：").append(trueName)
                            .append("   文件审核状态：").append("已审核未通过");
                    // .append("   理由：").append(reason);
                    SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, id, sb.toString());
                    solutionLogService.add(solutionLogEntity);
                    //给发起文件审核的人发消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(userName);
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy(userName);
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle("文件");
                    messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件已被审核，请查看结果!");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 1);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                    messageEntity.setFileId(preSaleFileEntity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(preSaleFileEntity.getUserId());
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
                messageEntity.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件已被您审核！");
                messageEntity.setType((short) 1);
                messageEntity.setSmallType((short) 1);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(preSaleFileEntity.getPreSaleId());
                messageEntity.setFileId(preSaleFileEntity.getId());
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(loginUserId);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
    }

    @Override
    public List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId) {
        List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByPreSaleFileId(id);
        List<Long> auditorIds = preSaleFileAuditorEntities.stream().map(preSaleFileAuditorEntity -> Long.valueOf(preSaleFileAuditorEntity.getAuditorId())).collect(Collectors.toList());
        auditorIds.add(loginUserId);

        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdNotIn(auditorIds);

        userEntityExample.setOrderByClause("id DESC");
        return userService.findByExample(userEntityExample);
    }

    @Override
    public List<AuditTotalVo> findByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short isDispose) {
        return null;
    }
}
