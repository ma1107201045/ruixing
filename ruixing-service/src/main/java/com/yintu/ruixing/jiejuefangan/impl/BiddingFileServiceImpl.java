package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
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
    private UserService userService;
    @Autowired
    private MessageService messageService;

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
    public void add(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName) {
        this.add(biddingFileEntity);
        Integer id = biddingFileEntity.getId();
        if (auditorIds != null) {
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Integer auditorId : auditorIds) {
                if (auditorId != null) {
                    BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                    biddingFileAuditorEntity.setBiddingFileId(id);
                    biddingFileAuditorEntity.setAuditorId(auditorId);
                    biddingFileAuditorEntity.setIsPass((short) 1);
                    biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                }
            }
            if (biddingFileAuditorEntities.size() > 0) {
                biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
                //给审核人发消息
                BiddingEntity biddingEntity = biddingService.findById(biddingFileEntity.getBiddingId());
                if (biddingEntity != null) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(biddingFileEntity.getCreateBy());
                    messageEntity.setCreateTime(biddingFileEntity.getCreateTime());
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
                    messageEntity.setReceiverId(biddingFileAuditorEntities.get(0).getAuditorId());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
        }

        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件类型：").append(biddingFileEntity.getType() == 1 ? "输入文件" : biddingFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   文件名称：").append(biddingFileEntity.getName())
                .append("   文件状态：").append(biddingFileEntity.getReleaseStatus() == 1 ? "录入" : biddingFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
        if (auditorIds != null && auditorIds.length > 0) {
            UserEntity userEntity = userService.findById(auditorIds[0].longValue());
            if (userEntity != null)
                sb.append("   审核人：").append(userEntity.getTrueName())
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
    public void edit(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName) {
        BiddingFileEntity bfSource = this.findById(biddingFileEntity.getId());
        List<BiddingFileAuditorEntity> bfaSources = biddingFileAuditorService.findByBiddingFileIdId(biddingFileEntity.getId());
        if (bfSource != null) {
            this.edit(biddingFileEntity);
            Integer id = biddingFileEntity.getId();
            biddingFileAuditorService.removeByBiddingFileId(id); //删除
            if (auditorIds != null) {
                List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
                for (Integer auditorId : auditorIds) {
                    if (auditorId != null) {
                        BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                        biddingFileAuditorEntity.setBiddingFileId(id);
                        biddingFileAuditorEntity.setAuditorId(auditorId);
                        biddingFileAuditorEntity.setIsPass((short) 1);
                        biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                    }
                }
                if (biddingFileAuditorEntities.size() > 0)
                    biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
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
            if (auditorIds != null && auditorIds.length == 0 && !bfaSources.isEmpty()) {
                sb.append("   审核人：").append("   审核状态：");
            }

            if (auditorIds != null && auditorIds.length > 0) {
                UserEntity userEntity = userService.findById(auditorIds[0].longValue());
                if (bfaSources.isEmpty() || !auditorIds[0].equals(bfaSources.get(0).getAuditorId())) {
                    sb.append("   审核人：").append(userEntity.getTrueName())
                            .append("   审核状态：").append("待审核");
                } else {
//                    sb.append("   审核人：").append(userEntity.getTrueName())
//                            .append("   审核状态：").append(psfaTarget.getIsPass() == 1 ? "待审核" : psfaTarget.getIsPass() == 2 ? "已审核未通过" : psfaTarget.getIsPass() == 3 ? "已审核未通过" : "错误");
                }
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
        Integer biddingId = biddingFileEntity.getBiddingId();
        if (biddingId != null) {
            BiddingEntity biddingEntity = biddingService.findById(biddingId);
            biddingFileEntity.setBiddingEntity(biddingEntity);
        }
        List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByBiddingFileIdId(id);
        biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorEntities);
        return biddingFileEntity;
    }

    @Override
    public List<BiddingFileEntity> findByBiddingIdAndNameAndType(Integer biddingId, String name, String type, Integer userId) {
        return biddingFileDao.selectByCondition(biddingId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
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


}
