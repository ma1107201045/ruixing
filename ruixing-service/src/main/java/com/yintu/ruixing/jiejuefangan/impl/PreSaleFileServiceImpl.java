package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.PreSaleFileDao;
import com.yintu.ruixing.xitongguanli.RoleService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserRoleEntity;
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
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoleService roleService;


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
    public void add(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, String trueName) {
        this.add(preSaleFileEntity);
        if (preSaleFileEntity.getReleaseStatus() == 2 && preSaleFileEntity.getType() == 2 && auditorIds != null && auditorIds.length > 0) {
            //添加审核角色
            List<UserEntity> userEntities = roleService.findUsersByIds(auditorIds);
            auditorIds = userEntities.stream()
                    .map(UserEntity::getId)
                    .filter(userId -> userId != preSaleFileEntity.getUserId().longValue())
                    .toArray(Long[]::new);

            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>(userEntities.size());
            for (Long auditorId : auditorIds) {
                if (auditorId != null) {
                    PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                    preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                    preSaleFileAuditorEntity.setAuditorId(auditorId.intValue());
                    preSaleFileAuditorEntity.setIsPass((short) 1);
                    preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                }
            }

            if (preSaleFileAuditorEntities.size() > 0) {
                preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                //添加审核人消息
                PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
                if (preSaleEntity != null) {

                    for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {
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
        //文件日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(preSaleFileEntity.getName())
                .append("   文件状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
        if (preSaleFileEntity.getReleaseStatus() == 2 && preSaleFileEntity.getType() == 2 && auditorIds != null && auditorIds.length > 0) {
            sb.append("   审核人：").append(trueName)
                    .append("   审核状态：").append("待审核");

        }
        sb.append("   备注：").append(preSaleFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, preSaleFileEntity.getId(), sb.toString()));

    }

    @Override
    public void edit(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, String trueName) {
        PreSaleFileEntity psfSource = this.findById(preSaleFileEntity.getId());
        if (psfSource.getReleaseStatus() == 1) {
            this.edit(preSaleFileEntity);
            Short releaseStatus = preSaleFileEntity.getReleaseStatus();
            Short type = preSaleFileEntity.getType();
            if (releaseStatus == 2 && type == 2 && auditorIds != null && auditorIds.length > 0) {

                //添加审核角色
                List<UserEntity> userEntities = roleService.findUsersByIds(auditorIds);
                auditorIds = userEntities.stream()
                        .map(UserEntity::getId)
                        .filter(userId -> userId != psfSource.getUserId().longValue())
                        .toArray(Long[]::new);

                List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>(auditorIds.length);

                for (Long auditorId : auditorIds) {
                    if (auditorId != null) {
                        PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                        preSaleFileAuditorEntity.setPreSaleFileId(preSaleFileEntity.getId());
                        preSaleFileAuditorEntity.setAuditorId(auditorId.intValue());
                        preSaleFileAuditorEntity.setIsPass((short) 1);
                        preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                    }
                }
                if (preSaleFileAuditorEntities.size() > 0) {
                    preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                    //添加审核人消息
                    PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
                    if (preSaleEntity != null) {
                        for (PreSaleFileAuditorEntity preSaleFileAuditorEntity : preSaleFileAuditorEntities) {
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
            if (releaseStatus == 2 && type == 2 && auditorIds != null && auditorIds.length > 0) {
                sb.append("   审核人：").append(trueName)
                        .append("   审核状态：").append("待审核");
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
            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByPreSaleFileId(id);
            preSaleFileEntity.setPreSaleFileAuditorEntities(preSaleFileAuditorEntities);
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
    public List<PreSaleFileEntity> findByPreSaleIdAndNameAndType(Integer preSaleId, String name, String type, Integer userId) {
        return preSaleFileDao.selectByCondition(preSaleId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
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

            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByPreSaleFileId(preSaleFileEntity.getId());

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
            content[i][8] = preSaleFileAuditorEntities.isEmpty() ? "" : preSaleFileAuditorEntities.get(0).getIsPass().equals((short) 1) ? "待审核" :
                    preSaleFileAuditorEntities.get(0).getIsPass().equals((short) 2) ? "已审核未通过" :
                            preSaleFileAuditorEntities.get(0).getIsPass().equals((short) 3) ? "已审核已通过" : "";
            content[i][9] = preSaleFileEntity.getRemark();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void audit(Integer id, Short isPass, String reason, Integer loginUserId, String userName, String trueName) {
        PreSaleFileEntity preSaleFileEntity = this.findById(id);
        if (preSaleFileEntity != null) {
            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), loginUserId, null);
            if (preSaleFileAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("你无权审核此文件");
            }
            preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), null, (short) 0);
            if (preSaleFileAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("此文件已审核，无需重复审核");
            }
            if (isPass == null)
                throw new BaseRuntimeException("审核状态不能为空");
            if (isPass != 2 && isPass != 3) {
                throw new BaseRuntimeException("此文件审核状态有误");
            }
            preSaleFileAuditorEntities = preSaleFileAuditorService.findByExample(preSaleFileEntity.getId(), loginUserId, (short) 0);
            PreSaleFileAuditorEntity preSaleFileAuditorEntity = preSaleFileAuditorEntities.get(0);
            preSaleFileAuditorEntity.setIsPass(isPass);
            preSaleFileAuditorEntity.setReason(isPass == 2 ? reason : null);
            preSaleFileAuditorService.edit(preSaleFileAuditorEntity);

            PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
            if (preSaleEntity != null) {
                //给审核人发审核结果消息
                MessageEntity messageEntity1 = new MessageEntity();
                messageEntity1.setCreateBy(userName);
                messageEntity1.setCreateTime(new Date());
                messageEntity1.setModifiedBy(userName);
                messageEntity1.setModifiedTime(new Date());
                messageEntity1.setTitle("文件");
                messageEntity1.setContext("“" + preSaleEntity.getProjectName() + "”项目中，“" + preSaleFileEntity.getName() + "”文件已被您审核！");
                messageEntity1.setType((short) 1);
                messageEntity1.setSmallType((short) 1);
                messageEntity1.setMessageType((short) 2);
                messageEntity1.setProjectId(preSaleFileEntity.getPreSaleId());
                messageEntity1.setFileId(preSaleFileEntity.getId());
                messageEntity1.setSenderId(null);
                messageEntity1.setReceiverId(preSaleFileAuditorEntity.getAuditorId());
                messageEntity1.setStatus((short) 1);
                messageService.sendMessage(messageEntity1);
                //给被审核人发审核结果消息
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
            //文件日志记录
            StringBuilder sb = new StringBuilder();
            sb.append("   审核人：").append(trueName)
                    .append("   审核状态：").append(isPass == 2 ? "已审核未通过" : "已审核通过");
            if (isPass == 2) {
                sb.append("   理由：").append(reason);
            }
            SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, id, sb.toString());
            solutionLogService.add(solutionLogEntity);
        }

    }

}
