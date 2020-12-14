package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.guzhangzhenduan.MenXianDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    public void removeBatch(List<MenXianEntity> menXianEntities) {
        menXianDao.deleteBatch(menXianEntities);
    }

    @Override
    public void addBatch(List<MenXianEntity> menXianEntities) {
        menXianDao.insertBatch(menXianEntities);
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
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, null, null);
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
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(quDuanInfoPropertyEntity.getId().longValue());
            treeNodeUtil.setValue(quDuanInfoPropertyEntity.getName());
            if (quDuanInfoPropertyEntity.getId() != -1) {
                List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
                TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                treeNodeUtil1.setId(quDuanInfoPropertyEntity.getId() * 10 + 1L);
                treeNodeUtil1.setValue("实测值");
                TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                treeNodeUtil2.setId(quDuanInfoPropertyEntity.getId() * 10 + 2L);
                treeNodeUtil2.setValue("上限");
                TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                treeNodeUtil3.setId(quDuanInfoPropertyEntity.getId() * 10 + 3L);
                treeNodeUtil3.setValue("下限");
                TreeNodeUtil treeNodeUtil4 = new TreeNodeUtil();
                treeNodeUtil4.setId(quDuanInfoPropertyEntity.getId() * 10 + 4L);
                treeNodeUtil4.setValue("跳变系数");
                treeNodeUtils.add(treeNodeUtil1);
                treeNodeUtils.add(treeNodeUtil2);
                treeNodeUtils.add(treeNodeUtil3);
                treeNodeUtils.add(treeNodeUtil4);
                treeNodeUtil.setChildren(treeNodeUtils);
            } else {
                treeNodeUtil.setChildren(new ArrayList<>());
            }
            t.add(treeNodeUtil);
        }
        return t;
    }


    public JSONObject findDate(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2) {
        JSONObject jo = new JSONObject(true);
        jo.put("id", quDuanInfoEntityV2.getId());
        jo.put("cid", quDuanInfoEntityV2.getCid());
        jo.put("qid", quDuanInfoEntityV2.getQid());
        jo.put("time", quDuanInfoEntityV2.getTime());
        jo.put("type", quDuanInfoEntityV2.getType());
        jo.put("dataZhengchang", quDuanInfoEntityV2.getDataZhengchang());
        jo.put("-1", quDuanInfoEntityV2.getQuDuanBaseEntity().getQuduanyunyingName());
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            if (quDuanInfoPropertyEntity.getId() == -1)
                continue;
            MenXianEntity menXianEntity = this.findByCzIdAndQuduanIdAndPropertyId(quDuanInfoEntityV2.getQuDuanBaseEntity().getCzid(),
                    quDuanInfoEntityV2.getQuDuanBaseEntity().getQdid(), quDuanInfoPropertyEntity.getId());
            menXianEntity = menXianEntity == null ? new MenXianEntity() : menXianEntity;
//            jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 2L), menXianEntity.getSuperiorLimitValue() == null ? 0 : menXianEntity.getSuperiorLimitValue());
//            jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 3L), menXianEntity.getLowerLimitValue() == null ? 0 : menXianEntity.getLowerLimitValue());
//            jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 4L), menXianEntity.getOutburstValue() == null ? 0 : menXianEntity.getOutburstValue());
            String idStr = String.valueOf(quDuanInfoPropertyEntity.getId());
            switch (idStr) {
                case "1":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getDesignCarrier());
                    break;
                case "2":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getDirection() == null ? null : quDuanInfoEntityV2.getDirection().equals(1) ? "正向" : quDuanInfoEntityV2.getDirection().equals(2) ? "反向" : "无效");
                    break;
                case "3":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "4":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "5":
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei().toString());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray5.get(0) + "/" + jsonArray5.get(1));
                    break;
                case "6":
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray6.get(0) + "/" + jsonArray6.get(1));
                    break;
                case "7":
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray7.get(0) + "/" + jsonArray7.get(1));
                    break;
                case "8":
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray8.get(0) + "/" + jsonArray8.get(1));
                    break;
                case "9":
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray9.get(0) + "/" + jsonArray9.get(1));
                    break;
                case "10":
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray10.get(0) + "/" + jsonArray10.get(1));
                    break;
                case "11":
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray11.get(0) + "/" + jsonArray11.get(1));
                    break;
                case "12":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getVSongduanCable());
                    break;
                case "13":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getMaSongduanCable());
                    break;
                case "14":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getVShouduanCableHost());
                    break;
                case "15":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getVShouduanCableSpare());
                    break;
                case "16":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getMaShouduanCable());
                    break;
                case "17":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getVInAll());
                    break;
                case "18":
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray18.get(0) + "/" + jsonArray18.get(1));
                    break;
                case "19":
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray19.get(0) + "/" + jsonArray19.get(1));
                    break;
                case "20":
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray20.get(0) + "/" + jsonArray20.get(1));
                    break;
                case "21":
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray21.get(0) + "/" + jsonArray21.get(1));
                    break;
                case "22":
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionZhu() == null ? null : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionBing() == null ? null : quDuanInfoEntityV2.getGjRearCollectionBing().equals("10") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionBing().equals("1") ? "吸起" : "无效");
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray22.get(0) + "/" + jsonArray22.get(1));
                    break;
                case "23":
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray23.get(0) + "/" + jsonArray23.get(1));
                    break;

                case "24":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getMaCableFbp());
                    break;
                case "25":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getALonginFbp());
                    break;
                case "26":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getALongoutFbp());
                    break;
                case "27":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getAShortinFbp());
                    break;
                case "28":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getAShortoutFbp());
                    break;
                case "29":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getTFbp());
                    break;

                case "30":
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray30.get(0) + "/" + jsonArray30.get(1));
                    break;
                case "31":
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray31.get(0) + "/" + jsonArray31.get(1));
                    break;
                case "32":
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray32.get(0) + "/" + jsonArray32.get(1));
                    break;
                case "33":
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray33.get(0) + "/" + jsonArray33.get(1));
                    break;
                case "34":
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray34.get(0) + "/" + jsonArray34.get(1));
                    break;
                case "35":
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray35.get(0) + "/" + jsonArray35.get(1));
                    break;
                case "36":
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray36.get(0) + "/" + jsonArray36.get(1));
                    break;
                case "37":
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), jsonArray37.get(0) + "/" + jsonArray37.get(1));
                    break;
                case "38":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getMaCableJbp());
                    break;
                case "39":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getALonginJbp());
                    break;
                case "40":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getALongoutJbp());
                    break;
                case "41":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getAShortinJbp());
                    break;
                case "42":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getAShortoutJbp());
                    break;
                case "43":
                    jo.put(String.valueOf(quDuanInfoPropertyEntity.getId() * 10 + 1L), quDuanInfoEntityV2.getTJbp());
                    break;
            }

        }
        return jo;
    }


    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFDatas(new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFDatas(new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        return content;
    }

    @Override
    public void importData(String[][] context, String loginUserName) {
        List<MenXianEntity> menXianEntities = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            JSONArray jsonArray = (JSONArray) JSONArray.toJSON(context[i]);
            Integer cid = jsonArray.getInteger(0);
            Integer qid = jsonArray.getInteger(2);
            List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(cid, qid, null, null);
            if (!quDuanBaseEntities.isEmpty()) {
                //（主机/备机）功出电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 4);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity4 = new MenXianEntity();
                    menXianEntity4.setCzId(cid);
                    menXianEntity4.setQuduanId(qid);
                    menXianEntity4.setPropertyId(4);
                    menXianEntity4.setLevel(1 + j);
                    menXianEntity4.setLcLower(jsonArray.getString(4));
                    menXianEntity4.setLcSuperior(jsonArray.getString(5));
                    menXianEntity4.setAlarmLower(jsonArray.getString(6));
                    menXianEntity4.setAlarmSuperior(jsonArray.getString(8));
                    menXianEntities.add(menXianEntity4);
                }
                //主机功出电流mA
                //备机功出电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 5);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity5 = new MenXianEntity();
                    menXianEntity5.setCzId(cid);
                    menXianEntity5.setQuduanId(qid);
                    menXianEntity5.setPropertyId(5);
                    menXianEntity5.setLevel(1 + j);
                    menXianEntity5.setLcLower(jsonArray.getString(9 + j * 5));
                    menXianEntity5.setLcSuperior(jsonArray.getString(10 + j * 5));
                    menXianEntity5.setAlarmLower(jsonArray.getString(11 + j * 5));
                    menXianEntity5.setAlarmSuperior(jsonArray.getString(13 + j * 5));
                    menXianEntities.add(menXianEntity5);
                }

                //（主机/备机）上边频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 6);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity6 = new MenXianEntity();
                    menXianEntity6.setCzId(cid);
                    menXianEntity6.setQuduanId(qid);
                    menXianEntity6.setPropertyId(6);
                    menXianEntity6.setLevel(1 + j);
                    menXianEntity6.setLcLower(jsonArray.getString(19));
                    menXianEntity6.setLcSuperior(jsonArray.getString(20));
                    menXianEntity6.setAlarmLower(jsonArray.getString(21));
                    menXianEntity6.setAlarmSuperior(jsonArray.getString(23));
                    menXianEntities.add(menXianEntity6);
                }
                //（主机/备机）下边频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 7);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity7 = new MenXianEntity();
                    menXianEntity7.setCzId(cid);
                    menXianEntity7.setQuduanId(qid);
                    menXianEntity7.setPropertyId(7);
                    menXianEntity7.setLevel(1 + j);
                    menXianEntity7.setLcLower(jsonArray.getString(24));
                    menXianEntity7.setLcSuperior(jsonArray.getString(25));
                    menXianEntity7.setAlarmLower(jsonArray.getString(26));
                    menXianEntity7.setAlarmSuperior(jsonArray.getString(28));
                    menXianEntities.add(menXianEntity7);
                }
                //（主机/备机）发送低频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 8);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity8 = new MenXianEntity();
                    menXianEntity8.setCzId(cid);
                    menXianEntity8.setQuduanId(qid);
                    menXianEntity8.setPropertyId(8);
                    menXianEntity8.setLevel(1 + j);
                    menXianEntity8.setLcLower(jsonArray.getString(29));
                    menXianEntity8.setLcSuperior(jsonArray.getString(30));
                    menXianEntity8.setAlarmLower(jsonArray.getString(31));
                    menXianEntity8.setAlarmSuperior(jsonArray.getString(33));
                    menXianEntities.add(menXianEntity8);
                }
                //送端电缆侧电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 11);
                MenXianEntity menXianEntity11 = new MenXianEntity();
                menXianEntity11.setCzId(cid);
                menXianEntity11.setQuduanId(qid);
                menXianEntity11.setPropertyId(11);
                menXianEntity11.setLevel(1);
                menXianEntity11.setLcLower(jsonArray.getString(34));
                menXianEntity11.setLcSuperior(jsonArray.getString(35));
                menXianEntity11.setAlarmLower(jsonArray.getString(36));
                menXianEntity11.setAlarmSuperior(jsonArray.getString(38));
                menXianEntities.add(menXianEntity11);


                //送端电缆侧电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 12);
                MenXianEntity menXianEntity12 = new MenXianEntity();
                menXianEntity12.setCzId(cid);
                menXianEntity12.setQuduanId(qid);
                menXianEntity12.setPropertyId(12);
                menXianEntity12.setLevel(1);
                menXianEntity12.setLcLower(jsonArray.getString(39));
                menXianEntity12.setLcSuperior(jsonArray.getString(40));
                menXianEntity12.setAlarmLower(jsonArray.getString(41));
                menXianEntity12.setAlarmSuperior(jsonArray.getString(43));
                menXianEntities.add(menXianEntity12);

                //受端电缆侧主电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 13);
                MenXianEntity menXianEntity13 = new MenXianEntity();
                menXianEntity13.setCzId(cid);
                menXianEntity13.setQuduanId(qid);
                menXianEntity13.setPropertyId(13);
                menXianEntity13.setLevel(1);
                menXianEntity13.setLcLower(jsonArray.getString(44));
                menXianEntity13.setLcSuperior(jsonArray.getString(45));
                menXianEntity13.setAlarmLowerK(jsonArray.getString(46));
                menXianEntity13.setAlarmLowerZ(jsonArray.getString(47));
                menXianEntity13.setAlarmSuperior(jsonArray.getString(48));
                menXianEntities.add(menXianEntity13);

                //受端电缆侧调电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 14);
                MenXianEntity menXianEntity14 = new MenXianEntity();
                menXianEntity14.setCzId(cid);
                menXianEntity14.setQuduanId(qid);
                menXianEntity14.setPropertyId(14);
                menXianEntity14.setLevel(1);
                menXianEntity14.setLcLower(jsonArray.getString(49));
                menXianEntity14.setLcSuperior(jsonArray.getString(50));
                menXianEntity14.setAlarmLowerK(jsonArray.getString(51));
                menXianEntity14.setAlarmLowerZ(jsonArray.getString(52));
                menXianEntity14.setAlarmSuperior(jsonArray.getString(53));
                menXianEntities.add(menXianEntity14);

                //受端电缆侧电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 15);
                MenXianEntity menXianEntity15 = new MenXianEntity();
                menXianEntity15.setCzId(cid);
                menXianEntity15.setQuduanId(qid);
                menXianEntity15.setPropertyId(15);
                menXianEntity15.setLevel(1);
                menXianEntity15.setLcLower(jsonArray.getString(54));
                menXianEntity15.setLcSuperior(jsonArray.getString(55));
                menXianEntity15.setAlarmLowerK(jsonArray.getString(56));
                menXianEntity15.setAlarmLowerZ(jsonArray.getString(57));
                menXianEntity15.setAlarmSuperior(jsonArray.getString(58));
                menXianEntities.add(menXianEntity15);


                //轨入电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 16);
                MenXianEntity menXianEntity16 = new MenXianEntity();
                menXianEntity16.setCzId(cid);
                menXianEntity16.setQuduanId(qid);
                menXianEntity16.setPropertyId(16);
                menXianEntity16.setLevel(1);
                menXianEntity16.setLcLower(jsonArray.getString(59));
                menXianEntity16.setLcSuperior(jsonArray.getString(60));
                menXianEntity16.setAlarmLowerK(jsonArray.getString(61));
                menXianEntity16.setAlarmLowerZ(jsonArray.getString(62));
                menXianEntity16.setAlarmSuperior(jsonArray.getString(63));
                menXianEntities.add(menXianEntity16);


                //（主机/并机）主接入电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 17);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity17 = new MenXianEntity();
                    menXianEntity17.setCzId(cid);
                    menXianEntity17.setQuduanId(qid);
                    menXianEntity17.setPropertyId(17);
                    menXianEntity17.setLevel(1 + j);
                    menXianEntity17.setLcLower(jsonArray.getString(64));
                    menXianEntity17.setLcSuperior(jsonArray.getString(65));
                    menXianEntity17.setAlarmLower(jsonArray.getString(66));
                    menXianEntity17.setAlarmSuperiorK(jsonArray.getString(67));
                    menXianEntity17.setAlarmSuperiorZ(jsonArray.getString(68));
                    menXianEntities.add(menXianEntity17);
                }
                //（主机/并机）调接入电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 18);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity18 = new MenXianEntity();
                    menXianEntity18.setCzId(cid);
                    menXianEntity18.setQuduanId(qid);
                    menXianEntity18.setPropertyId(18);
                    menXianEntity18.setLevel(1 + j);
                    menXianEntity18.setLcLower(jsonArray.getString(69));
                    menXianEntity18.setLcSuperior(jsonArray.getString(70));
                    menXianEntity18.setAlarmLowerK(jsonArray.getString(71));
                    menXianEntity18.setAlarmLowerZ(jsonArray.getString(72));
                    menXianEntity18.setAlarmSuperiorK(jsonArray.getString(73));
                    menXianEntities.add(menXianEntity18);
                }
                //（主机/并机）接收低频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 19);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity19 = new MenXianEntity();
                    menXianEntity19.setCzId(cid);
                    menXianEntity19.setQuduanId(qid);
                    menXianEntity19.setPropertyId(19);
                    menXianEntity19.setLevel(1 + j);
                    menXianEntity19.setLcLower(jsonArray.getString(74));
                    menXianEntity19.setLcSuperior(jsonArray.getString(75));
                    menXianEntity19.setAlarmLowerK(jsonArray.getString(76));
                    menXianEntity19.setAlarmLowerZ(jsonArray.getString(77));
                    menXianEntity19.setAlarmSuperior(jsonArray.getString(78));
                    menXianEntities.add(menXianEntity19);
                }


                //FBP电缆电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 24);
                MenXianEntity menXianEntity24 = new MenXianEntity();
                menXianEntity24.setCzId(cid);
                menXianEntity24.setQuduanId(qid);
                menXianEntity24.setPropertyId(24);
                menXianEntity24.setLevel(1);
                menXianEntity24.setLcLower(jsonArray.getString(79));
                menXianEntity24.setLcSuperior(jsonArray.getString(80));
                menXianEntity24.setAlarmLowerK(jsonArray.getString(81));
                menXianEntity24.setAlarmSuperior(jsonArray.getString(83));
                menXianEntities.add(menXianEntity24);

                //FBP电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 25);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 26);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 27);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 28);
                for (int j = 0; j < 4; j++) {
                    MenXianEntity menXianEntity25 = new MenXianEntity();
                    menXianEntity25.setCzId(cid);
                    menXianEntity25.setQuduanId(qid);
                    menXianEntity25.setPropertyId(25 + j);
                    menXianEntity25.setLevel(1);
                    menXianEntity25.setLcLower(jsonArray.getString(84));
                    menXianEntity25.setLcSuperior(jsonArray.getString(85));
                    menXianEntity25.setAlarmLower(jsonArray.getString(86));
                    menXianEntity25.setAlarmSuperior(jsonArray.getString(88));
                    menXianEntities.add(menXianEntity25);
                }
                //FBP温度℃
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 29);
                MenXianEntity menXianEntity29 = new MenXianEntity();
                menXianEntity29.setCzId(cid);
                menXianEntity29.setQuduanId(qid);
                menXianEntity29.setPropertyId(29);
                menXianEntity29.setLevel(1);
                menXianEntity29.setLcLower(jsonArray.getString(89));
                menXianEntity29.setLcSuperior(jsonArray.getString(90));
                menXianEntity29.setAlarmLower(jsonArray.getString(91));
                menXianEntity29.setAlarmSuperior(jsonArray.getString(93));
                menXianEntities.add(menXianEntity29);


                //主信号FBA电流A（长内/长外/短内/短外）
                //调信号FBA电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 30);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 31);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 32);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 33);
                for (int z = 0; z < 2; z++) {
                    for (int j = 0; j < 4; j++) {
                        MenXianEntity menXianEntity30 = new MenXianEntity();
                        menXianEntity30.setCzId(cid);
                        menXianEntity30.setQuduanId(qid);
                        menXianEntity30.setPropertyId(30 + j);
                        menXianEntity30.setLevel(1 + z);
                        menXianEntity30.setLcLower(jsonArray.getString(94 + z * 5));
                        menXianEntity30.setLcSuperior(jsonArray.getString(95 + z * 5));
                        menXianEntity30.setAlarmLower(jsonArray.getString(96 + z * 5));
                        menXianEntity30.setAlarmSuperior(jsonArray.getString(98 + z * 5));
                        menXianEntities.add(menXianEntity30);
                    }
                }

                //主信号JBA电流A（长内/长外/短内/短外）
                //调信号JBA电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 34);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 35);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 36);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 37);
                for (int z = 0; z < 2; z++) {
                    for (int j = 0; j < 4; j++) {
                        MenXianEntity menXianEntity34 = new MenXianEntity();
                        menXianEntity34.setCzId(cid);
                        menXianEntity34.setQuduanId(qid);
                        menXianEntity34.setPropertyId(34 + j);
                        menXianEntity34.setLevel(1 + z);
                        menXianEntity34.setLcLower(jsonArray.getString(104 + z * 5));
                        menXianEntity34.setLcSuperior(jsonArray.getString(105 + z * 5));
                        menXianEntity34.setAlarmLowerK(jsonArray.getString(106 + z * 5));
                        menXianEntity34.setAlarmLowerZ(jsonArray.getString(107 + z * 5));
                        menXianEntity34.setAlarmSuperior(jsonArray.getString(108 + z * 5));
                        menXianEntities.add(menXianEntity34);
                    }
                }


                //JBP电缆电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 38);
                MenXianEntity menXianEntity38 = new MenXianEntity();
                menXianEntity38.setCzId(cid);
                menXianEntity38.setQuduanId(qid);
                menXianEntity38.setPropertyId(39);
                menXianEntity38.setLevel(1);
                menXianEntity38.setLcLower(jsonArray.getString(114));
                menXianEntity38.setLcSuperior(jsonArray.getString(115));
                menXianEntity38.setAlarmLowerK(jsonArray.getString(116));
                menXianEntity38.setAlarmLowerZ(jsonArray.getString(117));
                menXianEntity38.setAlarmSuperior(jsonArray.getString(118));
                menXianEntities.add(menXianEntity38);

                //JBP电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 39);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 40);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 41);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 42);
                for (int j = 0; j < 4; j++) {
                    MenXianEntity menXianEntity39 = new MenXianEntity();
                    menXianEntity39.setCzId(cid);
                    menXianEntity39.setQuduanId(qid);
                    menXianEntity39.setPropertyId(39 + j);
                    menXianEntity39.setLevel(1);
                    menXianEntity39.setLcLower(jsonArray.getString(119));
                    menXianEntity39.setLcSuperior(jsonArray.getString(120));
                    menXianEntity39.setAlarmLowerK(jsonArray.getString(121));
                    menXianEntity39.setAlarmLowerZ(jsonArray.getString(122));
                    menXianEntity39.setAlarmSuperior(jsonArray.getString(123));
                    menXianEntities.add(menXianEntity39);
                }

                //JBP温度℃
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 43);
                MenXianEntity menXianEntity43 = new MenXianEntity();
                menXianEntity43.setCzId(cid);
                menXianEntity43.setQuduanId(qid);
                menXianEntity43.setPropertyId(43);
                menXianEntity43.setLevel(1);
                menXianEntity43.setLcLower(jsonArray.getString(124));
                menXianEntity43.setLcSuperior(jsonArray.getString(125));
                menXianEntity43.setAlarmLower(jsonArray.getString(126));
                menXianEntity43.setAlarmSuperior(jsonArray.getString(128));
                menXianEntities.add(menXianEntity43);

            }
        }
        if (!menXianEntities.isEmpty()) {
            this.removeBatch(menXianEntities);
            this.addBatch(menXianEntities);
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

    @Override
    public String findMinNumber(Integer czid, Integer qdid, Integer mid) {
        return menXianDao.findMinNumber(czid, qdid, mid);
    }

    @Override
    public String findMaxNumber(Integer czid, Integer qdid, Integer mid) {
        return menXianDao.findMaxNumber(czid, qdid, mid);
    }
}
