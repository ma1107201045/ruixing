package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.PreSaleFileDao;
import com.yintu.ruixing.jiejuefangan.PreSaleEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileService;
import com.yintu.ruixing.jiejuefangan.PreSaleService;
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
            if (preSaleFileAuditorEntities.size() > 0)
                preSaleFileAuditorService.addMuch(preSaleFileAuditorEntities);
        }

    }

    @Override
    public void edit(PreSaleFileEntity preSaleFileEntity, Integer[] auditorIds, String trueName) {
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
    public List<PreSaleFileEntity> findPreSaleIdAndNameAndType(Integer preSaleId, String name, String type) {
        return preSaleFileDao.selectByCondition(preSaleId, null, name, type == null ? null : "输入文件".equals(type) ? (short) 1 : (short) 2);
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "售前技术支持列表";
        //excel表名
        String[] headers = {"序号", "年份", "项目名称", "项目状态", "任务状态", "文件类型", "文件名称",};
        //获取数据
        List<PreSaleFileEntity> preSaleFileEntities = preSaleFileDao.selectByCondition(null, ids, null, null);
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
            Short projectStatus = preSaleEntity.getProjectStatus();
            content[i][3] = projectStatus.equals((short) 1) ? "未知" : projectStatus.equals((short) 2) ? "后续招标" : projectStatus.equals((short) 3) ? "确定采用" : projectStatus.equals((short) 4) ? "关闭" : "未知";
            Short taskStatus = preSaleEntity.getTaskStatus();
            content[i][4] = taskStatus.equals((short) 1) ? "正在进行" : taskStatus.equals((short) 2) ? "已完成" : "正在进行";
            Short type = preSaleFileEntity.getType();
            content[i][5] = type.equals((short) 1) ? "输入文件" : type.equals((short) 2) ? "输出文件" : "输入文件";
            content[i][6] = preSaleFileEntity.getName();

        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
