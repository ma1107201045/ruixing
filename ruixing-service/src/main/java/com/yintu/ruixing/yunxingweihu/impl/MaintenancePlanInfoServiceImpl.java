package com.yintu.ruixing.yunxingweihu.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoDao;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentEntity;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.weixiudaxiu.EquipmentService;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = maintenancePlanInfoDao.selectByCondition(new Integer[]{id}, null, null);
        return maintenancePlanInfoEntities.size() > 0 ? maintenancePlanInfoEntities.get(0) : null;
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<MaintenancePlanInfoEntity> findByCondition(Integer[] ids, Integer maintenancePlanId, Date date) {
        return maintenancePlanInfoDao.selectByCondition(ids, maintenancePlanId, date);
    }


    @Override
    public void add(List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities) {
        maintenancePlanInfoDao.insertMuch(maintenancePlanInfoEntities);
    }

    @Override
    public void importFile(InputStream inputStream, String fileName, Integer maintenancePlanId) throws IOException {
        //excel标题
        String title = "维护计划详情列表";
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFData(title, new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFData(title, new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = new ArrayList<>();
//        for (String[] rows : content) {
//            MaintenancePlanInfoEntity maintenancePlanInfoEntity = new MaintenancePlanInfoEntity();
//            maintenancePlanInfoEntity.setMaintenancePlanId(maintenancePlanId);
//
//            String cheZhanName = rows[1];
//            List<CheZhanEntity> cheZhanEntities = cheZhanService.findByCzName(cheZhanName);
//            if (!cheZhanEntities.isEmpty())
//                maintenancePlanInfoEntity.setCheZhanId((int) cheZhanEntities.get(0).getCid());
//
//            String equipmentName = rows[2];
//            List<EquipmentEntity> equipmentEntities = equipmentService.findByName(equipmentName);
//            if (!equipmentEntities.isEmpty())
//                maintenancePlanInfoEntity.setEquipmentId(equipmentEntities.get(0).getId());
//
//            if (rows[3] == null || rows[4] == null) {
//                throw new BaseRuntimeException("开始日期或者结束日期不能为空");
//            }
//            try {
//                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(rows[3]);
//                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(rows[4]);
//                if (endDate.after(startDate) || endDate.getTime() == startDate.getTime()) {
//                    maintenancePlanInfoEntity.setStartDate(startDate);
//                    maintenancePlanInfoEntity.setEndDate(endDate);
//                }
//            } catch (ParseException e) {
//                throw new BaseRuntimeException("日期转换有误");
//            }
//            maintenancePlanInfoEntity.setCreatedDate(new Date());
//            maintenancePlanInfoEntities.add(maintenancePlanInfoEntity);
//        }
        if (!maintenancePlanInfoEntities.isEmpty())
            this.add(maintenancePlanInfoEntities);
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "维护计划详情列表";
        //excel表名
        String[] headers = {"序号", "车站名称", "设备名称", "开始日期", "结束日期"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "维护计划详情列表";
        //excel表名
        String[] headers = {"序号", "车站名称", "设备名称", "开始日期", "结束日期"};
        //获取数据
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = this.findByCondition(ids, null, null);
        maintenancePlanInfoEntities = maintenancePlanInfoEntities.stream()
                .sorted(Comparator.comparing(MaintenancePlanInfoEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[maintenancePlanInfoEntities.size()][headers.length];
//        for (int i = 0; i < maintenancePlanInfoEntities.size(); i++) {
//            MaintenancePlanInfoEntity maintenancePlanInfoEntity = maintenancePlanInfoEntities.get(i);
//            content[i][0] = maintenancePlanInfoEntity.getId().toString();
//            content[i][1] = maintenancePlanInfoEntity.getCheZhanEntity().getCzName();
//            content[i][2] = maintenancePlanInfoEntity.getEquipmentEntity().getName();
//            content[i][3] = new SimpleDateFormat("yyyy-MM-dd").format(maintenancePlanInfoEntity.getStartDate());
//            content[i][4] = new SimpleDateFormat("yyyy-MM-dd").format(maintenancePlanInfoEntity.getEndDate());
//
//        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
