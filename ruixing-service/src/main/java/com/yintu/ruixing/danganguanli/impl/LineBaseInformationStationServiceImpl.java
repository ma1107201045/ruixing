package com.yintu.ruixing.danganguanli.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationDao;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationStationUnitDao;
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
 * @Author: mlf
 * @Date: 2020/12/24 18:07
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationStationServiceImpl implements LineBaseInformationStationService {
    @Autowired
    private LineBaseInformationStationDao lineBaseInformationStationDao;

    @Autowired
    private LineBaseInformationStationUnitService lineBaseInformationStationUnitService;
    @Autowired
    private DataStatsService dataStatsService;

    @Override
    public void add(LineBaseInformationStationEntity entity) {
        lineBaseInformationStationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationStationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationStationEntity entity) {
        lineBaseInformationStationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationStationEntity findById(Integer id) {
        return lineBaseInformationStationDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds) {
        this.add(lineBaseInformationStationEntity);
        for (Integer unitId : unitIds) {
            LineBaseInformationStationUnitEntity lineBaseInformationStationUnitEntity = new LineBaseInformationStationUnitEntity();
            lineBaseInformationStationUnitEntity.setCreateBy(lineBaseInformationStationEntity.getCreateBy());
            lineBaseInformationStationUnitEntity.setCreateTime(lineBaseInformationStationEntity.getCreateTime());
            lineBaseInformationStationUnitEntity.setModifiedBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setModifiedTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
            lineBaseInformationStationUnitEntity.setUnitId(unitId);
            lineBaseInformationStationUnitService.add(lineBaseInformationStationUnitEntity);
        }
    }

    @Override
    public void edit(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds) {
        this.edit(lineBaseInformationStationEntity);
        lineBaseInformationStationUnitService.removeByLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
        for (Integer unitId : unitIds) {
            LineBaseInformationStationUnitEntity lineBaseInformationStationUnitEntity = new LineBaseInformationStationUnitEntity();
            lineBaseInformationStationUnitEntity.setCreateBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setCreateTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setModifiedBy(lineBaseInformationStationEntity.getModifiedBy());
            lineBaseInformationStationUnitEntity.setModifiedTime(lineBaseInformationStationEntity.getModifiedTime());
            lineBaseInformationStationUnitEntity.setLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
            lineBaseInformationStationUnitEntity.setUnitId(unitId);
            lineBaseInformationStationUnitService.add(lineBaseInformationStationUnitEntity);
        }
    }

    @Override
    public List<LineBaseInformationStationEntity> findByExample(Integer lineBaseInformationId, Integer id, String name, Integer[] ids) {
        List<LineBaseInformationStationEntity> lineBaseInformationStationEntities = lineBaseInformationStationDao.selectByExample(lineBaseInformationId, id, name, ids);
        for (LineBaseInformationStationEntity lineBaseInformationStationEntity : lineBaseInformationStationEntities) {
            lineBaseInformationStationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationStationEntity.getId()));
        }
        return lineBaseInformationStationEntities;
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return lineBaseInformationStationDao.selectDianWuDuanEntityById(id);
    }

    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "车站基本信息列表";
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
    public void importDate(Integer lineBaseInformationId, List<List<String>> list, String loginUsername) {
        for (int i = 0; i < list.size(); i++) {
            List<String> row = list.get(i);
            LineBaseInformationStationEntity lineBaseInformationStationEntity = new LineBaseInformationStationEntity();
            lineBaseInformationStationEntity.setCreateBy(loginUsername);
            lineBaseInformationStationEntity.setCreateTime(new Date());
            lineBaseInformationStationEntity.setModifiedBy(loginUsername);
            lineBaseInformationStationEntity.setModifiedTime(new Date());
            lineBaseInformationStationEntity.setLineBaseInformationId(lineBaseInformationId);
            lineBaseInformationStationEntity.setName(row.get(1));
            String dianwuduanNames = row.get(2);
            String[] dianwuduanNameArr = StrUtil.split(dianwuduanNames, ",");
            List<Integer> dianwuduanIds = new ArrayList<>();
            for (String s : dianwuduanNameArr) {
                List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByName(s);
                if (dianWuDuanEntities.isEmpty())
                    throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "单位名称不存在");
                dianwuduanIds.add((int) dianWuDuanEntities.get(0).getDid());
            }
            lineBaseInformationStationEntity.setWorkshop(row.get(3));
            lineBaseInformationStationEntity.setWorkarea(row.get(4));
            lineBaseInformationStationEntity.setQuduanNum(Integer.parseInt(row.get(5)));
            lineBaseInformationStationEntity.setDianmahuaNum(Integer.parseInt(row.get(6)));
            lineBaseInformationStationEntity.setAxlePoints(Integer.parseInt(row.get(7)));
            lineBaseInformationStationEntity.setInfoNum(Integer.parseInt(row.get(8)));
            lineBaseInformationStationEntity.setHardwareMaterialCode(row.get(9));
            lineBaseInformationStationEntity.setSoftwareMaterialCode(row.get(10));
            lineBaseInformationStationEntity.setEquipmentNumber(Integer.parseInt(row.get(11)));
            lineBaseInformationStationEntity.setTerminalVersion(row.get(12));
            lineBaseInformationStationEntity.setConfigurationFile(row.get(13));
            lineBaseInformationStationEntity.setOpenTime(DateUtil.parseDateTime(row.get(14)));
            lineBaseInformationStationEntity.setLength(Double.parseDouble(row.get(15)));
            lineBaseInformationStationEntity.setVersion(row.get(16));
            this.add(lineBaseInformationStationEntity);
            for (Integer dianwuduanId : dianwuduanIds) {
                LineBaseInformationStationUnitEntity lineBaseInformationStationUnitEntity = new LineBaseInformationStationUnitEntity();
                lineBaseInformationStationUnitEntity.setCreateBy(lineBaseInformationStationEntity.getCreateBy());
                lineBaseInformationStationUnitEntity.setCreateTime(lineBaseInformationStationEntity.getCreateTime());
                lineBaseInformationStationUnitEntity.setModifiedBy(lineBaseInformationStationEntity.getModifiedBy());
                lineBaseInformationStationUnitEntity.setModifiedTime(lineBaseInformationStationEntity.getModifiedTime());
                lineBaseInformationStationUnitEntity.setLineBaseInformationStationId(lineBaseInformationStationEntity.getId());
                lineBaseInformationStationUnitEntity.setUnitId(dianwuduanId);
                lineBaseInformationStationUnitService.add(lineBaseInformationStationUnitEntity);
            }
        }
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "车站基本信息列表";
        //excel表名
        String[] headers = {"序号", "站名", "电务段", "车间", "工区", "轨道区段数", "电码化套数", "技轴点数", "安全信息套数", "设备硬件物料编码", "软件物料编码", "设备数量", "维护终端/诊断主机版本", "配置文件",
                "开通时间", "长度里程", "版本"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "车站基本信息列表";
        //excel表名
        String[] headers = {"序号", "所属线段", "站名", "电务段", "车间", "工区", "轨道区段数", "电码化套数", "技轴点数", "安全信息套数", "设备硬件物料编码", "软件物料编码", "设备数量", "维护终端/诊断主机版本", "配置文件",
                "开通时间", "长度里程", "版本"};
        //获取数据
        List<LineBaseInformationStationEntity> lineBaseInformationStationEntities = this.findByExample(null, null, null, ids);
        lineBaseInformationStationEntities = lineBaseInformationStationEntities.stream()
                .sorted(Comparator.comparing(LineBaseInformationStationEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[lineBaseInformationStationEntities.size()][headers.length];
        for (int i = 0; i < lineBaseInformationStationEntities.size(); i++) {
            LineBaseInformationStationEntity lineBaseInformationStationEntity = lineBaseInformationStationEntities.get(i);
            Integer id = lineBaseInformationStationEntity.getId();
            content[i][0] = id.toString();
            content[i][1] = lineBaseInformationStationEntity.getLineBaseInformationEntity().getName();
            content[i][2] = lineBaseInformationStationEntity.getName();
            content[i][3] = ArrayUtil.toString(lineBaseInformationStationEntity.getDianWuDuanEntities().stream().map(DianWuDuanEntity::getDwdName).toArray());
            content[i][4] = lineBaseInformationStationEntity.getWorkshop();
            content[i][5] = lineBaseInformationStationEntity.getWorkarea();
            content[i][6] = lineBaseInformationStationEntity.getQuduanNum() == null ? "" : lineBaseInformationStationEntity.getQuduanNum().toString();
            content[i][7] = lineBaseInformationStationEntity.getDianmahuaNum() == null ? "" : lineBaseInformationStationEntity.getDianmahuaNum().toString();
            content[i][8] = lineBaseInformationStationEntity.getAxlePoints() == null ? "" : lineBaseInformationStationEntity.getAxlePoints().toString();
            content[i][9] = lineBaseInformationStationEntity.getInfoNum() == null ? "" : lineBaseInformationStationEntity.getInfoNum().toString();
            content[i][10] = lineBaseInformationStationEntity.getHardwareMaterialCode();
            content[i][11] = lineBaseInformationStationEntity.getSoftwareMaterialCode();
            content[i][12] = lineBaseInformationStationEntity.getEquipmentNumber() == null ? "" : lineBaseInformationStationEntity.getEquipmentNumber().toString();
            content[i][13] = lineBaseInformationStationEntity.getTerminalVersion();
            content[i][14] = lineBaseInformationStationEntity.getConfigurationFile();
            content[i][15] = DateUtil.formatDateTime(lineBaseInformationStationEntity.getOpenTime());
            content[i][16] = lineBaseInformationStationEntity.getLength() == null ? "" : lineBaseInformationStationEntity.getLength().toString();
            content[i][17] = lineBaseInformationStationEntity.getVersion();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
