package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author:mlf
 * @date:2020/6/3 11:53
 */
@Service
public class QuDuanInfoServiceimpl implements QuDuanInfoService {
    @Autowired
    private CheZhanService cheZhanService;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;

    @Autowired
    private QuDuanBaseService quDuanBaseService;

    @Autowired
    private QuDuanInfoTypesPropertyServiceImpl quDuanInfoTypesPropertyService;

    @Autowired
    private QuDuanInfoPropertyService quDuanInfoPropertyService;


    @Override
    public QuDuanInfoEntityV2 findLastBycZId(Integer czId) {
        return quDuanInfoDaoV2.selectLastByCzId(czId, StringUtil.getTableName(czId, new Date()));
    }

    @Override
    public QuDuanInfoEntityV2 findLastByQid(Integer qid) {
        return quDuanInfoDaoV2.selectLastByQid(qid);
    }

    @Override
    public List<QuDuanInfoEntityV2> findByCzIdAndTime(Integer czId, Date time) {
        return quDuanInfoDaoV2.selectByCzId(czId, time, StringUtil.getTableName(czId, time));
    }

    @Override
    public List<Map<String, Object>> findStatisticsByCzIdAndTime(Integer czId, Date time) {
        return quDuanInfoDaoV2.selectStatisticsByCzIdAndTime(czId, time, StringUtil.getTableName(czId, time));
    }

    @Override
    public QuDuanInfoEntityV2 findFirstByCzId1(Integer czId, Integer qid) {
        return quDuanInfoDaoV2.selectFirstByCzId1(czId, qid, StringUtil.getTableName(czId, new Date()));
    }

    @Override
    public List<QuDuanInfoEntityV2> findByCzIdAndTime1(Integer czId, Date time) {
        return quDuanInfoDaoV2.selectByCzIdAndTime1(czId, time, StringUtil.getTableName(czId, new Date()));
    }


    @Override
    public List<JSONObject> findByCondition(Integer czId, Date time) {
        List<QuDuanInfoTypesPropertyEntity> quDuanInfoTypesPropertyEntities = this.findPropertiesByCzId(czId);
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (time == null) {
            List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzId(czId);
            for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
                QuDuanInfoEntityV2 quDuanInfoEntityV2 = this.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
                if (quDuanInfoEntityV2 == null) {
                    jsonObjects.add(null);
                    continue;
                }
                JSONObject jo = this.convert(quDuanInfoTypesPropertyEntities, quDuanInfoEntityV2);
                jsonObjects.add(jo);
            }
        } else {
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2s = this.findByCzIdAndTime1(czId, time);
            for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2s) {
                JSONObject jo = this.convert(quDuanInfoTypesPropertyEntities, quDuanInfoEntityV2);
                jsonObjects.add(jo);
            }
        }
        return jsonObjects;
    }


    public JSONObject findNullProperties(Integer czId) {
        List<QuDuanInfoTypesPropertyEntity> quDuanInfoTypesPropertyEntities = this.findPropertiesByCzId(czId);
        return convert(quDuanInfoTypesPropertyEntities, new QuDuanInfoEntityV2());
    }

    /**
     * 读取车站配置，根据配置读取不同的属性
     *
     * @param czId 车站id
     * @return
     */
    public List<QuDuanInfoTypesPropertyEntity> findPropertiesByCzId(Integer czId) {
        List<Integer> types = new ArrayList<>();
        CheZhanEntity cheZhanEntity = cheZhanService.findByczId(czId);
        if (cheZhanEntity != null) {
            if (cheZhanEntity.getTongxinbianmaguidaonumber() != null && cheZhanEntity.getTongxinbianmaguidaonumber() > 0)
                types.add(1);
            if (cheZhanEntity.getTongxinbianmazhanneioneguidaonumber() != null && cheZhanEntity.getTongxinbianmazhanneioneguidaonumber() > 0)
                types.add(2);
            if (cheZhanEntity.getJidianonetooneguidaonumber() != null && cheZhanEntity.getJidianonetooneguidaonumber() > 0)
                types.add(3);
            if (cheZhanEntity.getJidianntooneguidaonumber() != null && cheZhanEntity.getJidianntooneguidaonumber() > 0)
                types.add(4);
            if (cheZhanEntity.getJidianntooneshebeinumber() != null && cheZhanEntity.getJidianntooneshebeinumber() > 0)
                types.add(5);
            if (cheZhanEntity.getTongxinbianmadianmahuashebeinumber() != null && cheZhanEntity.getTongxinbianmadianmahuashebeinumber() > 0)
                types.add(6);
            if (cheZhanEntity.getJidianntoonedianmahuashebeinumber() != null && cheZhanEntity.getJidianntoonedianmahuashebeinumber() > 0)
                types.add(7);
            if (cheZhanEntity.getJidianjiashiguidaonumber() != null && cheZhanEntity.getJidianjiashiguidaonumber() > 0)
                types.add(8);
            if (cheZhanEntity.getJidianjiashidianmahuashebeinumber() != null && cheZhanEntity.getJidianjiashidianmahuashebeinumber() > 0)
                types.add(9);
            if (cheZhanEntity.getJiDianDianMaHuaNumber() != null && cheZhanEntity.getJiDianDianMaHuaNumber() > 0)
                types.add(10);
        }
        String type = quDuanInfoTypesPropertyService.countByType(types);//最大值求出配置参数
        return quDuanInfoTypesPropertyService.connectFindByCondition(type);
    }


    public JSONObject convert(List<QuDuanInfoTypesPropertyEntity> quDuanInfoTypesPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2) {
        JSONObject jo = new JSONObject(true);
        jo.put("id", quDuanInfoEntityV2.getId());
        jo.put("cid", quDuanInfoEntityV2.getCid());
        jo.put("qid", quDuanInfoEntityV2.getQid());
        jo.put("time", quDuanInfoEntityV2.getTime());
        jo.put("type", quDuanInfoEntityV2.getType());
        jo.put("dataZhengchang", quDuanInfoEntityV2.getDataZhengchang());
        JSONArray jsonArray = new JSONArray();
        for (QuDuanInfoTypesPropertyEntity quDuanInfoTypesPropertyEntity : quDuanInfoTypesPropertyEntities) {
            JSONObject jsonObject = new JSONObject();
            QuDuanInfoPropertyEntity quDuanInfoPropertyEntity = quDuanInfoTypesPropertyEntity.getQuDuanInfoPropertyEntity();
            jsonObject.put("property", quDuanInfoPropertyEntity.getName());
            switch (quDuanInfoTypesPropertyEntity.getPropertyId()) {
                case 1:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDesignCarrier());
                    jsonObject.put("column", 1);
                    break;
                case 2:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDirection());
                    jsonObject.put("column", 1);
                    break;
                case 3:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("column", 1);
                    break;
                case 4:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("column", 1);
                    break;
                case 5:
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei());
                    jsonObject.put("propertyV", jsonArray5);
                    jsonObject.put("column", 2);
                    break;
                case 6:
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jsonObject.put("propertyV", jsonArray6);
                    jsonObject.put("column", 2);
                    break;
                case 7:
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jsonObject.put("propertyV", jsonArray7);
                    jsonObject.put("column", 2);
                    break;
                case 8:
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jsonObject.put("propertyV", jsonArray8);
                    jsonObject.put("column", 2);
                    break;
                case 9:
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jsonObject.put("propertyV", jsonArray9);
                    jsonObject.put("column", 2);
                    break;
                case 10:
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jsonObject.put("propertyV", jsonArray10);
                    jsonObject.put("column", 2);
                    break;
                case 11:
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jsonObject.put("propertyV", jsonArray11);
                    jsonObject.put("column", 2);
                    break;
                case 12:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVSongduanCable());
                    jsonObject.put("column", 3);
                    break;
                case 13:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaSongduanCable());
                    jsonObject.put("column", 3);
                    break;
                case 14:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVShouduanCableHost());
                    jsonObject.put("column", 3);
                    break;
                case 15:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVShouduanCableSpare());
                    jsonObject.put("column", 3);
                    break;
                case 16:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaShouduanCable());
                    jsonObject.put("column", 3);
                    break;
                case 17:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVInAll());
                    jsonObject.put("column", 4);
                    break;
                case 18:
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jsonObject.put("propertyV", jsonArray18);
                    jsonObject.put("column", 4);
                    break;
                case 19:
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jsonObject.put("propertyV", jsonArray19);
                    jsonObject.put("column", 4);
                    break;
                case 20:
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jsonObject.put("propertyV", jsonArray20);
                    jsonObject.put("column", 4);
                    break;
                case 21:
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jsonObject.put("propertyV", jsonArray21);
                    jsonObject.put("column", 4);
                    break;
                case 22:
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(null == quDuanInfoEntityV2.getGjRearCollectionZhu());
                    jsonArray22.add(null == quDuanInfoEntityV2.getGjRearCollectionBing());
                    jsonObject.put("propertyV", jsonArray22);
                    jsonObject.put("column", 4);
                    break;
                case 23:
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonObject.put("propertyV", jsonArray23);
                    jsonObject.put("column", 4);
                    break;

                case 24:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaCableFbp());
                    jsonObject.put("column", 5);
                    break;
                case 25:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALonginFbp());
                    jsonObject.put("column", 5);
                    break;
                case 26:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALongoutFbp());
                    jsonObject.put("column", 5);
                    break;
                case 27:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortinFbp());
                    jsonObject.put("column", 5);
                    break;
                case 28:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortoutFbp());
                    jsonObject.put("column", 5);
                    break;
                case 29:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getTFbp());
                    jsonObject.put("column", 5);
                    break;

                case 30:
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jsonObject.put("propertyV", jsonArray30);
                    jsonObject.put("column", 6);
                    break;
                case 31:
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jsonObject.put("propertyV", jsonArray31);
                    jsonObject.put("column", 6);
                    break;
                case 32:
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jsonObject.put("propertyV", jsonArray32);
                    jsonObject.put("column", 6);
                    break;
                case 33:
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jsonObject.put("propertyV", jsonArray33);
                    jsonObject.put("column", 6);
                    break;
                case 34:
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jsonObject.put("propertyV", jsonArray34);
                    jsonObject.put("column", 6);
                    break;
                case 35:
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jsonObject.put("propertyV", jsonArray35);
                    jsonObject.put("column", 6);
                    break;
                case 36:
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jsonObject.put("propertyV", jsonArray36);
                    jsonObject.put("column", 6);
                    break;
                case 37:
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonObject.put("propertyV", jsonArray37);
                    jsonObject.put("column", 6);
                    break;
                case 38:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaCableJbp());
                    jsonObject.put("column", 7);
                    break;
                case 39:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALonginJbp());
                    jsonObject.put("column", 7);
                    break;
                case 40:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALongoutJbp());
                    jsonObject.put("column", 7);
                    break;
                case 41:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortinJbp());
                    jsonObject.put("column", 7);
                    break;
                case 42:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortoutJbp());
                    jsonObject.put("column", 7);
                    break;
                case 43:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getTJbp());
                    jsonObject.put("column", 7);
                    break;
            }
            jsonArray.add(jsonObject);
        }
        jo.put("properties", jsonArray);
        return jo;
    }


    /**
     * 实时报表 属性分类
     *
     * @param czId 车站id
     * @return
     */
    @Override
    public List<TreeNodeUtil> findPropertiesTree(Integer czId) {
        List<QuDuanInfoTypesPropertyEntity> quDuanInfoTypesPropertyEntities = this.findPropertiesByCzId(czId);
        Set<Integer> types = new HashSet<>();
        for (QuDuanInfoTypesPropertyEntity quDuanInfoTypesPropertyEntity : quDuanInfoTypesPropertyEntities) {
            Short type = quDuanInfoTypesPropertyEntity.getQuDuanInfoPropertyEntity().getType();
            if (type != null)
                types.add(type.intValue());
        }

        List<TreeNodeUtil> treeNodeUtils = this.findByTypes(types);
        for (TreeNodeUtil treeNodeUtil : treeNodeUtils) {
            List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.findByType(treeNodeUtil.getId().shortValue());
            List<TreeNodeUtil> trees = new ArrayList<>();
            for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
                TreeNodeUtil tree = new TreeNodeUtil();
                tree.setId(quDuanInfoPropertyEntity.getId().longValue());
                tree.setLabel(quDuanInfoPropertyEntity.getName());
                trees.add(tree);
            }
            treeNodeUtil.setChildren(trees);
        }
        return treeNodeUtils;
    }

    /**
     * 充当属性类别表
     *
     * @param types 类别id
     * @return
     */
    public List<TreeNodeUtil> findByTypes(Set<Integer> types) {
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            long id = i + 1001;
            switch (i) {
                case 0:
                    TreeNodeUtil treeNodeUtil0 = new TreeNodeUtil();
                    treeNodeUtil0.setId(id);
                    treeNodeUtil0.setLabel("发送设备数据");
                    treeNodeUtils.add(treeNodeUtil0);
                    break;
                case 1:
                    TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil1.setId(id);
                    treeNodeUtil1.setLabel("送端模拟电缆数据");
                    treeNodeUtils.add(treeNodeUtil1);
                    break;
                case 2:
                    TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil2.setId(id);
                    treeNodeUtil2.setLabel("受端模拟电缆数据");
                    treeNodeUtils.add(treeNodeUtil2);
                    break;
                case 3:
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil3.setId(id);
                    treeNodeUtil3.setLabel("接收设备数据");
                    treeNodeUtils.add(treeNodeUtil3);
                    break;
                case 4:
                    TreeNodeUtil treeNodeUtil4 = new TreeNodeUtil();
                    treeNodeUtil4.setId(id);
                    treeNodeUtil4.setLabel("FBP采集数据");
                    treeNodeUtils.add(treeNodeUtil4);
                    break;
                case 5:
                    TreeNodeUtil treeNodeUtil5 = new TreeNodeUtil();
                    treeNodeUtil5.setId(id);
                    treeNodeUtil5.setLabel("FBA采集数据");
                    treeNodeUtils.add(treeNodeUtil5);
                    break;
                case 6:
                    TreeNodeUtil treeNodeUtil6 = new TreeNodeUtil();
                    treeNodeUtil6.setId(id);
                    treeNodeUtil6.setLabel("JBA采集数据");
                    treeNodeUtils.add(treeNodeUtil6);
                    break;
                case 7:
                    TreeNodeUtil treeNodeUtil7 = new TreeNodeUtil();
                    treeNodeUtil7.setId(id);
                    treeNodeUtil7.setLabel("JBP采集数据");
                    treeNodeUtils.add(treeNodeUtil7);
                    break;
            }
        }
        return treeNodeUtils;
    }

    @Override
    public List<JSONObject> realTimeReport(Integer[] properties, Integer czId, Date time) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        List<QuDuanInfoEntityV2> quDuanInfoEntityV2s = this.findByCzIdAndTime(czId, time);
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.findByIds(properties);
        for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2s) {
            jsonObjects.add(this.findDate(quDuanInfoPropertyEntities, quDuanInfoEntityV2));
        }
        return jsonObjects;
    }

    public JSONObject findDate(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2) {
        JSONObject jo = new JSONObject(true);
        jo.put("id", quDuanInfoEntityV2.getId());
        jo.put("cid", quDuanInfoEntityV2.getCid());
        jo.put("qid", quDuanInfoEntityV2.getQid());
        jo.put("time", quDuanInfoEntityV2.getTime());
        jo.put("type", quDuanInfoEntityV2.getType());
        jo.put("dataZhengchang", quDuanInfoEntityV2.getDataZhengchang());
        JSONArray jsonArray = new JSONArray();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("id", quDuanInfoPropertyEntity.getId());
            jsonObject.put("property", quDuanInfoPropertyEntity.getName());
            switch (quDuanInfoPropertyEntity.getId()) {
                case 1:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDesignCarrier());
                    break;
                case 2:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDirection());
                    break;
                case 3:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case 4:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("10") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case 5:
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei());
                    jsonObject.put("propertyV", jsonArray5);
                    break;
                case 6:
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jsonObject.put("propertyV", jsonArray6);
                    break;
                case 7:
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jsonObject.put("propertyV", jsonArray7);
                    break;
                case 8:
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jsonObject.put("propertyV", jsonArray8);
                    break;
                case 9:
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jsonObject.put("propertyV", jsonArray9);
                    break;
                case 10:
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jsonObject.put("propertyV", jsonArray10);
                    break;
                case 11:
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("10") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jsonObject.put("propertyV", jsonArray11);
                    break;
                case 12:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVSongduanCable());
                    break;
                case 13:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaSongduanCable());
                    break;
                case 14:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVShouduanCableHost());
                    break;
                case 15:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVShouduanCableSpare());
                    break;
                case 16:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaShouduanCable());
                    break;
                case 17:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getVInAll());
                    break;
                case 18:
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jsonObject.put("propertyV", jsonArray18);
                    break;
                case 19:
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jsonObject.put("propertyV", jsonArray19);
                    break;
                case 20:
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jsonObject.put("propertyV", jsonArray20);
                    break;
                case 21:
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jsonObject.put("propertyV", jsonArray21);
                    break;
                case 22:
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(null == quDuanInfoEntityV2.getGjRearCollectionZhu());
                    jsonArray22.add(null == quDuanInfoEntityV2.getGjRearCollectionBing());
                    jsonObject.put("propertyV", jsonArray22);
                    break;
                case 23:
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonObject.put("propertyV", jsonArray23);
                    break;

                case 24:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaCableFbp());
                    break;
                case 25:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALonginFbp());
                    break;
                case 26:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALongoutFbp());
                    break;
                case 27:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortinFbp());
                    break;
                case 28:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortoutFbp());
                    break;
                case 29:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getTFbp());
                    break;

                case 30:
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jsonObject.put("propertyV", jsonArray30);
                    break;
                case 31:
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jsonObject.put("propertyV", jsonArray31);
                    break;
                case 32:
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jsonObject.put("propertyV", jsonArray32);
                    break;
                case 33:
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jsonObject.put("propertyV", jsonArray33);
                    break;
                case 34:
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jsonObject.put("propertyV", jsonArray34);
                    break;
                case 35:
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jsonObject.put("propertyV", jsonArray35);
                    break;
                case 36:
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jsonObject.put("propertyV", jsonArray36);
                    break;
                case 37:
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonObject.put("propertyV", jsonArray37);
                    break;
                case 38:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getMaCableJbp());
                    break;
                case 39:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALonginJbp());
                    break;
                case 40:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getALongoutJbp());
                    break;
                case 41:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortinJbp());
                    break;
                case 42:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getAShortoutJbp());
                    break;
                case 43:
                    jsonObject.put("propertyV", quDuanInfoEntityV2.getTJbp());
                    break;
            }
            jsonArray.add(jsonObject);
        }
        jo.put("properties", jsonArray);
        jo.put("quDuanBaseEntity", quDuanInfoEntityV2.getQuDuanBaseEntity());
        return jo;
    }


}
