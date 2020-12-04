package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.BiddingFileDao;
import com.yintu.ruixing.xitongguanli.RoleService;
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
    private SolutionLogService solutionLogService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private RoleService roleService;

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
    public void add(BiddingFileEntity biddingFileEntity, Long[] auditorIds, String trueName) {
        this.add(biddingFileEntity);
        if (biddingFileEntity.getReleaseStatus() == 2 && biddingFileEntity.getType() == 2 && auditorIds != null && auditorIds.length > 0) {
            //添加审核角色
            List<UserEntity> userEntities = roleService.findUsersByIds(auditorIds);
            auditorIds = userEntities.stream()
                    .map(UserEntity::getId)
                    .filter(userId -> userId != biddingFileEntity.getUserId().longValue())
                    .toArray(Long[]::new);

            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Long auditorId : auditorIds) {
                if (auditorId != null) {
                    BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                    biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                    biddingFileAuditorEntity.setAuditorId(auditorId.intValue());
                    biddingFileAuditorEntity.setIsPass((short) 1);
                    biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                }
            }
            if (biddingFileAuditorEntities.size() > 0) {
                biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
                //添加审核人消息
                BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());
                if (biddingEntity != null) {
                    for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {
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

        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(biddingFileEntity.getType() == 1 ? "输入文件" : biddingFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(biddingFileEntity.getName())
                .append("   文件状态：").append(biddingFileEntity.getReleaseStatus() == 1 ? "录入" : biddingFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
        if (biddingFileEntity.getReleaseStatus() == 2 && biddingFileEntity.getType() == 2 && auditorIds != null && auditorIds.length > 0) {
            sb.append("   审核人：").append(trueName)
                    .append("   审核状态：").append("待审核");
        }
        sb.append("   备注：").append(biddingFileEntity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, biddingFileEntity.getId(), sb.toString()));

    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(BiddingFileEntity biddingFileEntity, Long[] auditorIds, String trueName) {
        BiddingFileEntity bfSource = this.findById(biddingFileEntity.getId());
        if (bfSource.getReleaseStatus() == 1) {
            this.edit(biddingFileEntity);
            Short releaseStatus = biddingFileEntity.getReleaseStatus();
            Short type = biddingFileEntity.getType();
            if (releaseStatus == 2 && type == 2 && auditorIds != null && auditorIds.length > 0) {
                //添加审核角色
                List<UserEntity> userEntities = roleService.findUsersByIds(auditorIds);
                auditorIds = userEntities.stream()
                        .map(UserEntity::getId)
                        .filter(userId -> userId != bfSource.getUserId().longValue())
                        .toArray(Long[]::new);

                List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
                for (Long auditorId : auditorIds) {
                    if (auditorId != null) {
                        BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                        biddingFileAuditorEntity.setBiddingFileId(biddingFileEntity.getId());
                        biddingFileAuditorEntity.setAuditorId(auditorId.intValue());
                        biddingFileAuditorEntity.setIsPass((short) 1);
                        biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                    }
                }
                if (biddingFileAuditorEntities.size() > 0) {
                    biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
                    //添加审核人消息
                    BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());
                    if (biddingEntity != null) {
                        for (BiddingFileAuditorEntity biddingFileAuditorEntity : biddingFileAuditorEntities) {
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

            //项目日志记录
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
            if (releaseStatus == 2 && type == 2 && auditorIds != null && auditorIds.length > 0) {
                sb.append("   审核人：").append(trueName)
                        .append("   审核状态：").append("待审核");
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
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByBiddingFileIdId(id);
            biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorEntities);
        }
        return biddingFileEntity == null ? new BiddingFileEntity() : biddingFileEntity;
    }

    @Override
    public List<BiddingFileEntity> findByBiddingIdAndNameAndType(Integer biddingId, String name, String type, Integer userId) {
        List<BiddingFileEntity> biddingFileEntities = biddingFileDao.selectByCondition(biddingId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
        for (BiddingFileEntity biddingFileEntity : biddingFileEntities) {
            biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorService.findByBiddingFileIdId(biddingFileEntity.getId()));
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
    public void audit(Integer id, Short isPass, String reason, Integer loginUserId, String userName, String trueName) {
        BiddingFileEntity biddingFileEntity = this.findById(id);
        if (biddingFileEntity != null) {
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), loginUserId, null);
            if (biddingFileAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("你无权审核此文件");
            }
            biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), null, (short) 0);
            if (biddingFileAuditorEntities.isEmpty()) {
                throw new BaseRuntimeException("此文件已审核，无需重复审核");
            }
            if (isPass == null)
                throw new BaseRuntimeException("审核状态不能为空");
            if (isPass != 2 && isPass != 3) {
                throw new BaseRuntimeException("此文件审核状态有误");
            }
            biddingFileAuditorEntities = biddingFileAuditorService.findByExample(biddingFileEntity.getId(), loginUserId, (short) 0);
            BiddingFileAuditorEntity biddingFileAuditorEntity = biddingFileAuditorEntities.get(0);
            biddingFileAuditorEntity.setIsPass(isPass);
            biddingFileAuditorEntity.setReason(isPass == 2 ? reason : null);
            biddingFileAuditorService.edit(biddingFileAuditorEntity);

            BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());
            if (biddingEntity != null) {
                //给审核人发审核结果消息
                MessageEntity messageEntity1 = new MessageEntity();
                messageEntity1.setCreateBy(userName);
                messageEntity1.setCreateTime(new Date());
                messageEntity1.setModifiedBy(userName);
                messageEntity1.setModifiedTime(new Date());
                messageEntity1.setTitle("文件");
                messageEntity1.setContext("“" + biddingEntity.getProjectName() + "”项目中，“" + biddingFileEntity.getName() + "”文件已被您审核！");
                messageEntity1.setType((short) 1);
                messageEntity1.setSmallType((short) 2);
                messageEntity1.setMessageType((short) 2);
                messageEntity1.setProjectId(biddingFileEntity.getBiddingId());
                messageEntity1.setFileId(biddingFileEntity.getId());
                messageEntity1.setSenderId(null);
                messageEntity1.setReceiverId(biddingFileAuditorEntity.getAuditorId());
                messageEntity1.setStatus((short) 1);
                messageService.sendMessage(messageEntity1);
                //给被审核人发消息
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
            //文件日志记录
            StringBuilder sb = new StringBuilder();
            sb.append("   审核人：").append(trueName)
                    .append("   审核状态：").append(isPass == 2 ? "已审核未通过" : "已审核通过");
            if (isPass == 2) {
                sb.append("   理由：").append(reason);
            }
            SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 2, id, sb.toString());
            solutionLogService.add(solutionLogEntity);

        }
    }


}
