package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    public JSONObject findByCzIdAndProperties(Integer czId, Integer[] properties) {
        JSONObject jo = new JSONObject(true);
        if (properties == null || properties.length == 0) {
            jo.put("title", new JSONArray());
            jo.put("data", new JSONArray());
            return jo;
        }
        //表头数组
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = new ArrayList<>();
        quDuanInfoPropertyEntities.add(new QuDuanInfoPropertyEntity(-1, "区段运用名称", null));
        quDuanInfoPropertyEntities.addAll(quDuanInfoPropertyService.findByIds(properties));
        List<TreeNodeUtil> treeNodeUtils = this.findTitle(quDuanInfoPropertyEntities);
        jo.put("title", treeNodeUtils);

        //表头对应数据数组
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzId(czId);
        JSONArray dataJa = new JSONArray();
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
            QuDuanInfoEntityV2 quDuanInfoEntityV2 = quDuanInfoService.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
            if (quDuanInfoEntityV2 == null) {
                quDuanInfoEntityV2 = new QuDuanInfoEntityV2();
            }
            quDuanInfoEntityV2.setQuDuanBaseEntity(quDuanBaseEntity);
            JSONObject jsonObject = this.findDate(quDuanInfoPropertyEntities, quDuanInfoEntityV2);
            dataJa.add(jsonObject);
        }
        jo.put("data", dataJa);

        return jo;
    }

    public List<TreeNodeUtil> findTitle(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities) {
        List<TreeNodeUtil> t = new ArrayList<>();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
        treeNodeUtil1.setId(1L);
        treeNodeUtil1.setValue("实测值");
        TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
        treeNodeUtil2.setId(2L);
        treeNodeUtil2.setValue("上限");
        TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
        treeNodeUtil3.setId(3L);
        treeNodeUtil3.setValue("下限");
        TreeNodeUtil treeNodeUtil4 = new TreeNodeUtil();
        treeNodeUtil4.setId(4L);
        treeNodeUtil4.setValue("跳变系数");
        treeNodeUtils.add(treeNodeUtil1);
        treeNodeUtils.add(treeNodeUtil2);
        treeNodeUtils.add(treeNodeUtil3);
        treeNodeUtils.add(treeNodeUtil4);
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(quDuanInfoPropertyEntity.getId().longValue());
            treeNodeUtil.setValue(quDuanInfoPropertyEntity.getName());
            if (quDuanInfoPropertyEntity.getId() != -1) {
                treeNodeUtil.setChildren(treeNodeUtils);
            } else {
                treeNodeUtil.setChildren(new ArrayList<>());
            }
            t.add(treeNodeUtil);
        }
        return t;
    }


    public JSONObject findDate(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2) {
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id", quDuanInfoEntityV2.getId());
        jsonObject.put("cid", quDuanInfoEntityV2.getCid());
        jsonObject.put("qid", quDuanInfoEntityV2.getQid());
        jsonObject.put("time", quDuanInfoEntityV2.getTime());
        jsonObject.put("type", quDuanInfoEntityV2.getType());
        jsonObject.put("dataZhengchang", quDuanInfoEntityV2.getDataZhengchang());
        jsonObject.put("-1", quDuanInfoEntityV2.getQuDuanBaseEntity().getQuduanyunyingName());
        JSONArray ja = new JSONArray();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            if (quDuanInfoPropertyEntity.getId() == -1)
                continue;
            JSONObject jo = new JSONObject();
            MenXianEntity menXianEntity = this.findByCzIdAndQuduanIdAndPropertyId(quDuanInfoEntityV2.getQuDuanBaseEntity().getCzid(),
                    quDuanInfoEntityV2.getQuDuanBaseEntity().getQdid(), quDuanInfoPropertyEntity.getId());
            menXianEntity = menXianEntity == null ? new MenXianEntity() : menXianEntity;
            jo.put("2", menXianEntity.getSuperiorLimitValue() == null ? 0 : menXianEntity.getSuperiorLimitValue());
            jo.put("3", menXianEntity.getLowerLimitValue() == null ? 0 : menXianEntity.getLowerLimitValue());
            jo.put("4", menXianEntity.getOutburstValue() == null ? 0 : menXianEntity.getOutburstValue());
            String idStr = String.valueOf(quDuanInfoPropertyEntity.getId());
            switch (idStr) {
                case "1":
                    jo.put("1", quDuanInfoEntityV2.getDesignCarrier());
                    break;
                case "2":
                    jo.put("1", quDuanInfoEntityV2.getDirection() == null ? null : quDuanInfoEntityV2.getDirection().equals(1) ? "正向" : quDuanInfoEntityV2.getDirection().equals(2) ? "反向" : "无效");
                    break;
                case "3":
                    jo.put("1", quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "4":
                    jo.put("1", quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "5":
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei().toString());
                    jo.put("1", jsonArray5.get(0) + "/" + jsonArray5.get(1));
                    break;
                case "6":
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jo.put("1", jsonArray6.get(0) + "/" + jsonArray6.get(1));
                    break;
                case "7":
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jo.put("1", jsonArray7.get(0) + "/" + jsonArray7.get(1));
                    break;
                case "8":
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jo.put("1", jsonArray8.get(0) + "/" + jsonArray8.get(1));
                    break;
                case "9":
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jo.put("1", jsonArray9.get(0) + "/" + jsonArray9.get(1));
                    break;
                case "10":
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jo.put("1", jsonArray10.get(0) + "/" + jsonArray10.get(1));
                    break;
                case "11":
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jo.put("1", jsonArray11.get(0) + "/" + jsonArray11.get(1));
                    break;
                case "12":
                    jo.put("1", quDuanInfoEntityV2.getVSongduanCable());
                    break;
                case "13":
                    jo.put("1", quDuanInfoEntityV2.getMaSongduanCable());
                    break;
                case "14":
                    jo.put("1", quDuanInfoEntityV2.getVShouduanCableHost());
                    break;
                case "15":
                    jo.put("1", quDuanInfoEntityV2.getVShouduanCableSpare());
                    break;
                case "16":
                    jo.put("1", quDuanInfoEntityV2.getMaShouduanCable());
                    break;
                case "17":
                    jo.put("1", quDuanInfoEntityV2.getVInAll());
                    break;
                case "18":
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jo.put("1", jsonArray18.get(0) + "/" + jsonArray18.get(1));
                    break;
                case "19":
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jo.put("1", jsonArray19.get(0) + "/" + jsonArray19.get(1));
                    break;
                case "20":
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jo.put("1", jsonArray20.get(0) + "/" + jsonArray20.get(1));
                    break;
                case "21":
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jo.put("1", jsonArray21.get(0) + "/" + jsonArray21.get(1));
                    break;
                case "22":
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionZhu() == null ? null : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionBing() == null ? null : quDuanInfoEntityV2.getGjRearCollectionBing().equals("10") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionBing().equals("1") ? "吸起" : "无效");
                    jo.put("1", jsonArray22.get(0) + "/" + jsonArray22.get(1));
                    break;
                case "23":
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jo.put("1", jsonArray23.get(0) + "/" + jsonArray23.get(1));
                    break;

                case "24":
                    jo.put("1", quDuanInfoEntityV2.getMaCableFbp());
                    break;
                case "25":
                    jo.put("1", quDuanInfoEntityV2.getALonginFbp());
                    break;
                case "26":
                    jo.put("1", quDuanInfoEntityV2.getALongoutFbp());
                    break;
                case "27":
                    jo.put("1", quDuanInfoEntityV2.getAShortinFbp());
                    break;
                case "28":
                    jo.put("1", quDuanInfoEntityV2.getAShortoutFbp());
                    break;
                case "29":
                    jo.put("1", quDuanInfoEntityV2.getTFbp());
                    break;

                case "30":
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jo.put("1", jsonArray30.get(0) + "/" + jsonArray30.get(1));
                    break;
                case "31":
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jo.put("1", jsonArray31.get(0) + "/" + jsonArray31.get(1));
                    break;
                case "32":
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jo.put("1", jsonArray32.get(0) + "/" + jsonArray32.get(1));
                    break;
                case "33":
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jo.put("1", jsonArray33.get(0) + "/" + jsonArray33.get(1));
                    break;
                case "34":
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jo.put("1", jsonArray34.get(0) + "/" + jsonArray34.get(1));
                    break;
                case "35":
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jo.put("1", jsonArray35.get(0) + "/" + jsonArray35.get(1));
                    break;
                case "36":
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jo.put("1", jsonArray36.get(0) + "/" + jsonArray36.get(1));
                    break;
                case "37":
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jo.put("1", jsonArray37.get(0) + "/" + jsonArray37.get(1));
                    break;
                case "38":
                    jo.put("1", quDuanInfoEntityV2.getMaCableJbp());
                    break;
                case "39":
                    jo.put("1", quDuanInfoEntityV2.getALonginJbp());
                    break;
                case "40":
                    jo.put("1", quDuanInfoEntityV2.getALongoutJbp());
                    break;
                case "41":
                    jo.put("1", quDuanInfoEntityV2.getAShortinJbp());
                    break;
                case "42":
                    jo.put("1", quDuanInfoEntityV2.getAShortoutJbp());
                    break;
                case "43":
                    jo.put("1", quDuanInfoEntityV2.getTJbp());
                    break;
            }
            ja.add(jo);
        }
        jsonObject.put("children", ja);
        return jsonObject;
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
