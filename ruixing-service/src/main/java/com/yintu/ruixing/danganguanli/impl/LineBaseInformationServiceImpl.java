package com.yintu.ruixing.danganguanli.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationService;
import com.yintu.ruixing.danganguanli.LineBaseInformationUnitEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationUnitService;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanService;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.master.danganguanli.LineBaseInformationDao;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanEntity;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:46
 * @Version: 1.0
 */
@Service
@Transactional
public class LineBaseInformationServiceImpl implements LineBaseInformationService {
    @Autowired
    private LineBaseInformationDao lineBaseInformationDao;
    @Autowired
    private LineBaseInformationUnitService lineBaseInformationUnitService;
    @Autowired
    private DataStatsService dataStatsService;
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeService anZhuangTiaoShiCheZhanXiangMuTypeService;

    @Override
    public void add(LineBaseInformationEntity entity) {
        lineBaseInformationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineBaseInformationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineBaseInformationEntity entity) {
        lineBaseInformationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineBaseInformationEntity findById(Integer id) {
        return lineBaseInformationDao.selectByPrimaryKey(id);
    }


    @Override
    public void add(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds) {
        this.add(lineBaseInformationEntity);
        for (Integer unitId : unitIds) {
            LineBaseInformationUnitEntity lineBaseInformationUnitEntity = new LineBaseInformationUnitEntity();
            lineBaseInformationUnitEntity.setCreateBy(lineBaseInformationEntity.getCreateBy());
            lineBaseInformationUnitEntity.setCreateTime(lineBaseInformationEntity.getCreateTime());
            lineBaseInformationUnitEntity.setModifiedBy(lineBaseInformationEntity.getModifiedBy());
            lineBaseInformationUnitEntity.setModifiedTime(lineBaseInformationEntity.getModifiedTime());
            lineBaseInformationUnitEntity.setLineBaseInformationId(lineBaseInformationEntity.getId());
            lineBaseInformationUnitEntity.setUnitId(unitId);
            lineBaseInformationUnitService.add(lineBaseInformationUnitEntity);
        }

    }

    @Override
    public void edit(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds) {
        this.edit(lineBaseInformationEntity);
        lineBaseInformationUnitService.removeByLineBaseInformationId(lineBaseInformationEntity.getId());
        for (Integer unitId : unitIds) {
            LineBaseInformationUnitEntity lineBaseInformationUnitEntity = new LineBaseInformationUnitEntity();
            lineBaseInformationUnitEntity.setCreateBy(lineBaseInformationEntity.getModifiedBy());
            lineBaseInformationUnitEntity.setCreateTime(lineBaseInformationEntity.getModifiedTime());
            lineBaseInformationUnitEntity.setModifiedBy(lineBaseInformationEntity.getModifiedBy());
            lineBaseInformationUnitEntity.setModifiedTime(lineBaseInformationEntity.getModifiedTime());
            lineBaseInformationUnitEntity.setLineBaseInformationId(lineBaseInformationEntity.getId());
            lineBaseInformationUnitEntity.setUnitId(unitId);
            lineBaseInformationUnitService.add(lineBaseInformationUnitEntity);
        }

    }

    @Override
    public List<Map<String, Object>> findRailwaysBureauTid() {
        return lineBaseInformationDao.selectRailwaysBureauTid();
    }

    @Override
    public List<Map<String, Object>> findByTid(Integer tid) {
        return lineBaseInformationDao.selectByTid(tid);
    }

    @Override
    public List<Map<String, Object>> findStationById(Integer id) {
        return lineBaseInformationDao.selectStationById(id);
    }

    @Override
    public List<TreeNodeUtil> findTree() {
        List<Map<String, Object>> maps1 = this.findRailwaysBureauTid();
        List<TreeNodeUtil> first = new ArrayList<>();
        for (Map<String, Object> map1 : maps1) {
            Integer tid = (Integer) map1.get("tid");
            List<Map<String, Object>> maps2 = this.findByTid(tid);
            List<TreeNodeUtil> second = new ArrayList<>();
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setId((long) tid);
            treeNodeUtil1.setValue(String.valueOf(tid - 1000000L));
            treeNodeUtil1.setLabel((String) map1.get("tlj_name"));
            treeNodeUtil1.setChildren(second);
            for (Map<String, Object> map2 : maps2) {
                Integer id = (Integer) map2.get("id");
                List<Map<String, Object>> maps3 = this.findStationById(id);
                List<TreeNodeUtil> third = new ArrayList<>();
                TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                treeNodeUtil2.setId((long) id);
                treeNodeUtil2.setValue(String.valueOf(id));
                treeNodeUtil2.setLabel((String) map2.get("name"));
                treeNodeUtil2.setChildren(third);
                for (Map<String, Object> map3 : maps3) {
                    Integer stationId = (Integer) map3.get("id");
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil3.setId((long) stationId);
                    treeNodeUtil3.setValue(String.valueOf(stationId + 1000000L));
                    treeNodeUtil3.setLabel((String) map3.get("name"));
                    third.add(treeNodeUtil3);
                }
                second.add(treeNodeUtil2);
            }
            first.add(treeNodeUtil1);
        }
        return first;
    }


    @Override
    public List<LineBaseInformationEntity> findNewLineByTid(Integer tid) {
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationDao.selectNewLineByTid(tid);
        for (LineBaseInformationEntity lineBaseInformationEntity : lineBaseInformationEntities) {
            lineBaseInformationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationEntity.getId()));
        }
        return lineBaseInformationEntities;
    }

    @Override
    public List<LineBaseInformationEntity> findHistoryByExample(Integer tid, Integer id, String name, Integer[] ids) {
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationDao.selectHistoryByExample(tid, id, name, ids);
        for (LineBaseInformationEntity lineBaseInformationEntity : lineBaseInformationEntities) {
            lineBaseInformationEntity.setDianWuDuanEntities(this.findDianWuDuanEntityById(lineBaseInformationEntity.getId()));
        }
        return lineBaseInformationEntities;
    }


    @Override
    public List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id) {
        return lineBaseInformationDao.selectDianWuDuanEntityById(id);
    }

    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "线段技术状态列表";
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
    public void importDate(String[][] context, String loginUsername) {
        for (int i = 0; i < context.length; i++) {
            String[] row = context[i];
            LineBaseInformationEntity lineBaseInformationEntity = new LineBaseInformationEntity();
            lineBaseInformationEntity.setCreateBy(loginUsername);
            lineBaseInformationEntity.setCreateTime(new Date());
            lineBaseInformationEntity.setModifiedBy(loginUsername);
            lineBaseInformationEntity.setModifiedTime(new Date());
            String tName = row[1];
            List<TieLuJuEntity> t = dataStatsService.findAllTieLuJuByName(tName);
            if (t.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "铁路局名称不存在");
            lineBaseInformationEntity.setTid((int) t.get(0).getTid());
            lineBaseInformationEntity.setName(row[2]);
            lineBaseInformationEntity.setShortName(row[3]);
            lineBaseInformationEntity.setLength(Double.parseDouble(row[4]));
            lineBaseInformationEntity.setStationNum(Integer.parseInt(row[5]));

            String unitNames = row[6];
            String[] unitNameArr = StrUtil.split(unitNames, ",");
            List<Integer> unitIds = new ArrayList<>();
            for (String s : unitNameArr) {
                List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByName(s);
                if (dianWuDuanEntities.isEmpty())
                    throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "单位名称不存在");
                unitIds.add((int) dianWuDuanEntities.get(0).getDid());
            }

            String xiangmutypeName = row[7];
            List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> a = anZhuangTiaoShiCheZhanXiangMuTypeService.findXiangMuTypeByName(xiangmutypeName, null, null);
            if (a.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "项目类型名称不存在");
            Integer xiangmutypeId = a.get(0).getId();
            lineBaseInformationEntity.setXiangmutypeId(xiangmutypeId);
            lineBaseInformationEntity.setQuduanNum(Integer.parseInt(row[8]));
            lineBaseInformationEntity.setDianmahuaNum(Integer.parseInt(row[9]));
            lineBaseInformationEntity.setAxlePoints(Integer.parseInt(row[10]));
            lineBaseInformationEntity.setInfoNum(Integer.parseInt(row[11]));
            lineBaseInformationEntity.setTechnologyState(row[12]);
            lineBaseInformationEntity.setOpenTime(DateUtil.parseDateTime(row[13]));
            lineBaseInformationEntity.setGuaranteePeriodInformation(row[14]);
            lineBaseInformationEntity.setModifySituation(row[15]);
            lineBaseInformationEntity.setAuxiliaryProductInformation(row[16]);
            lineBaseInformationEntity.setManufacturerInformation(row[17]);
            lineBaseInformationEntity.setVersion(row[18]);
            this.add(lineBaseInformationEntity);
            for (Integer unitId : unitIds) {
                LineBaseInformationUnitEntity lineBaseInformationUnitEntity = new LineBaseInformationUnitEntity();
                lineBaseInformationUnitEntity.setCreateBy(lineBaseInformationEntity.getCreateBy());
                lineBaseInformationUnitEntity.setCreateTime(lineBaseInformationEntity.getCreateTime());
                lineBaseInformationUnitEntity.setModifiedBy(lineBaseInformationEntity.getModifiedBy());
                lineBaseInformationUnitEntity.setModifiedTime(lineBaseInformationEntity.getModifiedTime());
                lineBaseInformationUnitEntity.setLineBaseInformationId(lineBaseInformationEntity.getId());
                lineBaseInformationUnitEntity.setUnitId(unitId);
                lineBaseInformationUnitService.add(lineBaseInformationUnitEntity);
            }
        }
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "线段技术状态列表";
        //excel表名
        String[] headers = {"序号", "所属铁路局", "线段名称", "项目简称", "长度里程", "站数", "管理单位(多个单位用英文逗号分隔)", "设备类型", "总轨道区段数", "电码化套数", "技轴点数", "安全信息套数", "技术状态", "开通时间", "质保期信息",
                "整改情况", "配套产品信息", "相关信号厂家信息", "状态版本"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "线段技术状态列表";
        //excel表名
        String[] headers = {"序号", "所属铁路局", "线段名称", "项目简称", "长度里程", "站数", "管理单位", "设备类型", "总轨道区段数", "电码化套数", "技轴点数", "安全信息套数", "技术状态", "开通时间", "质保期信息",
                "整改情况", "配套产品信息", "相关信号厂家信息", "状态版本"};
        //获取数据
        List<LineBaseInformationEntity> lineBaseInformationEntities = this.findHistoryByExample(null, null, null, ids);
        lineBaseInformationEntities = lineBaseInformationEntities.stream()
                .sorted(Comparator.comparing(LineBaseInformationEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[lineBaseInformationEntities.size()][headers.length];
        for (int i = 0; i < lineBaseInformationEntities.size(); i++) {
            LineBaseInformationEntity lineBaseInformationEntity = lineBaseInformationEntities.get(i);
            Integer id = lineBaseInformationEntity.getId();
            content[i][0] = id.toString();
            content[i][1] = lineBaseInformationEntity.getTieLuJuEntity().getTljName();
            content[i][2] = lineBaseInformationEntity.getName();
            content[i][3] = lineBaseInformationEntity.getShortName();
            content[i][4] = lineBaseInformationEntity.getLength().toString();
            content[i][5] = lineBaseInformationEntity.getStationNum().toString();
            content[i][6] = ArrayUtil.toString(lineBaseInformationEntity.getDianWuDuanEntities().stream().map(DianWuDuanEntity::getDwdName).toArray());
            content[i][7] = lineBaseInformationEntity.getAnZhuangTiaoShiCheZhanXiangMuTypeEntity().getXiangmuleixing();
            content[i][8] = lineBaseInformationEntity.getQuduanNum().toString();
            content[i][9] = lineBaseInformationEntity.getDianmahuaNum().toString();
            content[i][10] = lineBaseInformationEntity.getAxlePoints().toString();
            content[i][11] = lineBaseInformationEntity.getInfoNum().toString();
            content[i][12] = lineBaseInformationEntity.getTechnologyState();
            content[i][13] = DateUtil.formatDateTime(lineBaseInformationEntity.getOpenTime());
            content[i][14] = lineBaseInformationEntity.getGuaranteePeriodInformation();
            content[i][15] = lineBaseInformationEntity.getModifySituation();
            content[i][16] = lineBaseInformationEntity.getAuxiliaryProductInformation();
            content[i][17] = lineBaseInformationEntity.getManufacturerInformation();
            content[i][18] = lineBaseInformationEntity.getVersion();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
