package com.yintu.ruixing.jiejuefangan.impl;

import cn.hutool.core.date.DateUtil;
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

            }

        }
        //项目日志记录
        StringBuilder sb = new StringBuilder();
        sb.append("   文件名：").append(preSaleFileEntity.getName())
                .append("   文件路径：").append(preSaleFileEntity.getPath())
                .append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误")
                .append("   发布状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
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
            Integer id = preSaleFileEntity.getId();
            preSaleFileAuditorService.removeByPreSaleFileId(id); //删除
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
                if (preSaleFileAuditorEntities.size() > 0)
                    preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);//添加
            }

            //项目日志记录
            PreSaleFileEntity psfTarget = BeanUtil.compareFieldValues(psfSource, preSaleFileEntity, PreSaleFileEntity.class);
            StringBuilder sb = new StringBuilder();
            if (psfTarget.getName() != null) {
                sb.append("   文件名：").append(preSaleFileEntity.getName());
            }
            if (psfTarget.getPath() != null) {
                sb.append("   文件类型：").append(preSaleFileEntity.getType() == 1 ? "输入文件" : preSaleFileEntity.getType() == 2 ? "输出文件" : "错误");
            }
            if (psfTarget.getReleaseStatus() != null) {
                sb.append("   发布状态：").append(preSaleFileEntity.getReleaseStatus() == 1 ? "录入" : preSaleFileEntity.getReleaseStatus() == 2 ? "发布" : "错误");
            }

            if (auditorIds != null && auditorIds.length == 0 && !psfaSources.isEmpty()) {
                sb.append("   审核人：").append("   审核状态：");
            }

            if (auditorIds != null && auditorIds.length > 0) {
                UserEntity userEntity = userService.findById(auditorIds[0].longValue());
                if (psfaSources.isEmpty()) {
                    sb.append("   审核人：").append(userEntity.getTrueName())
                            .append("   审核状态：").append("待审核");
                } else {
                    PreSaleFileAuditorEntity psfaTarget = BeanUtil.compareFieldValues(psfaSources.get(0), preSaleFileAuditorService.findByPreSaleFileId(auditorIds[0]).get(0), PreSaleFileAuditorEntity.class);
                    if (psfaTarget.getAuditorId() != null) {
                        sb.append("   审核人：").append(userEntity.getTrueName());
                    }
                    if (psfaTarget.getIsPass() != null) {
                        sb.append("   审核状态：").append(psfaTarget.getIsPass() == 1 ? "待审核" : psfaTarget.getIsPass() == 2 ? "已审核未通过" : psfaTarget.getIsPass() == 3 ? "已审核未通过" : "错误");
                    }
                }
            }
            if (psfTarget.getRemark() != null) {
                sb.append("   备注：").append(preSaleFileEntity.getRemark());
            }
            if (!"".equals(sb.toString())) {
                SolutionLogEntity solutionLogEntity = new SolutionLogEntity(null, trueName, new Date(), (short) 1, (short) 2, preSaleFileEntity.getId(), sb.toString());
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
    public List<PreSaleFileEntity> findPreSaleIdAndNameAndType(Integer preSaleId, String name, String type, Integer userId) {
        return preSaleFileDao.selectByCondition(preSaleId, null, name, type == null ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
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

}
