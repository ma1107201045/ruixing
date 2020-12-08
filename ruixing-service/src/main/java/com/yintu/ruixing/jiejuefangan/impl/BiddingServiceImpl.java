package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BeanUtil;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import com.yintu.ruixing.jiejuefangan.*;
import com.yintu.ruixing.master.jiejuefangan.BiddingDao;
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
    @Autowired
    private UserService userService;

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
        //投招标支持项目状态为3时发送消息
        if (entity.getProjectStatus().equals((short) 3)) {
            List<UserEntity> userEntities = userService.findByTruename(null);
            for (UserEntity userEntity : userEntities) {
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
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(userEntity.getId().intValue());
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   项目创建日期：").append(DateUtil.formatDate(entity.getProjectDate()))
                .append("   项目名称：").append(entity.getProjectName())
                .append("   所属路局：").append(tieLuJuService.findByTljId(entity.getRailwayAdministrationId().longValue()).getTljName())
                .append("   任务状态：").append(entity.getTaskStatus() == 1 ? "正在进行" : entity.getTaskStatus() == 2 ? "已完成" : "错误")
                .append("   项目状态：").append(entity.getProjectStatus() == 1 ? "正在投标" : entity.getProjectStatus() == 2 ? "未中标" : entity.getProjectStatus() == 3 ? "已中标" : entity.getProjectStatus() == 4 ? "流标再投" : "错误")
                .append("   备注：").append(entity.getRemark());
        solutionLogService.add(new SolutionLogEntity(null, trueName, new Date(), (short) 2, (short) 1, entity.getId(), sb.toString()));

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
            if (source.getProjectStatus() != 1 && entity.getProjectStatus() == 1)
                throw new BaseRuntimeException("项目不能由其他状态改为正在投标");
            if (entity.getProjectStatus() == 3 && source.getProjectStatus() == 3 && !entity.getProjectName().equals(source.getProjectName())) {
                //更新项目消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 2, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    messageEntity.setModifiedBy(entity.getModifiedBy());
                    messageEntity.setModifiedTime(entity.getModifiedTime());
                    messageEntity.setContext("“" + entity.getProjectName() + "”项目已中标，请关注项目进展情况，及时进行设计联络！");
                    messageService.edit(messageEntity);
                }
            }
            if (entity.getProjectStatus() == 3 && source.getProjectStatus() != 3) {
                List<UserEntity> userEntities = userService.findByTruename(null);
                for (UserEntity userEntity : userEntities) {
                    //添加项目消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(entity.getModifiedBy());
                    messageEntity.setCreateTime(entity.getModifiedTime());
                    messageEntity.setModifiedBy(entity.getModifiedBy());
                    messageEntity.setModifiedTime(entity.getModifiedTime());
                    messageEntity.setTitle("项目");
                    messageEntity.setContext("“" + entity.getProjectName() + "”项目已中标，请关注项目进展情况，及时进行设计联络！");
                    messageEntity.setType((short) 1);
                    messageEntity.setSmallType((short) 2);
                    messageEntity.setMessageType((short) 1);
                    messageEntity.setProjectId(entity.getId());
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(userEntity.getId().intValue());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
            if (entity.getProjectStatus() != 3 && source.getProjectStatus() == 3) {
                //删除项目消息
                MessageEntity messageExample = new MessageEntity(null, null, null, null, null, null,
                        (short) 1, (short) 1, (short) 1, entity.getId(), null, null, null, null, null);
                List<MessageEntity> messageEntities = messageService.findByExample(messageExample);
                for (MessageEntity messageEntity : messageEntities) {
                    messageService.remove(messageEntity.getId());
                }
            }

            //项目日志记录
            BiddingEntity target = BeanUtil.compareFieldValues(source, entity, BiddingEntity.class);
            StringBuilder sb = new StringBuilder();
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
        return biddingDao.selectByExample(null, year, projectName);
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
            firstTreeNodeUtil.setValue(String.valueOf(year + 100000));
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);

            for (BiddingEntity biddingEntity : biddingEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(biddingEntity.getProjectName());
                secondTreeNodeUtil.setValue(String.valueOf(biddingEntity.getId()));
                Map<String, Object> secondMap = cn.hutool.core.bean.BeanUtil.beanToMap(biddingEntity);
                secondTreeNodeUtil.setA_attr(secondMap);
                secondTreeNodeUtil.setChildren(thirdTreeNodeUtils);
                secondTreeNodeUtils.add(secondTreeNodeUtil);
                TreeNodeUtil thirdTreeNodeUtil1 = new TreeNodeUtil();
                thirdTreeNodeUtil1.setId(3L);
                thirdTreeNodeUtil1.setLabel("输入文件");
                thirdTreeNodeUtil1.setValue(String.valueOf(biddingEntity.getId() + 1000000));
                TreeNodeUtil thirdTreeNodeUtil2 = new TreeNodeUtil();
                thirdTreeNodeUtil2.setId(3L);
                thirdTreeNodeUtil2.setLabel("输出文件");
                thirdTreeNodeUtil2.setValue(String.valueOf(biddingEntity.getId() + 1000001));
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
        String[] headers = {"序号", "项目名称", "项目状态", "项目创建日期", "任务状态", "任务完成时间", "所属路局", "备注"};
        //获取数据
        List<BiddingEntity> biddingEntities = biddingDao.selectByExample(ids, null, null);
        biddingEntities = biddingEntities.stream()
                .sorted(Comparator.comparing(BiddingEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[biddingEntities.size()][headers.length];
        for (int i = 0; i < biddingEntities.size(); i++) {
            BiddingEntity biddingEntity = biddingEntities.get(i);
            content[i][0] = biddingEntity.getId().toString();
            content[i][1] = biddingEntity.getProjectName();
            Short projectStatus = biddingEntity.getProjectStatus();
            content[i][2] = projectStatus == 1 ? "未知" : projectStatus == 2 ? "后续招标" : projectStatus == 3 ? "确定采用" : "关闭";
            Short taskStatus = biddingEntity.getTaskStatus();
            content[i][3] = DateUtil.formatDate(biddingEntity.getProjectDate());
            content[i][4] = taskStatus == 1 ? "正在进行" : "已完成";
            content[i][5] = DateUtil.formatDate(biddingEntity.getTaskFinishDate());
            content[i][6] = biddingEntity.getTieLuJuEntity().getTljName();
            content[i][7] = biddingEntity.getRemark();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
