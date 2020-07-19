package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.dao.MaintenancePlanDao;
import com.yintu.ruixing.entity.MaintenancePlanEntity;
import com.yintu.ruixing.service.MaintenancePlanService;
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

/**
 * @author:mlf
 * @date:2020/7/8 15:51
 */
@Service
@Transactional
public class MaintenancePlanServiceImpl implements MaintenancePlanService {

    @Autowired
    private MaintenancePlanDao maintenancePlanDao;

    @Override
    public void add(MaintenancePlanEntity entity) {
        entity.setCreatedDate(new Date());
        maintenancePlanDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        maintenancePlanDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MaintenancePlanEntity entity) {
        maintenancePlanDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public MaintenancePlanEntity findById(Integer id) {
        return maintenancePlanDao.selectByPrimaryKey(id);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<MaintenancePlanEntity> findByName(String name) {
        return maintenancePlanDao.selectByName(name);
    }

    @Override
    public List<MaintenancePlanEntity> findByIds(Integer[] ids) {
        return maintenancePlanDao.selectByIds(ids);
    }

    @Override
    public void add(List<MaintenancePlanEntity> maintenancePlanEntities) {
        maintenancePlanDao.insertMuch(maintenancePlanEntities);
    }

    @Override
    public void importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "维护计划列表";
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFData(title, new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFData(title, new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        List<MaintenancePlanEntity> maintenancePlanEntities = new ArrayList<>();
        for (String[] rows : content) {
            MaintenancePlanEntity maintenancePlanEntity = new MaintenancePlanEntity();
            maintenancePlanEntity.setName(rows[1]);
            maintenancePlanEntity.setCreatedDate(new Date());
            maintenancePlanEntities.add(maintenancePlanEntity);
        }
        if (!maintenancePlanEntities.isEmpty())
            this.add(maintenancePlanEntities);
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "维护计划列表";
        //excel表名
        String[] headers = {"序号", "名称"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "维护计划列表";
        //excel表名
        String[] headers = {"序号", "名称"};
        //获取数据
        List<MaintenancePlanEntity> maintenancePlanEntities = this.findByIds(ids);
        maintenancePlanEntities = maintenancePlanEntities.stream()
                .sorted(Comparator.comparing(MaintenancePlanEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[maintenancePlanEntities.size()][headers.length];
        for (int i = 0; i < maintenancePlanEntities.size(); i++) {
            MaintenancePlanEntity maintenancePlanEntity = maintenancePlanEntities.get(i);
            content[i][0] = maintenancePlanEntity.getId().toString();
            content[i][1] = maintenancePlanEntity.getName();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
