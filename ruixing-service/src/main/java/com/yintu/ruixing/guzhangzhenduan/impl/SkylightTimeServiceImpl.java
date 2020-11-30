package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.guzhangzhenduan.SkylightTimeDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/18 17:55
 */
@Service
@Transactional
public class SkylightTimeServiceImpl implements SkylightTimeService {
    @Autowired
    private SkylightTimeDao skylightTimeDao;
    @Autowired
    private CheZhanService cheZhanService;
    @Autowired
    private QuDuanBaseService quDuanBaseService;

    @Override
    public void add(SkylightTimeEntity entity) {
        skylightTimeDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        skylightTimeDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(SkylightTimeEntity entity) {
        skylightTimeDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public SkylightTimeEntity findById(Integer id) {
        return skylightTimeDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(SkylightTimeEntity entity, Integer[] qdIds) {
        for (Integer qbId : qdIds) {
            SkylightTimeEntity skylightTimeEntity = this.findByCzIdAndQdId(entity.getCzId(), qbId);
            if (skylightTimeEntity == null) {
                entity.setQdId(qbId);
                this.add(entity);
            }
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public SkylightTimeEntity findByCzIdAndQdId(Integer czId, Integer qdId) {
        return skylightTimeDao.selectByCzIdAndQdId(czId, qdId);
    }

    @Override
    public List<SkylightTimeEntity> findByCondition(Integer id, Date startTime, Date endTime, Integer czId) {
        return skylightTimeDao.connectSelectByCondition(id, startTime, endTime, czId);
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "天窗时间列表";
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
    public void importData(String[][] context, String loginUserName) {
        for (int i = 0; i < context.length; i++) {
            JSONArray jsonArray = (JSONArray) JSONArray.toJSON(context[i]);
            String czName = context[i][1];
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
                    SkylightTimeEntity skylightTimeEntity = this.findByCzIdAndQdId(czId, qdId);
                    if (skylightTimeEntity == null) {
                        skylightTimeEntity = new SkylightTimeEntity();
                        skylightTimeEntity.setCreateBy(loginUserName);
                        skylightTimeEntity.setCreateTime(new Date());
                        skylightTimeEntity.setModifiedBy(loginUserName);
                        skylightTimeEntity.setModifiedTime(new Date());
                        skylightTimeEntity.setCzId(czId);
                        skylightTimeEntity.setQdId(qdId);
                        try {
                            Date startTime = jsonArray.getDate(3);
                            Date entTime = jsonArray.getDate(4);
                            if (!entTime.after(startTime))
                                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "结束时间不能在开始时间之前");
                            skylightTimeEntity.setStartTime(startTime);
                            skylightTimeEntity.setEndTime(entTime);
                            this.add(skylightTimeEntity);
                        } catch (NumberFormatException e) {
                            throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "时间格式有误，格式为yyyy/MM/dd hh:mm:ss");
                        } catch (BaseRuntimeException e) {
                            throw new BaseRuntimeException(e.getMessage());
                        }
                    }
                }

            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "天窗时间列表";
        //excel表名
        String[] headers = {"序号", "车站名称", "区段名称", "开始时间", "结束时间"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
