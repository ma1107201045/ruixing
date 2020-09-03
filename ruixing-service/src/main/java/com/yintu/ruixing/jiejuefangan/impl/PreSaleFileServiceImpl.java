package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
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
    private UserService userService;
    @Autowired
    private MessageService messageService;


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
    public void add(PreSaleFileEntity preSaleFileEntity, Integer[] auditorIds, String trueName) {
        this.add(preSaleFileEntity);
        //审核人操作
        Integer id = preSaleFileEntity.getId();
        if (auditorIds != null) {
            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Integer auditorId : auditorIds) {
                if (auditorId != null) {
                    PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                    preSaleFileAuditorEntity.setPreSaleFileId(id);
                    preSaleFileAuditorEntity.setAuditorId(auditorId);
                    preSaleFileAuditorEntity.setIsPass((short) 1);
                    preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                }
            }
            if (preSaleFileAuditorEntities.size() > 0) {
                preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                //给审核人发消息
                PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
                if (preSaleEntity != null) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(preSaleFileEntity.getCreateBy());
                    messageEntity.setCreateTime(preSaleFileEntity.getCreateTime());
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
                    messageEntity.setReceiverId(preSaleFileAuditorEntities.get(0).getAuditorId());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }

        }

        //文件日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(preSaleFileEntity.getName())
                .append("   文件状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
        if (auditorIds != null && auditorIds.length > 0) {
            UserEntity userEntity = userService.findById(auditorIds[0].longValue());
            if (userEntity != null)
                sb.append("   审核人：").append(userEntity.getTrueName())
                        .append("   审核状态：").append("待审核");
        }
        sb.append("   备注：").append(preSaleFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, preSaleFileEntity.getId(), sb.toString()));

    }

    @Override
    public void edit(PreSaleFileEntity preSaleFileEntity, Integer[] auditorIds, String trueName) {
        PreSaleFileEntity psfSource = this.findById(preSaleFileEntity.getId());
        List<PreSaleFileAuditorEntity> psfaSources = preSaleFileAuditorService.findByPreSaleFileId(preSaleFileEntity.getId());
        if (psfSource != null) {
            this.edit(preSaleFileEntity);

            //审核人操作
            Integer id = preSaleFileEntity.getId();
            preSaleFileAuditorService.removeByPreSaleFileId(id);
            if (auditorIds != null) {
                List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = new ArrayList<>(auditorIds.length);
                for (Integer auditorId : auditorIds) {
                    if (auditorId != null) {
                        PreSaleFileAuditorEntity preSaleFileAuditorEntity = new PreSaleFileAuditorEntity();
                        preSaleFileAuditorEntity.setPreSaleFileId(id);
                        preSaleFileAuditorEntity.setAuditorId(auditorId);
                        preSaleFileAuditorEntity.setIsPass((short) 1);
                        preSaleFileAuditorEntities.add(preSaleFileAuditorEntity);
                    }
                }
                if (preSaleFileAuditorEntities.size() > 0) {
                    preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
                    if (psfaSources.isEmpty()) {
                        //添加审核人消息
                        PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
                        if (preSaleEntity != null) {
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
                            messageEntity.setReceiverId(preSaleFileAuditorEntities.get(0).getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageService.sendMessage(messageEntity);
                        }
                    } else {
                        //更新审核人消息
                        MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                                (short) 1, (short) 1, (short) 2, preSaleFileEntity.getPreSaleId(), preSaleFileEntity.getId(), null, null, null, null);
                        List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                        for (MessageEntity messageEntity : messageEntities) {
                            if (!messageEntity.getReceiverId().equals(preSaleFileAuditorEntities.get(0).getAuditorId())) {
                                messageEntity.setModifiedBy(preSaleFileEntity.getModifiedBy());
                                messageEntity.setModifiedTime(preSaleFileEntity.getModifiedTime());
                                messageEntity.setReceiverId(preSaleFileAuditorEntities.get(0).getAuditorId());
                                messageService.edit(messageEntity);
                            }
                        }
                    }
                }
            }
            //删除审核人消息
            if ((auditorIds == null || auditorIds.length == 0) && !psfaSources.isEmpty()) {
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 1, (short) 2, preSaleFileEntity.getPreSaleId(), preSaleFileEntity.getId(), null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    messageService.remove(messageEntity.getId());
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

            if ((auditorIds == null || auditorIds.length == 0) && !psfaSources.isEmpty()) {
                sb.append("   审核人：").append("   审核状态：");
            }

            if (auditorIds != null && auditorIds.length > 0) {
                UserEntity userEntity = userService.findById(auditorIds[0].longValue());
                if (psfaSources.isEmpty() || !auditorIds[0].equals(psfaSources.get(0).getAuditorId())) {
                    sb.append("   审核人：").append(userEntity.getTrueName())
                            .append("   审核状态：").append("待审核");
                } else {
//                    sb.append("   审核人：").append(userEntity.getTrueName())
//                            .append("   审核状态：").append(psfaTarget.getIsPass() == 1 ? "待审核" : psfaTarget.getIsPass() == 2 ? "已审核未通过" : psfaTarget.getIsPass() == 3 ? "已审核未通过" : "错误");
                }
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
        Integer preSaleId = preSaleFileEntity.getPreSaleId();
        if (preSaleId != null) {
            PreSaleEntity preSaleEntity = preSaleService.findById(preSaleId);
            preSaleFileEntity.setPreSaleEntity(preSaleEntity);
        }
        List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByPreSaleFileId(id);
        preSaleFileEntity.setPreSaleFileAuditorEntities(preSaleFileAuditorEntities);
        return preSaleFileEntity;
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
            List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities = preSaleFileAuditorService.findByPreSaleFileId(id);
            if (!preSaleFileAuditorEntities.isEmpty()) {
                PreSaleFileAuditorEntity preSaleFileAuditorEntity = preSaleFileAuditorEntities.get(0);
                if (!preSaleFileAuditorEntity.getAuditorId().equals(loginUserId)) {
                    throw new BaseRuntimeException("您无权审核此文件");
                }
                if (preSaleFileAuditorEntity.getIsPass() != (short) 1) {
                    throw new BaseRuntimeException("此文件已审核，无需重复审核");
                }
                if (isPass == null)
                    throw new BaseRuntimeException("审核状态不能为空");
                if (isPass != 2 && isPass != 3) {
                    throw new BaseRuntimeException("此文件审核状态有误");
                }
                preSaleFileAuditorEntity.setIsPass(isPass);
                preSaleFileAuditorEntity.setReason(isPass == 2 ? reason : null);
                preSaleFileAuditorService.edit(preSaleFileAuditorEntity);
                //给被审核人发消息
                PreSaleEntity preSaleEntity = preSaleService.findById(preSaleFileEntity.getPreSaleId());
                if (preSaleEntity != null) {
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
                        .append("   审核状态：").append(isPass == 2 ? "已审核未通过" : "已审核未通过");
                if (isPass == 2) {
                    sb.append("   理由：").append(reason);
                }
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, id, sb.toString());
                solutionLogService.add(solutionLogEntity);
            }
        }

    }

}
