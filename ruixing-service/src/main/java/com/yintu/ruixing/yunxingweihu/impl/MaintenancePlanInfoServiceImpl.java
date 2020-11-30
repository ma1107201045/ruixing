package com.yintu.ruixing.yunxingweihu.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.master.yunxingweihu.MaintenancePlanInfoDao;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoEntity;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaintenancePlanInfoServiceImpl implements MaintenancePlanInfoService {

    @Autowired
    private MaintenancePlanInfoDao maintenancePlanInfoDao;

    @Override
    public void add(MaintenancePlanInfoEntity entity) {
        maintenancePlanInfoDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        maintenancePlanInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MaintenancePlanInfoEntity entity) {
        maintenancePlanInfoDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public MaintenancePlanInfoEntity findById(Integer id) {
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = maintenancePlanInfoDao.selectByCondition(new Integer[]{id}, null, null, null);
        return maintenancePlanInfoEntities.size() > 0 ? maintenancePlanInfoEntities.get(0) : null;
    }

    @Override
    public void add(List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities) {
        maintenancePlanInfoDao.insertMuch(maintenancePlanInfoEntities);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<MaintenancePlanInfoEntity> findByCondition(Integer[] ids, String context, Integer maintenancePlanId, Date date) {
        return maintenancePlanInfoDao.selectByCondition(ids, context, maintenancePlanId, date);
    }

    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "维护记录信息列表";
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFData(title, new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFData(title, new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        return content;
    }


    @Override
    public void importData(Integer maintenancePlanId, String[][] context, String loginUsername) {
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            String[] row = context[i];
            MaintenancePlanInfoEntity maintenancePlanInfoEntity = new MaintenancePlanInfoEntity();
            maintenancePlanInfoEntity.setCreateBy(loginUsername);
            maintenancePlanInfoEntity.setCreateTime(new Date());
            maintenancePlanInfoEntity.setModifiedBy(loginUsername);
            maintenancePlanInfoEntity.setModifiedTime(new Date());
            maintenancePlanInfoEntity.setContext(row[1]);
            if (!"未完成".equals(row[2]) && !"已完成".equals(row[2]))
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "是否完成有误");
            maintenancePlanInfoEntity.setIsFinish("未完成".equals(row[2]) ? (short) 0 : 1);
            maintenancePlanInfoEntity.setDocumentNames(row[3]);
            maintenancePlanInfoEntity.setDocumentFiles(row[4]);
            maintenancePlanInfoEntity.setMaintenancePlanId(maintenancePlanId);
            maintenancePlanInfoEntities.add(maintenancePlanInfoEntity);
        }
        if (!maintenancePlanInfoEntities.isEmpty())
            this.add(maintenancePlanInfoEntities);
    }


    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "维护记录信息列表";
        //excel表名
        String[] headers = {"序号", "维护内容", "是否完成", "记录文档名称集", "记录文档文件集"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "维护记录信息列表";
        //excel表名
        String[] headers = {"序号", "创建时间", "维护内容", "是否完成", "记录文档名称集", "记录文档文件集"};
        //获取数据
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = this.findByCondition(ids, null, null, null);
        maintenancePlanInfoEntities = maintenancePlanInfoEntities.stream()
                .sorted(Comparator.comparing(MaintenancePlanInfoEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[maintenancePlanInfoEntities.size()][headers.length];
        for (int i = 0; i < maintenancePlanInfoEntities.size(); i++) {
            MaintenancePlanInfoEntity maintenancePlanInfoEntity = maintenancePlanInfoEntities.get(i);
            content[i][0] = maintenancePlanInfoEntity.getId().toString();
            content[i][1] = DateUtil.formatDateTime(maintenancePlanInfoEntity.getCreateTime());
            content[i][2] = maintenancePlanInfoEntity.getContext();
            content[i][3] = maintenancePlanInfoEntity.getIsFinish() == 1 ? "已完成" : maintenancePlanInfoEntity.getIsFinish() == 2 ? "未完成" : "";
            content[i][4] = maintenancePlanInfoEntity.getDocumentNames();
            content[i][5] = maintenancePlanInfoEntity.getDocumentFiles();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
