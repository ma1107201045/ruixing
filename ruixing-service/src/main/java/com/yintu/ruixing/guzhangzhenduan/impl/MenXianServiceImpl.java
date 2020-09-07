package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/12 11:54
 */
@Service
@Transactional
public class MenXianServiceImpl implements MenXianService {
    @Autowired
    private MenXianDao menXianDao;
    @Autowired
    private CheZhanService cheZhanService;
    @Autowired
    private QuDuanBaseService quDuanBaseService;
    @Autowired
    private QuDuanInfoPropertyService quDuanInfoPropertyService;
    @Autowired
    private QuDuanInfoService quDuanInfoService;

    @Override
    public void add(MenXianEntity menXianEntity) {
        menXianDao.insertSelective(menXianEntity);
    }

    @Override
    public void remove(Integer id) {
        menXianDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MenXianEntity menXianEntity) {
        menXianDao.updateByPrimaryKeySelective(menXianEntity);
    }

    @Override
    public MenXianEntity findById(Integer id) {
        return menXianDao.selectByPrimaryKey(id);
    }

    @Override
    public MenXianEntity findByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quanduanId, Integer propertyId) {
        return menXianDao.selectByCzIdAndQuduanIdAndPropertyId(czId, quanduanId, propertyId);
    }


    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "门限参数列表";
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
    public void importData(JSONArray ja, String loginUserName) {
        for (int i = 0; i < ja.size(); i++) {
            Object obj = ja.get(i);
            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                String czName = jsonArray.getString(1);
                if (czName != null && !"".equals(czName)) {
                    List<CheZhanEntity> cheZhanEntities = cheZhanService.findByCzName(czName);
                    if (cheZhanEntities.isEmpty())
                        throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "车站不存在");
                    Integer czId = (int) cheZhanEntities.get(0).getCzId();
                    String qdName = jsonArray.getString(2);
                    if (qdName != null && !"".equals(qdName)) {
                        QuDuanBaseEntity quDuanBaseEntity = quDuanBaseService.findByCzIdAndQuduanyunyingName(czId, qdName);
                        if (quDuanBaseEntity == null) {
                            throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "车站下边没有此区段");
                        }
                        Integer qdId = quDuanBaseEntity.getQdid();
                        String propertyName = jsonArray.getString(3);
                        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.finByName(propertyName);
                        Integer propertyId = quDuanInfoPropertyEntities.get(0).getId();
                        MenXianEntity menXianEntity = this.findByCzIdAndQuduanIdAndPropertyId(czId, qdId, propertyId);
                        if (menXianEntity == null) {
                            menXianEntity = new MenXianEntity();
                            menXianEntity.setCzId(czId);
                            menXianEntity.setQuduanId(qdId);
                            menXianEntity.setPropertyId(propertyId);
                            menXianEntity.setSuperiorLimitValue(jsonArray.getString(4));
                            menXianEntity.setLowerLimitValue(jsonArray.getString(5));
                            menXianEntity.setOutburstValue(jsonArray.getString(6));
                            this.add(menXianEntity);
                        } else {
                            menXianEntity.setSuperiorLimitValue(jsonArray.getString(4));
                            menXianEntity.setLowerLimitValue(jsonArray.getString(5));
                            menXianEntity.setOutburstValue(jsonArray.getString(6));
                            this.edit(menXianEntity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "门限参数列表";
        //excel表名
        String[] headers = {"序号", "车站名称", "区段名称", "属性名称", "上限", "下限", "跳变系数"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
