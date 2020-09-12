package com.yintu.ruixing.yunxingweihu.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.yunxingweihu.*;
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
public class SparePartsInfoServiceImpl implements SparePartsInfoService {

    @Autowired
    private SparePartsInfoDao sparePartsInfoDao;

    @Override
    public void add(SparePartsInfoEntity entity) {
        sparePartsInfoDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        sparePartsInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(SparePartsInfoEntity entity) {
        sparePartsInfoDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public SparePartsInfoEntity findById(Integer id) {
        List<SparePartsInfoEntity> sparePartsInfoEntities = sparePartsInfoDao.selectByCondition(new Integer[]{id}, null, null);
        return sparePartsInfoEntities.size() > 0 ? sparePartsInfoEntities.get(0) : null;
    }

    @Override
    public void add(List<SparePartsInfoEntity> sparePartsInfoEntities) {
        sparePartsInfoDao.insertMuch(sparePartsInfoEntities);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<SparePartsInfoEntity> findByCondition(Integer[] ids, Integer maintenancePlanId, Date date) {
        return sparePartsInfoDao.selectByCondition(ids, maintenancePlanId, date);
    }

    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "备品试验记录信息列表";
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
        List<SparePartsInfoEntity> sparePartsInfoEntities = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            String[] row = context[i];
            SparePartsInfoEntity sparePartsInfoEntity = new SparePartsInfoEntity();
            sparePartsInfoEntity.setCreateBy(loginUsername);
            sparePartsInfoEntity.setCreateTime(new Date());
            sparePartsInfoEntity.setModifiedBy(loginUsername);
            sparePartsInfoEntity.setModifiedTime(new Date());
            sparePartsInfoEntity.setContext(row[1]);
            if (!"未完成".equals(row[2]) && !"已完成".equals(row[2]))
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "是否完成有误");
            sparePartsInfoEntity.setIsFinish("未完成".equals(row[2]) ? (short) 0 : 1);
            sparePartsInfoEntity.setDocumentNames(row[3]);
            sparePartsInfoEntity.setDocumentFiles(row[4]);
            sparePartsInfoEntities.add(sparePartsInfoEntity);
        }
        if (!sparePartsInfoEntities.isEmpty())
            this.add(sparePartsInfoEntities);
    }


    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "备品试验记录信息列表";
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
        String title = "备品试验记录信息列表";
        //excel表名
        String[] headers = {"序号", "创建时间", "维护内容", "是否完成", "记录文档名称集", "记录文档文件集"};
        //获取数据
        List<SparePartsInfoEntity> sparePartsInfoEntities = this.findByCondition(ids, null, null);
        sparePartsInfoEntities = sparePartsInfoEntities.stream()
                .sorted(Comparator.comparing(SparePartsInfoEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[sparePartsInfoEntities.size()][headers.length];
        for (int i = 0; i < sparePartsInfoEntities.size(); i++) {
            SparePartsInfoEntity sparePartsInfoEntity = sparePartsInfoEntities.get(i);
            content[i][0] = sparePartsInfoEntity.getId().toString();
            content[i][1] = DateUtil.formatDateTime(sparePartsInfoEntity.getCreateTime());
            content[i][2] = sparePartsInfoEntity.getContext();
            content[i][3] = sparePartsInfoEntity.getIsFinish() == 1 ? "已完成" : sparePartsInfoEntity.getIsFinish() == 2 ? "未完成" : "";
            content[i][4] = sparePartsInfoEntity.getDocumentNames();
            content[i][5] = sparePartsInfoEntity.getDocumentFiles();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
