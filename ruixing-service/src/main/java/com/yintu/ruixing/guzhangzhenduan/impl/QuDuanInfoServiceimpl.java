package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.internal.util.StackTraceElements;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private MenXianService menXianService;


    @Override
    public boolean isTableExist(String tableName) {
        return quDuanInfoDaoV2.isTableExist(tableName) > 0;
    }


    @Override
    public QuDuanInfoEntityV2 findFirstByCzId1(Integer czId, Integer qid) {
        String tableName = StringUtil.getTableName(czId, new Date());
        return this.isTableExist(tableName) ? quDuanInfoDaoV2.selectFirstByCzId1(czId, qid, tableName) : null;
    }

    @Override
    public List<QuDuanInfoEntityV2> findByCzIdAndTime1(Integer czId, Integer[] qids, Date startTime, Date endTime) {
        if (startTime.after(endTime))
            throw new BaseRuntimeException("开始时间不能大于结束时间");
        if (DateUtil.month(startTime) == DateUtil.month(endTime)) {
            String tableName = StringUtil.getTableName(czId, startTime);
            return this.isTableExist(tableName) ? quDuanInfoDaoV2.selectByCzIdAndTime1(czId, qids, (int) (startTime.getTime() / 1000), (int) (endTime.getTime() / 1000), tableName) : new ArrayList<>();
        } else {
            String firstTableName = StringUtil.getTableName(czId, startTime);
            List<QuDuanInfoEntityV2> firstQuDuanInfoEntityV2s =
                    this.isTableExist(firstTableName) ? quDuanInfoDaoV2.selectByCzIdAndTime1(czId, qids, (int) (startTime.getTime() / 1000), (int) (DateUtil.endOfDay(startTime).getTime() / 1000), firstTableName) :
                            new ArrayList<>();

            String lastTableName = StringUtil.getTableName(czId, endTime);
            List<QuDuanInfoEntityV2> lastQuDuanInfoEntityV2s =
                    this.isTableExist(firstTableName) ? quDuanInfoDaoV2.selectByCzIdAndTime1(czId, qids, (int) (DateUtil.beginOfDay(endTime).getTime() / 1000), (int) (endTime.getTime() / 1000), lastTableName) : new ArrayList<>();
            firstQuDuanInfoEntityV2s.addAll(lastQuDuanInfoEntityV2s);
            return firstQuDuanInfoEntityV2s;
        }
    }


    public JSONObject findNullProperties(Integer czId) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        return convert(quDuanInfoPropertyEntities, new QuDuanInfoEntityV2(), true);
    }

    @Override
    public List<JSONObject> findByCondition(Integer czId, Date startTime, Date endTime) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (startTime == null || endTime == null) {
            List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, null, false);
            for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
                QuDuanInfoEntityV2 quDuanInfoEntityV2 = this.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
                if (quDuanInfoEntityV2 == null) {
                    jsonObjects.add(null);
                    continue;
                }
                JSONObject jo = this.convert(quDuanInfoPropertyEntities, quDuanInfoEntityV2, false);
                jsonObjects.add(jo);
            }
        } else {
            Integer[] qids = quDuanBaseService.findByCzIdAndQdId(czId, null, null, false)
                    .stream()
                    .map(QuDuanBaseEntity::getQdid)
                    .toArray(Integer[]::new);
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2s = this.findByCzIdAndTime1(czId, qids, startTime, endTime);
            for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2s) {
                JSONObject jo = this.convert(quDuanInfoPropertyEntities, quDuanInfoEntityV2, false);
                jsonObjects.add(jo);
            }
        }
        return jsonObjects;
    }


    /**
     * 读取车站配置，根据配置读取不同的属性
     *
     * @param czId 车站id
     * @return
     */
    public List<QuDuanInfoPropertyEntity> findPropertiesByCzId(Integer czId) {
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
        List<QuDuanInfoTypesPropertyEntity> quDuanInfoTypesPropertyEntities = quDuanInfoTypesPropertyService.connectFindByCondition(type);
        return quDuanInfoTypesPropertyEntities.stream().map(QuDuanInfoTypesPropertyEntity::getQuDuanInfoPropertyEntity).collect(Collectors.toList());
    }


    public JSONObject convert(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2, Boolean isPropertyName) {
        JSONObject jo = new JSONObject();
        jo.put("i", quDuanInfoEntityV2.getId());
        jo.put("c", quDuanInfoEntityV2.getCid());
        jo.put("q", quDuanInfoEntityV2.getQid());
        jo.put("t", quDuanInfoEntityV2.getTime());
        jo.put("e", quDuanInfoEntityV2.getType());
        jo.put("d", quDuanInfoEntityV2.getDataZhengchang());
        JSONArray jsonArray = new JSONArray();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("p", quDuanInfoPropertyEntity.getId());
            if (isPropertyName)
                jsonObject.put("n", quDuanInfoPropertyEntity.getName());
            switch (quDuanInfoPropertyEntity.getId()) {
                case 1:
                    jsonObject.put("v", quDuanInfoEntityV2.getDesignCarrier());
                    jsonObject.put("c", 1);
                    break;
                case 2:
                    jsonObject.put("v", quDuanInfoEntityV2.getDirection() == null ? null : quDuanInfoEntityV2.getDirection().equals(1) ? "正向" : quDuanInfoEntityV2.getDirection().equals(2) ? "反向" : "无效");
                    jsonObject.put("c", 1);
                    break;
                case 3:
                    jsonObject.put("v", quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("c", 1);
                    break;
                case 4:
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei());
                    jsonObject.put("v", jsonArray5);
                    jsonObject.put("c", 2);
                    break;
                case 5:
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jsonObject.put("v", jsonArray6);
                    jsonObject.put("c", 2);
                    break;
                case 6:
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jsonObject.put("v", jsonArray7);
                    jsonObject.put("c", 2);
                    break;
                case 7:
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jsonObject.put("v", jsonArray8);
                    jsonObject.put("c", 2);
                    break;
                case 8:
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jsonObject.put("v", jsonArray9);
                    jsonObject.put("c", 2);
                    break;
                case 9:
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jsonObject.put("v", jsonArray10);
                    jsonObject.put("c", 2);
                    break;
                case 10:
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jsonObject.put("v", jsonArray11);
                    jsonObject.put("c", 2);
                    break;
                case 11:
                    jsonObject.put("v", quDuanInfoEntityV2.getVSongduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 12:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaSongduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 13:
                    jsonObject.put("v", quDuanInfoEntityV2.getVShouduanCableHost());
                    jsonObject.put("c", 3);
                    break;
                case 14:
                    jsonObject.put("v", quDuanInfoEntityV2.getVShouduanCableSpare());
                    jsonObject.put("c", 3);
                    break;
                case 15:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaShouduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 16:
                    jsonObject.put("v", quDuanInfoEntityV2.getVInAll());
                    jsonObject.put("c", 4);
                    break;
                case 17:
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jsonObject.put("v", jsonArray18);
                    jsonObject.put("c", 4);
                    break;
                case 18:
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jsonObject.put("v", jsonArray19);
                    jsonObject.put("c", 4);
                    break;
                case 19:
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jsonObject.put("v", jsonArray20);
                    jsonObject.put("c", 4);
                    break;
                case 20:
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jsonObject.put("v", jsonArray21);
                    jsonObject.put("c", 4);
                case 21:
                    jsonObject.put("v", quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("c", 4);
                    break;
                case 22:
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionZhu() == null ? null : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("2") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionBing() == null ? null : quDuanInfoEntityV2.getGjRearCollectionBing().equals("2") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionBing().equals("1") ? "吸起" : "无效");
                    jsonObject.put("v", jsonArray22);
                    jsonObject.put("c", 4);
                    break;
                case 23:
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonObject.put("v", jsonArray23);
                    jsonObject.put("c", 4);
                    break;

                case 24:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaCableFbp());
                    jsonObject.put("c", 5);
                    break;
                case 25:
                    jsonObject.put("v", quDuanInfoEntityV2.getALonginFbp());
                    jsonObject.put("c", 5);
                    break;
                case 26:
                    jsonObject.put("v", quDuanInfoEntityV2.getALongoutFbp());
                    jsonObject.put("c", 5);
                    break;
                case 27:
                    jsonObject.put("v", quDuanInfoEntityV2.getAShortinFbp());
                    jsonObject.put("c", 5);
                    break;
                case 28:
                    jsonObject.put("v", quDuanInfoEntityV2.getAShortoutFbp());
                    jsonObject.put("c", 5);
                    break;
                case 29:
                    jsonObject.put("v", quDuanInfoEntityV2.getTFbp());
                    jsonObject.put("c", 5);
                    break;

                case 30:
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jsonObject.put("v", jsonArray30);
                    jsonObject.put("c", 6);
                    break;
                case 31:
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jsonObject.put("v", jsonArray31);
                    jsonObject.put("c", 6);
                    break;
                case 32:
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jsonObject.put("v", jsonArray32);
                    jsonObject.put("c", 6);
                    break;
                case 33:
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jsonObject.put("v", jsonArray33);
                    jsonObject.put("c", 6);
                    break;
                case 34:
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jsonObject.put("v", jsonArray34);
                    jsonObject.put("c", 6);
                    break;
                case 35:
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jsonObject.put("v", jsonArray35);
                    jsonObject.put("c", 6);
                    break;
                case 36:
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jsonObject.put("v", jsonArray36);
                    jsonObject.put("c", 6);
                    break;
                case 37:
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaDiao());
                    jsonObject.put("v", jsonArray37);
                    jsonObject.put("c", 6);
                    break;
                case 38:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaCableJbp());
                    jsonObject.put("c", 7);
                    break;
                case 39:
                    jsonObject.put("v", quDuanInfoEntityV2.getALonginJbp());
                    jsonObject.put("c", 7);
                    break;
                case 40:
                    jsonObject.put("v", quDuanInfoEntityV2.getALongoutJbp());
                    jsonObject.put("c", 7);
                    break;
                case 41:
                    jsonObject.put("v", quDuanInfoEntityV2.getAShortinJbp());
                    jsonObject.put("c", 7);
                    break;
                case 42:
                    jsonObject.put("v", quDuanInfoEntityV2.getAShortoutJbp());
                    jsonObject.put("c", 7);
                    break;
                case 43:
                    jsonObject.put("v", quDuanInfoEntityV2.getTJbp());
                    jsonObject.put("c", 7);
                    break;
            }
            jsonArray.add(jsonObject);
        }
        jo.put("p", jsonArray);
        jo.put("v31", quDuanInfoEntityV2.getV31());
        jo.put("v32", quDuanInfoEntityV2.getV32());
        jo.put("v33", quDuanInfoEntityV2.getV33());
        jo.put("v34", quDuanInfoEntityV2.getV34());
        jo.put("v35", quDuanInfoEntityV2.getV35());
        jo.put("v36", quDuanInfoEntityV2.getV36());
        jo.put("v37", quDuanInfoEntityV2.getV37());
        jo.put("v38", quDuanInfoEntityV2.getV38());
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
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        Set<Integer> types = new HashSet<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            Short type = quDuanInfoPropertyEntity.getType();
            if (type != null)
                types.add(type.intValue());
        }

        List<TreeNodeUtil> treeNodeUtils = this.findByTypes(types);
        for (TreeNodeUtil treeNodeUtil : treeNodeUtils) {
            List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities1 = quDuanInfoPropertyService.findByType(treeNodeUtil.getId().shortValue());
            List<TreeNodeUtil> trees = new ArrayList<>();
            for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities1) {
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
        types = types.stream().sorted(Integer::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            long id = i + 1000;
            switch (i) {
                case 0:
                    TreeNodeUtil treeNodeUtil0 = new TreeNodeUtil();
                    treeNodeUtil0.setId(id);
                    treeNodeUtil0.setLabel("区段信息");
                    treeNodeUtils.add(treeNodeUtil0);
                    break;
                case 1:
                    TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil1.setId(id);
                    treeNodeUtil1.setLabel("发送设备数据");
                    treeNodeUtils.add(treeNodeUtil1);
                    break;
                case 2:
                    TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil2.setId(id);
                    treeNodeUtil2.setLabel("送端模拟电缆数据");
                    treeNodeUtils.add(treeNodeUtil2);
                    break;
                case 3:
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil3.setId(id);
                    treeNodeUtil3.setLabel("受端模拟电缆数据");
                    treeNodeUtils.add(treeNodeUtil3);
                    break;
                case 4:
                    TreeNodeUtil treeNodeUtil4 = new TreeNodeUtil();
                    treeNodeUtil4.setId(id);
                    treeNodeUtil4.setLabel("接收设备数据");
                    treeNodeUtils.add(treeNodeUtil4);
                    break;
                case 5:
                    TreeNodeUtil treeNodeUtil5 = new TreeNodeUtil();
                    treeNodeUtil5.setId(id);
                    treeNodeUtil5.setLabel("FBP采集数据");
                    treeNodeUtils.add(treeNodeUtil5);
                    break;
                case 6:
                    TreeNodeUtil treeNodeUtil6 = new TreeNodeUtil();
                    treeNodeUtil6.setId(id);
                    treeNodeUtil6.setLabel("FBA采集数据");
                    treeNodeUtils.add(treeNodeUtil6);
                    break;
                case 7:
                    TreeNodeUtil treeNodeUtil7 = new TreeNodeUtil();
                    treeNodeUtil7.setId(id);
                    treeNodeUtil7.setLabel("JBA采集数据");
                    treeNodeUtils.add(treeNodeUtil7);
                    break;
                case 8:
                    TreeNodeUtil treeNodeUtil8 = new TreeNodeUtil();
                    treeNodeUtil8.setId(id);
                    treeNodeUtil8.setLabel("JBP采集数据");
                    treeNodeUtils.add(treeNodeUtil8);
                    break;
            }
        }
        return treeNodeUtils;
    }


    /**
     * 实时 报表
     *
     * @param properties 属性集合
     * @param czId       车站id
     * @return
     */
    @Override
    public JSONObject realTimeReport(Integer czId, Integer[] properties, String qName, Boolean isDianMaHua) {
        JSONObject jo = new JSONObject(true);
        if (properties == null || properties.length == 0) {
            jo.put("title", new JSONArray());
            jo.put("data", new JSONArray());
            return jo;
        }
        //表头数组
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.findByIds(properties);
        List<TreeNodeUtil> first = new ArrayList<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setLabel(quDuanInfoPropertyEntity.getName());

            List<TreeNodeUtil> second = new ArrayList<>();
            treeNodeUtil.setChildren(second);
            TreeNodeUtil treeNodeUtil1 = null;
            TreeNodeUtil treeNodeUtil2 = null;
            switch (quDuanInfoPropertyEntity.getId()) {
                case 1:
                case 2:
                case 3:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 21:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel(quDuanInfoPropertyEntity.getName());
                    treeNodeUtil1.setValue("a" + quDuanInfoPropertyEntity.getId().toString());
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil1.setValue("a" + quDuanInfoPropertyEntity.getId().toString());
                    treeNodeUtil2.setLabel("备机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setValue("b" + quDuanInfoPropertyEntity.getId().toString());
                    break;
                case 17:
                case 18:
                case 19:
                case 20:
                case 22:
                case 23:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil1.setValue("a" + quDuanInfoPropertyEntity.getId().toString());
                    treeNodeUtil2.setLabel("并机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setValue("b" + quDuanInfoPropertyEntity.getId().toString());
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil1.setValue("a" + quDuanInfoPropertyEntity.getId().toString());
                    treeNodeUtil2.setLabel("调信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setValue("b" + quDuanInfoPropertyEntity.getId().toString());
                    break;
            }
            if (treeNodeUtil1 != null)
                second.add(treeNodeUtil1);
            if (treeNodeUtil2 != null)
                second.add(treeNodeUtil2);
            first.add(treeNodeUtil);
        }
        jo.put("title", first);
        //表头对应数据数组
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, qName, isDianMaHua);
        JSONArray dataJa = new JSONArray();
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
            QuDuanInfoEntityV2 quDuanInfoEntityV2 = this.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
            if (quDuanInfoEntityV2 == null) {
                continue;
            }
            quDuanInfoEntityV2.setQuDuanBaseEntity(quDuanBaseEntity);
            JSONObject jsonObject = this.findDate(quDuanInfoPropertyEntities, quDuanInfoEntityV2);
            dataJa.add(jsonObject);
        }
        jo.put("data", dataJa);
        return jo;
    }

    public JSONObject findDate(List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities, QuDuanInfoEntityV2 quDuanInfoEntityV2) {
        JSONObject jo = new JSONObject();
        jo.put("quDuanYunYingName", quDuanInfoEntityV2.getQuDuanBaseEntity().getQuduanyunyingName());
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            String idStr = String.valueOf(quDuanInfoPropertyEntity.getId());
            List<MenXianEntity> menXianEntities = menXianService.findByCzIdAndQuduanIdAndPropertyId(quDuanInfoEntityV2.getCid(), quDuanInfoEntityV2.getQid(), quDuanInfoPropertyEntity.getId());
            Integer status = quDuanInfoEntityV2.getDataZhengchang();
            MenXianEntity m1 = null;
            MenXianEntity m2 = null;
            if (menXianEntities.size() == 1)
                m1 = menXianEntities.get(0);
            if (menXianEntities.size() == 2) {
                m1 = menXianEntities.get(0);
                m2 = menXianEntities.get(1);
            }
            switch (idStr) {
                case "1":
                    JSONObject jo11 = new JSONObject();
                    jo11.put("value", quDuanInfoEntityV2.getDesignCarrier());
                    jo.put("a" + idStr, jo11);
                    break;
                case "2":
                    Integer direction = quDuanInfoEntityV2.getDirection();
                    JSONObject jo12 = new JSONObject();
                    jo12.put("value", direction == null ? null : direction == 1 ? "正向" : direction == 2 ? "反向" : "无效");
                    jo.put("a" + idStr, jo12);
                    break;
                case "3":
                    String djcollection = quDuanInfoEntityV2.getDjcollection();
                    JSONObject jo13 = new JSONObject();
                    jo13.put("value", djcollection == null ? null : djcollection.equals("2") ? "落下" : djcollection.equals("1") ? "吸起" : "无效");
                    jo.put("a" + idStr, jo13);
                    break;
                case "4":
                    JSONObject jo14 = new JSONObject();
                    String vOutZhu = quDuanInfoEntityV2.getVOutZhu();
                    jo14.put("value", vOutZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo14.put("isOver", Double.parseDouble(vOutZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(vOutZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo14.put("text", ja);
                        }
                    }

                    JSONObject jo24 = new JSONObject();
                    String vOutBei = quDuanInfoEntityV2.getVOutBei();
                    jo24.put("value", vOutBei);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo24.put("isOver", Double.parseDouble(vOutBei) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(vOutBei) > Double.parseDouble(m2.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m2.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m2.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo24.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo14);
                    jo.put("b" + idStr, jo24);
                    break;
                case "5":
                    JSONObject jo15 = new JSONObject();
                    String maOutZhu = quDuanInfoEntityV2.getMaOutZhu();
                    jo15.put("value", maOutZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo15.put("isOver", Double.parseDouble(maOutZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(maOutZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo15.put("text", ja);
                        }
                    }
                    JSONObject jo25 = new JSONObject();
                    String maOutBei = quDuanInfoEntityV2.getMaOutBei();
                    jo25.put("value", maOutBei);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo25.put("isOver", Double.parseDouble(maOutBei) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(maOutBei) > Double.parseDouble(m2.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m2.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m2.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo25.put("text", ja);
                        }
                    }

                    jo.put("a" + idStr, jo15);
                    jo.put("b" + idStr, jo25);
                    break;
                case "6":
                    JSONObject jo16 = new JSONObject();
                    String hzUpZhu = quDuanInfoEntityV2.getHzUpZhu();
                    jo16.put("value", hzUpZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo16.put("isOver", Double.parseDouble(hzUpZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(hzUpZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo16.put("text", ja);
                        }
                    }
                    JSONObject jo26 = new JSONObject();
                    String hzUpBei = quDuanInfoEntityV2.getHzUpBei();
                    jo26.put("value", hzUpBei);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo26.put("isOver", Double.parseDouble(hzUpBei) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(hzUpBei) > Double.parseDouble(m2.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m2.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m2.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo26.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo16);
                    jo.put("b" + idStr, jo26);
                    break;
                case "7":
                    JSONObject jo17 = new JSONObject();
                    String hzDownZhu = quDuanInfoEntityV2.getHzDownZhu();
                    jo17.put("value", hzDownZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo17.put("isOver", Double.parseDouble(hzDownZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(hzDownZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo17.put("text", ja);
                        }
                    }
                    JSONObject jo27 = new JSONObject();
                    String hzDownBei = quDuanInfoEntityV2.getHzDownBei();
                    jo27.put("value", hzDownBei);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo27.put("isOver", Double.parseDouble(hzDownBei) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(hzDownBei) > Double.parseDouble(m2.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m2.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m2.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo27.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo17);
                    jo.put("b" + idStr, jo27);
                    break;
                case "8":
                    JSONObject jo18 = new JSONObject();
                    String hzLowZhu = quDuanInfoEntityV2.getHzLowZhu();
                    jo18.put("value", hzLowZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo18.put("isOver", Double.parseDouble(hzLowZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(hzLowZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo18.put("text", ja);
                        }
                    }
                    JSONObject jo28 = new JSONObject();
                    String hzLowBei = quDuanInfoEntityV2.getHzLowBei();
                    jo28.put("value", hzLowBei);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo28.put("isOver", Double.parseDouble(hzLowBei) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(hzLowBei) > Double.parseDouble(m2.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m2.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m2.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo28.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo18);
                    jo.put("b" + idStr, jo28);
                    break;
                case "9":
                    JSONObject jo19 = new JSONObject();
                    Integer fbjDriveZhu1 = quDuanInfoEntityV2.getFbjDriveZhu();
                    String str1 = fbjDriveZhu1 == null ? null : fbjDriveZhu1 == 1 ? "正常" : fbjDriveZhu1 == 2 ? "无" : "无效";
                    jo19.put("value", str1);
                    jo19.put("isOver", "无".equals(str1) || "无效".equals(str1));
                    JSONObject jo29 = new JSONObject();
                    Integer fbjDriveBei2 = quDuanInfoEntityV2.getFbjDriveBei();
                    String str2 = fbjDriveBei2 == null ? null : fbjDriveBei2 == 1 ? "正常" : fbjDriveBei2 == 2 ? "无" : "无效";
                    jo29.put("value", str2);
                    jo29.put("isOver", "无".equals(str2) || "无效".equals(str2));

                    jo.put("a" + idStr, jo19);
                    jo.put("b" + idStr, jo29);
                    break;
                case "10":
                    JSONObject jo110 = new JSONObject();
                    String fbjCollectionZhu = quDuanInfoEntityV2.getFbjCollectionZhu();
                    jo110.put("value", fbjCollectionZhu == null ? null : fbjCollectionZhu.equals("2") ? "落下" : fbjCollectionZhu.equals("1") ? "吸起" : "无效");
                    JSONObject jo210 = new JSONObject();
                    String fbjCollectionBei = quDuanInfoEntityV2.getFbjCollectionBei();
                    jo210.put("value", fbjCollectionBei == null ? null : fbjCollectionBei.equals("2") ? "落下" : fbjCollectionBei.equals("1") ? "吸起" : "无效");
                    jo.put("a" + idStr, jo110);
                    jo.put("b" + idStr, jo210);
                    break;
                case "11":
                    JSONObject jo111 = new JSONObject();
                    String vSongduanCable = quDuanInfoEntityV2.getVSongduanCable();
                    jo111.put("value", vSongduanCable);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo111.put("isOver", Double.parseDouble(vSongduanCable) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(vSongduanCable) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo111.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo111);
                    break;
                case "12":
                    JSONObject jo112 = new JSONObject();
                    String maSongduanCable = quDuanInfoEntityV2.getMaSongduanCable();
                    jo112.put("value", maSongduanCable);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo112.put("isOver", Double.parseDouble(maSongduanCable) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(maSongduanCable) > Double.parseDouble(m1.getAlarmSuperior()));
                            JSONArray ja = new JSONArray();
                            JSONObject x = new JSONObject();
                            x.put("label", "报警下限");
                            x.put("value", m1.getAlarmLower());
                            JSONObject s = new JSONObject();
                            s.put("label", "报警上限");
                            s.put("value", m1.getAlarmSuperior());
                            ja.add(x);
                            ja.add(s);
                            jo112.put("text", ja);
                        }
                    }
                    jo.put("a" + idStr, jo112);
                    break;
                case "13":
                    JSONObject jo113 = new JSONObject();
                    String vShouduanCableHost = quDuanInfoEntityV2.getVShouduanCableHost();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo113.put("value", vShouduanCableHost + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo113.put("isOver", Double.parseDouble(vShouduanCableHost) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(vShouduanCableHost) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo113.put("value", vShouduanCableHost + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerZ()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo113.put("isOver", Double.parseDouble(vShouduanCableHost) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(vShouduanCableHost) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m1.getAlarmLowerZ());
                            }
                        } else {
                            jo113.put("value", vShouduanCableHost);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo113.put("text", ja);

                    }
                    jo.put("a" + idStr, jo113);
                    break;
                case "14":
                    JSONObject jo114 = new JSONObject();
                    String vShouduanCableSpare = quDuanInfoEntityV2.getVShouduanCableSpare();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo114.put("value", vShouduanCableSpare + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo114.put("isOver", Double.parseDouble(vShouduanCableSpare) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(vShouduanCableSpare) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo114.put("value", vShouduanCableSpare + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerZ()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo114.put("isOver", Double.parseDouble(vShouduanCableSpare) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(vShouduanCableSpare) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m1.getAlarmLowerZ());
                            }
                        } else {
                            jo114.put("value", vShouduanCableSpare);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo114.put("text", ja);
                    }
                    jo.put("a" + idStr, jo114);
                    break;
                case "15":
                    JSONObject jo115 = new JSONObject();
                    String maShouduanCable = quDuanInfoEntityV2.getMaShouduanCable();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo115.put("value", maShouduanCable + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo115.put("isOver", Double.parseDouble(maShouduanCable) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(maShouduanCable) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo115.put("value", maShouduanCable + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerZ()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo115.put("isOver", Double.parseDouble(maShouduanCable) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(maShouduanCable) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m1.getAlarmLowerZ());
                            }
                        } else {
                            jo115.put("value", maShouduanCable);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo115.put("text", ja);
                    }
                    jo.put("a" + idStr, jo115);
                    break;
                case "16":
                    JSONObject jo116 = new JSONObject();
                    String vInAll = quDuanInfoEntityV2.getVInAll();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo116.put("value", vInAll + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo116.put("isOver", Double.parseDouble(vInAll) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(vInAll) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo116.put("value", vInAll + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo116.put("isOver", Double.parseDouble(vInAll) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(vInAll) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m1.getAlarmLowerZ());
                            }
                        } else {
                            jo116.put("value", vInAll);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo116.put("text", ja);
                    }
                    jo.put("a" + idStr, jo116);
                    break;
                case "17":
                    JSONObject jo117 = new JSONObject();
                    String mvInZhu = quDuanInfoEntityV2.getMvInZhu();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        if (status == 1) {
                            jo117.put("value", mvInZhu + "(空闲)");
                            if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperiorK())) {
                                jo117.put("isOver", Double.parseDouble(mvInZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(mvInZhu) > Double.parseDouble(m1.getAlarmSuperiorK()));
                                s.put("label", "报警上限(空闲)");
                                s.put("value", m1.getAlarmSuperiorK());
                            }
                        } else if (status == 2) {
                            jo117.put("value", mvInZhu + "(占用)");
                            if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperiorZ())) {
                                jo117.put("isOver", Double.parseDouble(mvInZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(mvInZhu) > Double.parseDouble(m1.getAlarmSuperiorZ()));
                                s.put("label", "报警上限(占用)");
                                s.put("value", m1.getAlarmSuperiorZ());
                            }
                        } else {
                            jo117.put("value", mvInZhu);
                        }
                        ja.add(x);
                        ja.add(s);
                        jo117.put("text", ja);
                    }

                    JSONObject jo217 = new JSONObject();
                    String mvInBing = quDuanInfoEntityV2.getMvInBing();
                    if (m2 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        if (status == 1) {
                            jo217.put("value", mvInZhu + "(空闲)");
                            if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperiorK())) {
                                jo217.put("isOver", Double.parseDouble(mvInBing) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(mvInBing) > Double.parseDouble(m2.getAlarmSuperiorK()));
                                s.put("label", "报警上限(空闲)");
                                s.put("value", m2.getAlarmSuperiorK());
                            }
                        } else if (status == 2) {
                            jo217.put("value", mvInZhu + "(占用)");
                            if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperiorZ())) {
                                jo217.put("isOver", Double.parseDouble(mvInBing) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(mvInBing) > Double.parseDouble(m2.getAlarmSuperiorZ()));
                                s.put("label", "报警上限(占用)");
                                s.put("value", m2.getAlarmSuperiorZ());
                            }
                        } else {
                            jo217.put("value", mvInBing);
                        }
                        ja.add(x);
                        ja.add(s);
                        jo217.put("text", ja);
                    }

                    jo.put("a" + idStr, jo117);
                    jo.put("b" + idStr, jo217);
                    break;
                case "18":
                    JSONObject jo118 = new JSONObject();
                    String mvInDiaoZhu = quDuanInfoEntityV2.getMvInDiaoZhu();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        JSONObject s = new JSONObject();
                        if (status == 1) {
                            jo118.put("value", mvInDiaoZhu + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperiorK())) {
                                jo118.put("isOver", Double.parseDouble(mvInDiaoZhu) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(mvInDiaoZhu) > Double.parseDouble(m1.getAlarmSuperiorK()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                                s.put("label", "报警上限(空闲)");
                                s.put("value", m1.getAlarmSuperiorK());
                            }
                        } else {
                            jo118.put("value", mvInDiaoZhu);
                        }
                        ja.add(x);
                        ja.add(s);
                        jo118.put("text", ja);
                    }

                    JSONObject jo218 = new JSONObject();
                    String mvInDiaoBing = quDuanInfoEntityV2.getMvInDiaoBing();
                    if (m2 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        JSONObject s = new JSONObject();
                        if (status == 1) {
                            jo218.put("value", mvInDiaoBing + "(空闲)");
                            if (!"——".equals(m2.getAlarmLowerK()) && !"——".equals(m2.getAlarmSuperiorK())) {
                                jo218.put("isOver", Double.parseDouble(mvInDiaoZhu) < Double.parseDouble(m2.getAlarmLowerK()) || Double.parseDouble(mvInDiaoZhu) > Double.parseDouble(m2.getAlarmSuperiorK()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m2.getAlarmLowerK());
                                s.put("label", "报警上限(空闲)");
                                s.put("value", m2.getAlarmSuperiorK());
                            }
                        } else {
                            jo118.put("value", mvInDiaoBing);
                        }
                        ja.add(x);
                        ja.add(s);
                        jo218.put("text", ja);
                    }
                    jo.put("a" + idStr, jo118);
                    jo.put("b" + idStr, jo218);
                    break;
                case "19":
                    JSONObject jo119 = new JSONObject();
                    String hzInLowZhu = quDuanInfoEntityV2.getHzInLowZhu();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo119.put("value", hzInLowZhu + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo119.put("isOver", Double.parseDouble(hzInLowZhu) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(hzInLowZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo119.put("value", hzInLowZhu + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerZ()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo119.put("isOver", Double.parseDouble(hzInLowZhu) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(hzInLowZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m1.getAlarmLowerZ());
                            }
                        } else {
                            jo119.put("value", hzInLowZhu);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo119.put("text", ja);
                    }

                    JSONObject jo219 = new JSONObject();
                    String hzInLowBing = quDuanInfoEntityV2.getHzInLowBing();
                    if (m2 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo219.put("value", hzInLowBing + "(空闲)");
                            if (!"——".equals(m2.getAlarmLowerK()) && !"——".equals(m2.getAlarmSuperior())) {
                                jo219.put("isOver", Double.parseDouble(hzInLowBing) < Double.parseDouble(m2.getAlarmLowerK()) || Double.parseDouble(hzInLowBing) > Double.parseDouble(m2.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m2.getAlarmLowerK());
                            }
                        } else if (status == 2) {
                            jo219.put("value", hzInLowBing + "(占用)");
                            if (!"——".equals(m2.getAlarmLowerZ()) && !"——".equals(m2.getAlarmSuperior())) {
                                jo219.put("isOver", Double.parseDouble(hzInLowBing) < Double.parseDouble(m2.getAlarmLowerZ()) || Double.parseDouble(hzInLowBing) > Double.parseDouble(m2.getAlarmSuperior()));
                                x.put("label", "报警下限(占用)");
                                x.put("value", m2.getAlarmLowerZ());
                            }
                        } else {
                            jo219.put("value", hzInLowBing);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo219.put("text", ja);
                    }
                    jo.put("a" + idStr, jo119);
                    jo.put("b" + idStr, jo219);
                    break;
                case "20":
                    Integer gjDriveZhu = quDuanInfoEntityV2.getGjDriveZhu();
                    JSONObject jo120 = new JSONObject();
                    String str120 = gjDriveZhu == null ? null : gjDriveZhu == 1 ? "正常" : gjDriveZhu == 2 ? "无" : "无效";
                    jo120.put("value", str120);
                    jo120.put("isOver", "无".equals(str120) || "无效".equals(str120));

                    Integer gjDriveBing = quDuanInfoEntityV2.getGjDriveBing();
                    JSONObject jo220 = new JSONObject();
                    String str220 = gjDriveBing == null ? null : gjDriveBing == 1 ? "正常" : gjDriveBing == 2 ? "无" : "无效";
                    jo220.put("value", str120);
                    jo220.put("isOver", "无".equals(str220) || "无效".equals(str220));

                    jo.put("a" + idStr, jo120);
                    jo.put("b" + idStr, jo220);
                    break;
                case "21":
                    JSONObject jo121 = new JSONObject();
                    String gjcollection = quDuanInfoEntityV2.getGjcollection();
                    jo121.put("value", gjcollection == null ? null : gjcollection.equals("2") ? "落下" : gjcollection.equals("1") ? "吸起" : "无效");

                    jo.put("a" + idStr, jo121);
                    break;
                case "22":
                    JSONObject jo122 = new JSONObject();
                    String gjRearCollectionZhu = quDuanInfoEntityV2.getGjRearCollectionBing();
                    jo122.put("value", gjRearCollectionZhu == null ? null : gjRearCollectionZhu.equals("2") ? "落下" : gjRearCollectionZhu.equals("1") ? "吸起" : "无效");

                    JSONObject jo222 = new JSONObject();
                    String gjRearCollectionBing = quDuanInfoEntityV2.getGjRearCollectionZhu();
                    jo222.put("value", gjRearCollectionBing == null ? null : gjRearCollectionBing.equals("2") ? "落下" : gjRearCollectionBing.equals("1") ? "吸起" : "无效");

                    jo.put("a" + idStr, jo122);
                    jo.put("b" + idStr, jo222);
                    break;
                case "23":
                    Integer baojingZhu = quDuanInfoEntityV2.getBaojingZhu();
                    JSONObject jo123 = new JSONObject();
                    String str123 = baojingZhu == null ? null : baojingZhu == 1 ? "正常" : baojingZhu == 2 ? "报警" : "无效";
                    jo123.put("value", str123);
                    jo123.put("isOver", "报警".equals(str123) || "无效".equals(str123));

                    Integer baojingBing = quDuanInfoEntityV2.getBaojingBing();
                    JSONObject jo223 = new JSONObject();
                    String str223 = baojingBing == null ? null : baojingBing == 1 ? "正常" : baojingBing == 2 ? "报警" : "无效";
                    jo223.put("value", str223);
                    jo223.put("isOver", "报警".equals(str223) || "无效".equals(str223));

                    jo.put("a" + idStr, jo123);
                    jo.put("b" + idStr, jo223);
                    break;
                case "24":
                    JSONObject jo124 = new JSONObject();
                    String maCableFbp = quDuanInfoEntityV2.getMaCableFbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo124.put("value", maCableFbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo124.put("isOver", Double.parseDouble(maCableFbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(maCableFbp) > Double.parseDouble(m1.getAlarmSuperior()));
                                x.put("label", "报警下限(空闲)");
                                x.put("value", m1.getAlarmLowerK());
                            }
                        } else {
                            jo124.put("value", maCableFbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo124.put("text", ja);
                    }
                    jo.put("a" + idStr, jo124);
                    break;
                case "25":
                    JSONObject jo125 = new JSONObject();
                    String aLonginFbp = quDuanInfoEntityV2.getALonginFbp();
                    jo125.put("value", aLonginFbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo125.put("isOver", Double.parseDouble(aLonginFbp) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLonginFbp) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo125.put("text", ja);

                    }
                    jo.put("a" + idStr, jo125);
                    break;
                case "26":
                    JSONObject jo126 = new JSONObject();
                    String aLongoutFbp = quDuanInfoEntityV2.getALongoutFbp();
                    jo126.put("value", aLongoutFbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo126.put("isOver", Double.parseDouble(aLongoutFbp) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLongoutFbp) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo126.put("text", ja);
                    }
                    jo.put("a" + idStr, jo126);
                    break;
                case "27":
                    JSONObject jo127 = new JSONObject();
                    String aShortinFbp = quDuanInfoEntityV2.getAShortinFbp();
                    jo127.put("value", aShortinFbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo127.put("isOver", Double.parseDouble(aShortinFbp) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortinFbp) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo127.put("text", ja);
                    }
                    jo.put("a" + idStr, jo127);
                    break;
                case "28":
                    JSONObject jo128 = new JSONObject();
                    String aShortoutFbp = quDuanInfoEntityV2.getAShortoutFbp();
                    jo128.put("value", aShortoutFbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo128.put("isOver", Double.parseDouble(aShortoutFbp) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortoutFbp) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo128.put("text", ja);
                    }
                    jo.put("a" + idStr, jo128);
                    break;
                case "29":
                    JSONObject jo129 = new JSONObject();
                    Integer tFbp = quDuanInfoEntityV2.getTFbp();
                    jo129.put("value", tFbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo129.put("isOver", Double.parseDouble(String.valueOf(tFbp)) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(String.valueOf(tFbp)) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo129.put("text", ja);
                    }
                    jo.put("a" + idStr, jo129);
                    break;
                case "30":
                    JSONObject jo130 = new JSONObject();
                    String aLonginFbaZhu = quDuanInfoEntityV2.getALonginFbaZhu();
                    jo130.put("value", aLonginFbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo130.put("isOver", Double.parseDouble(aLonginFbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLonginFbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo130.put("text", ja);
                    }

                    JSONObject jo230 = new JSONObject();
                    String aLonginFbaDiao = quDuanInfoEntityV2.getALonginFbaDiao();
                    jo230.put("value", aLonginFbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo230.put("isOver", Double.parseDouble(aLonginFbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aLonginFbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo230.put("text", ja);
                    }
                    jo.put("a" + idStr, jo130);
                    jo.put("b" + idStr, jo230);
                    break;
                case "31":
                    JSONObject jo131 = new JSONObject();
                    String aLongoutFbaZhu = quDuanInfoEntityV2.getALongoutFbaZhu();
                    jo131.put("value", aLongoutFbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo131.put("isOver", Double.parseDouble(aLongoutFbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLongoutFbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo131.put("text", ja);
                    }

                    JSONObject jo231 = new JSONObject();
                    String aLongoutFbaDiao = quDuanInfoEntityV2.getALongoutFbaDiao();
                    jo231.put("value", aLongoutFbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo231.put("isOver", Double.parseDouble(aLongoutFbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aLongoutFbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo231.put("text", ja);
                    }
                    jo.put("a" + idStr, jo131);
                    jo.put("b" + idStr, jo231);
                    break;
                case "32":
                    JSONObject jo132 = new JSONObject();
                    String aShortinFbaZhu = quDuanInfoEntityV2.getAShortinFbaZhu();
                    jo132.put("value", aShortinFbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo132.put("isOver", Double.parseDouble(aShortinFbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortinFbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo132.put("text", ja);
                    }
                    JSONObject jo232 = new JSONObject();
                    String aShortinFbaDiao = quDuanInfoEntityV2.getAShortinFbaDiao();
                    jo232.put("value", aShortinFbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo232.put("isOver", Double.parseDouble(aShortinFbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aShortinFbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo232.put("text", ja);
                    }
                    jo.put("a" + idStr, jo132);
                    jo.put("b" + idStr, jo232);
                    break;
                case "33":
                    JSONObject jo133 = new JSONObject();
                    String aShortoutFbaZhu = quDuanInfoEntityV2.getAShortoutFbaZhu();
                    jo133.put("value", aShortoutFbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo133.put("isOver", Double.parseDouble(aShortoutFbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortoutFbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo133.put("text", ja);
                    }
                    JSONObject jo233 = new JSONObject();
                    String aShortoutFbaDiao = quDuanInfoEntityV2.getAShortoutFbaDiao();
                    jo233.put("value", aShortoutFbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo233.put("isOver", Double.parseDouble(aShortoutFbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aShortoutFbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo233.put("text", ja);
                    }
                    jo.put("a" + idStr, jo133);
                    jo.put("b" + idStr, jo233);
                    break;
                case "34":
                    JSONObject jo134 = new JSONObject();
                    String aLonginJbaZhu = quDuanInfoEntityV2.getALonginJbaZhu();
                    jo134.put("value", aLonginJbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo134.put("isOver", Double.parseDouble(aLonginJbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLonginJbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo134.put("text", ja);
                    }
                    JSONObject jo234 = new JSONObject();
                    String aLonginJbaDiao = quDuanInfoEntityV2.getALonginJbaDiao();
                    jo234.put("value", aLonginJbaZhu);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo234.put("isOver", Double.parseDouble(aLonginJbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aLonginJbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo234.put("text", ja);
                    }
                    jo.put("a" + idStr, jo134);
                    jo.put("b" + idStr, jo234);
                    break;
                case "35":
                    JSONObject jo135 = new JSONObject();
                    String aLongoutJbaZhu = quDuanInfoEntityV2.getALongoutJbaZhu();
                    jo135.put("value", aLongoutJbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo135.put("isOver", Double.parseDouble(aLongoutJbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aLongoutJbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo135.put("text", ja);
                    }
                    JSONObject jo235 = new JSONObject();
                    String aLongoutJbaDiao = quDuanInfoEntityV2.getALongoutJbaDiao();
                    jo235.put("value", aLongoutJbaZhu);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo235.put("isOver", Double.parseDouble(aLongoutJbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aLongoutJbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo235.put("text", ja);
                    }
                    jo.put("a" + idStr, jo135);
                    jo.put("b" + idStr, jo235);
                    break;
                case "36":
                    JSONObject jo136 = new JSONObject();
                    String aShortinJbaZhu = quDuanInfoEntityV2.getAShortinJbaZhu();
                    jo136.put("value", aShortinJbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo136.put("isOver", Double.parseDouble(aShortinJbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortinJbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo136.put("text", ja);
                    }
                    JSONObject jo236 = new JSONObject();
                    String aShortinJbaDiao = quDuanInfoEntityV2.getAShortinJbaDiao();
                    jo236.put("value", aShortinJbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo236.put("isOver", Double.parseDouble(aShortinJbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aShortinJbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo236.put("text", ja);
                    }
                    jo.put("a" + idStr, jo136);
                    jo.put("b" + idStr, jo236);
                    break;
                case "37":
                    JSONObject jo137 = new JSONObject();
                    String aShortoutJbaZhu = quDuanInfoEntityV2.getAShortoutJbaZhu();
                    jo137.put("value", aShortoutJbaZhu);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo137.put("isOver", Double.parseDouble(aShortoutJbaZhu) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(aShortoutJbaZhu) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo137.put("text", ja);
                    }
                    JSONObject jo237 = new JSONObject();
                    String aShortoutJbaDiao = quDuanInfoEntityV2.getAShortoutJbaDiao();
                    jo237.put("value", aShortoutJbaDiao);
                    if (m2 != null) {
                        if (!"——".equals(m2.getAlarmLower()) && !"——".equals(m2.getAlarmSuperior())) {
                            jo237.put("isOver", Double.parseDouble(aShortoutJbaDiao) < Double.parseDouble(m2.getAlarmLower()) || Double.parseDouble(aShortoutJbaDiao) > Double.parseDouble(m2.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m2.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m2.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo237.put("text", ja);
                    }
                    jo.put("a" + idStr, jo137);
                    jo.put("b" + idStr, jo237);
                    break;
                case "38":
                    JSONObject jo138 = new JSONObject();
                    String maCableJbp = quDuanInfoEntityV2.getMaCableJbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo138.put("value", maCableJbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo138.put("isOver", Double.parseDouble(maCableJbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(maCableJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(空闲)");
                            x.put("value", m1.getAlarmLowerK());
                        } else if (status == 2) {
                            jo138.put("value", maCableJbp + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerZ()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo138.put("isOver", Double.parseDouble(maCableJbp) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(maCableJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(占用)");
                            x.put("value", m1.getAlarmLowerZ());
                        } else {
                            jo138.put("value", maCableJbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo138.put("text", ja);
                    }
                    jo.put("a" + idStr, jo138);
                    break;
                case "39":
                    JSONObject jo139 = new JSONObject();
                    String aLonginJbp = quDuanInfoEntityV2.getALonginJbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo139.put("value", aLonginJbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo139.put("isOver", Double.parseDouble(aLonginJbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(aLonginJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(空闲)");
                            x.put("value", m1.getAlarmLowerK());
                        } else if (status == 2) {
                            jo139.put("value", aLonginJbp + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo139.put("isOver", Double.parseDouble(aLonginJbp) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(aLonginJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(占用)");
                            x.put("value", m1.getAlarmLowerZ());
                        } else {
                            jo139.put("value", aLonginJbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo139.put("text", ja);
                    }
                    jo.put("a" + idStr, jo139);
                    break;
                case "40":
                    JSONObject jo140 = new JSONObject();
                    String aLongoutJbp = quDuanInfoEntityV2.getALongoutJbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo140.put("value", aLongoutJbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo140.put("isOver", Double.parseDouble(aLongoutJbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(aLongoutJbp) > Double.parseDouble(m1.getAlarmLowerK()));
                            }
                            x.put("label", "报警下限(空闲)");
                            x.put("value", m1.getAlarmLowerK());
                        } else if (status == 2) {
                            jo140.put("value", aLongoutJbp + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo140.put("isOver", Double.parseDouble(aLongoutJbp) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(aLongoutJbp) > Double.parseDouble(m1.getAlarmLowerZ()));
                            }
                            x.put("label", "报警下限(占用)");
                            x.put("value", m1.getAlarmLowerZ());
                        } else {
                            jo140.put("value", aLongoutJbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo140.put("text", ja);
                    }
                    jo.put("a" + idStr, jo140);
                    break;
                case "41":
                    JSONObject jo141 = new JSONObject();
                    String aShortinJbp = quDuanInfoEntityV2.getAShortinJbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo141.put("value", aShortinJbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo141.put("isOver", Double.parseDouble(aShortinJbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(aShortinJbp) > Double.parseDouble(m1.getAlarmLowerK()));
                            }
                            x.put("label", "报警下限(空闲)");
                            x.put("value", m1.getAlarmLowerK());
                        } else if (status == 2) {
                            jo141.put("value", aShortinJbp + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo141.put("isOver", Double.parseDouble(aShortinJbp) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(aShortinJbp) > Double.parseDouble(m1.getAlarmLowerZ()));
                            }
                            x.put("label", "报警下限(占用)");
                            x.put("value", m1.getAlarmLowerZ());
                        } else {
                            jo141.put("value", aShortinJbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo141.put("text", ja);
                    }
                    jo.put("a" + idStr, jo141);
                    break;
                case "42":
                    JSONObject jo142 = new JSONObject();
                    String aShortoutJbp = quDuanInfoEntityV2.getAShortoutJbp();
                    if (m1 != null) {
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        if (status == 1) {
                            jo142.put("value", aShortoutJbp + "(空闲)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo142.put("isOver", Double.parseDouble(aShortoutJbp) < Double.parseDouble(m1.getAlarmLowerK()) || Double.parseDouble(aShortoutJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(空闲)");
                            x.put("value", m1.getAlarmLowerK());
                        } else if (status == 2) {
                            jo142.put("value", aShortoutJbp + "(占用)");
                            if (!"——".equals(m1.getAlarmLowerK()) && !"——".equals(m1.getAlarmSuperior())) {
                                jo142.put("isOver", Double.parseDouble(aShortoutJbp) < Double.parseDouble(m1.getAlarmLowerZ()) || Double.parseDouble(aShortoutJbp) > Double.parseDouble(m1.getAlarmSuperior()));
                            }
                            x.put("label", "报警下限(占用)");
                            x.put("value", m1.getAlarmLowerZ());
                        } else {
                            jo142.put("value", aShortoutJbp);
                        }
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo142.put("text", ja);
                    }
                    jo.put("a" + idStr, jo142);
                    break;
                case "43":
                    JSONObject jo143 = new JSONObject();
                    Integer tJbp = quDuanInfoEntityV2.getTJbp();
                    jo143.put("value", tJbp);
                    if (m1 != null) {
                        if (!"——".equals(m1.getAlarmLower()) && !"——".equals(m1.getAlarmSuperior())) {
                            jo143.put("isOver", Double.parseDouble(String.valueOf(tJbp)) < Double.parseDouble(m1.getAlarmLower()) || Double.parseDouble(String.valueOf(tJbp)) > Double.parseDouble(m1.getAlarmSuperior()));
                        }
                        JSONArray ja = new JSONArray();
                        JSONObject x = new JSONObject();
                        x.put("label", "报警下限");
                        x.put("value", m1.getAlarmLower());
                        JSONObject s = new JSONObject();
                        s.put("label", "报警上限");
                        s.put("value", m1.getAlarmSuperior());
                        ja.add(x);
                        ja.add(s);
                        jo143.put("text", ja);
                    }
                    jo.put("a" + idStr, jo143);
                    break;
            }
        }
        return jo;
    }


    public boolean idIsInArr(int i) {
        int[] ids = new int[]{9, 10, 20, 21, 22, 23};
        for (int id : ids) {
            if (id == i)
                return true;
        }
        return false;
    }

    /**
     * 日报表 属性分类
     *
     * @param czId 车站id
     * @return
     */
    @Override
    public List<TreeNodeUtil> findPropertiesTrees(Integer czId) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        Set<Integer> types = new HashSet<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            Short type = quDuanInfoPropertyEntity.getType();
            if (type != null && type != 1000)
                types.add(type.intValue());
        }

        List<TreeNodeUtil> treeNodeUtils = this.findByTypess(types);
        for (TreeNodeUtil treeNodeUtil : treeNodeUtils) {
            List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities1 = quDuanInfoPropertyService.findByType(treeNodeUtil.getId().shortValue());
            List<TreeNodeUtil> trees = new ArrayList<>();
            for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities1) {
                if (!this.idIsInArr(quDuanInfoPropertyEntity.getId())) {
                    TreeNodeUtil tree = new TreeNodeUtil();
                    tree.setId(quDuanInfoPropertyEntity.getId().longValue());
                    tree.setLabel(quDuanInfoPropertyEntity.getName());
                    trees.add(tree);
                }
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
    public List<TreeNodeUtil> findByTypess(Set<Integer> types) {
        types = types.stream().sorted(Integer::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
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


    /**
     * 日报表
     *
     * @param czId        车站id
     * @param time        时间
     * @param properties  属性集合
     * @param isDianMaHua 是否电码化
     * @return
     */
    @Override
    public JSONObject dayReport(Integer czId, Date time, Integer[] properties, String qName, Boolean isDianMaHua) {
        JSONObject jo = new JSONObject();
        if (properties == null || properties.length == 0) {
            jo.put("title", new JSONArray());
            jo.put("data", new JSONArray());
            return jo;
        }
        //表头数组
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.findByIds(properties);
//        quDuanInfoPropertyEntities.add(new QuDuanInfoPropertyEntity(-1, "区段运用名称", null));
        List<TreeNodeUtil> first = new ArrayList<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setLabel(quDuanInfoPropertyEntity.getName());
            List<TreeNodeUtil> second = new ArrayList<>();
            treeNodeUtil.setChildren(second);
            TreeNodeUtil treeNodeUtil1 = null;
            TreeNodeUtil treeNodeUtil2 = null;
            switch (quDuanInfoPropertyEntity.getId()) {
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 21:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel(quDuanInfoPropertyEntity.getName());
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setLabel("备机" + quDuanInfoPropertyEntity.getName());
                    break;
                case 17:
                case 18:
                case 19:
                case 20:
                case 22:
                case 23:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setLabel("并机" + quDuanInfoPropertyEntity.getName());
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    treeNodeUtil1 = new TreeNodeUtil();
                    treeNodeUtil2 = new TreeNodeUtil();
                    treeNodeUtil1.setLabel("主信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil2.setLabel("调信号" + quDuanInfoPropertyEntity.getName());
                    break;
            }
            if (treeNodeUtil1 != null)
                second.add(treeNodeUtil1);
            if (treeNodeUtil2 != null)
                second.add(treeNodeUtil2);
            if (treeNodeUtil1 != null) {
                List<TreeNodeUtil> third = new ArrayList<>();
                treeNodeUtil1.setChildren(third);
                TreeNodeUtil treeNodeUtilMax = new TreeNodeUtil();
                treeNodeUtilMax.setLabel("max");
                treeNodeUtilMax.setValue("a" + quDuanInfoPropertyEntity.getId());
                TreeNodeUtil treeNodeUtilMin = new TreeNodeUtil();
                treeNodeUtilMin.setLabel("min");
                treeNodeUtilMin.setValue("b" + quDuanInfoPropertyEntity.getId());
                third.add(treeNodeUtilMax);
                third.add(treeNodeUtilMin);
            }
            if (treeNodeUtil2 != null) {
                List<TreeNodeUtil> third = new ArrayList<>();
                treeNodeUtil2.setChildren(third);
                TreeNodeUtil treeNodeUtilMax = new TreeNodeUtil();
                treeNodeUtilMax.setLabel("max");
                treeNodeUtilMax.setValue("c" + quDuanInfoPropertyEntity.getId());
                TreeNodeUtil treeNodeUtilMin = new TreeNodeUtil();
                treeNodeUtilMin.setLabel("min");
                treeNodeUtilMin.setValue("d" + quDuanInfoPropertyEntity.getId());
                third.add(treeNodeUtilMax);
                third.add(treeNodeUtilMin);
            }
            first.add(treeNodeUtil);
        }
        jo.put("title", first);

        //表头对应数据数组

        List<Map<String, Object>> mapss = new ArrayList<>();
        String tableName = StringUtil.getTableName(czId, time);
        if (this.isTableExist(tableName)) {
            List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, qName, isDianMaHua);
            List<Map<String, Object>> finalMaps = quDuanInfoDaoV2.selectStatisticsByCzIdAndTime(czId, properties, (int) (DateUtil.beginOfDay(time).getTime() / 1000), (int) (DateUtil.endOfDay(time).getTime() / 1000), tableName);
            for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
                for (Map<String, Object> map : finalMaps) {
                    Integer qid = (Integer) map.get("v1");
                    if (quDuanBaseEntity.getQdid().equals(qid)) {
                        Map<String, Object> maps = new HashMap<>();
                        for (String s : map.keySet()) {
                            if (!"v1".equals(s)) {
                                List<MenXianEntity> menXianEntities = menXianService.findByCzIdAndQuduanIdAndPropertyId(czId, qid, Integer.valueOf(s.substring(1)));
                                MenXianEntity m1 = null;
                                MenXianEntity m2 = null;
                                if (menXianEntities.size() == 1)
                                    m1 = menXianEntities.get(0);
                                if (menXianEntities.size() == 2) {
                                    m1 = menXianEntities.get(0);
                                    m2 = menXianEntities.get(1);
                                }
                                String value = map.get(s).toString();
                                switch (s) {
                                    case "a4":
                                        Map<String, Object> ta4 = this.findByQidAndNV(qid, "v3", value, tableName);
                                        JSONObject a4 = new JSONObject();
                                        a4.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a4.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a4.put("text", ja);
                                            }
                                        }
                                        a4.put("time", ta4.get("createtime"));
                                        maps.put("a4", a4);
                                        break;
                                    case "b4":
                                        Map<String, Object> tb4 = this.findByQidAndNV(qid, "v3", value, tableName);
                                        JSONObject b4 = new JSONObject();
                                        b4.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b4.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b4.put("text", ja);
                                            }
                                        }
                                        b4.put("time", tb4.get("createtime"));
                                        maps.put("b4", b4);
                                        break;
                                    case "c4":
                                        Map<String, Object> tc4 = this.findByQidAndNV(qid, "v9", value, tableName);
                                        JSONObject c4 = new JSONObject();
                                        c4.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                c4.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                c4.put("text", ja);
                                            }
                                        }
                                        c4.put("time", tc4.get("createtime"));
                                        maps.put("c4", c4);
                                        break;
                                    case "d4":
                                        Map<String, Object> td4 = this.findByQidAndNV(qid, "v9", value, tableName);
                                        JSONObject d4 = new JSONObject();
                                        d4.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d4.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                d4.put("text", ja);
                                            }
                                        }
                                        d4.put("time", td4.get("createtime"));
                                        maps.put("d4", d4);
                                        break;
                                    case "a5":
                                        Map<String, Object> ta5 = this.findByQidAndNV(qid, "v4", value, tableName);
                                        JSONObject a5 = new JSONObject();
                                        a5.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                a5.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a5.put("text", ja);
                                            }
                                        }
                                        a5.put("time", ta5.get("createtime"));
                                        maps.put("a5", a5);
                                        break;
                                    case "b5":
                                        Map<String, Object> tb5 = this.findByQidAndNV(qid, "v4", value, tableName);
                                        JSONObject b5 = new JSONObject();
                                        b5.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b5.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b5.put("text", ja);
                                            }
                                        }
                                        b5.put("time", tb5.get("createtime"));
                                        maps.put("b5", b5);
                                        break;
                                    case "c5":
                                        Map<String, Object> tc5 = this.findByQidAndNV(qid, "v10", value, tableName);
                                        JSONObject c5 = new JSONObject();
                                        c5.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                c5.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c5.put("text", ja);
                                            }
                                        }
                                        c5.put("time", tc5.get("createtime"));
                                        maps.put("c5", c5);
                                        break;
                                    case "d5":
                                        Map<String, Object> td5 = this.findByQidAndNV(qid, "v10", value, tableName);
                                        JSONObject d5 = new JSONObject();
                                        d5.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d5.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d5.put("text", ja);
                                            }
                                        }
                                        d5.put("time", td5.get("createtime"));
                                        maps.put("d5", d5);
                                        break;
                                    case "a6":
                                        Map<String, Object> ta6 = this.findByQidAndNV(qid, "v5", value, tableName);
                                        JSONObject a6 = new JSONObject();
                                        a6.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                a6.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a6.put("text", ja);
                                            }
                                        }
                                        a6.put("time", ta6.get("createtime"));
                                        maps.put("a6", a6);
                                        break;
                                    case "b6":
                                        Map<String, Object> tb6 = this.findByQidAndNV(qid, "v5", value, tableName);
                                        JSONObject b6 = new JSONObject();
                                        b6.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b6.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b6.put("text", ja);
                                            }
                                        }
                                        b6.put("time", tb6.get("createtime"));
                                        maps.put("b6", b6);
                                        break;
                                    case "c6":
                                        Map<String, Object> tc6 = this.findByQidAndNV(qid, "v11", value, tableName);
                                        JSONObject c6 = new JSONObject();
                                        c6.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c6.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c6.put("text", ja);
                                            }
                                        }
                                        c6.put("time", tc6.get("createtime"));
                                        maps.put("c6", c6);
                                        break;
                                    case "d6":
                                        Map<String, Object> td6 = this.findByQidAndNV(qid, "v11", value, tableName);
                                        JSONObject d6 = new JSONObject();
                                        d6.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d6.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d6.put("text", ja);
                                            }
                                        }
                                        d6.put("time", td6.get("createtime"));
                                        maps.put("d6", d6);
                                        break;
                                    case "a7":
                                        Map<String, Object> ta7 = this.findByQidAndNV(qid, "v6", value, tableName);
                                        JSONObject a7 = new JSONObject();
                                        a7.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a7.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a7.put("text", ja);
                                            }
                                        }
                                        a7.put("time", ta7.get("createtime"));
                                        maps.put("a7", a7);
                                        break;
                                    case "b7":
                                        Map<String, Object> tb7 = this.findByQidAndNV(qid, "v6", value, tableName);
                                        JSONObject b7 = new JSONObject();
                                        b7.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b7.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b7.put("text", ja);
                                            }
                                        }
                                        b7.put("time", tb7.get("createtime"));
                                        maps.put("b7", b7);
                                        break;
                                    case "c7":
                                        Map<String, Object> tc7 = this.findByQidAndNV(qid, "v12", value, tableName);
                                        JSONObject c7 = new JSONObject();
                                        c7.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c7.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                c7.put("text", ja);
                                            }
                                        }
                                        c7.put("time", tc7.get("createtime"));
                                        maps.put("c7", c7);
                                        break;
                                    case "d7":
                                        Map<String, Object> td7 = this.findByQidAndNV(qid, "v12", value, tableName);
                                        JSONObject d7 = new JSONObject();
                                        d7.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d7.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                d7.put("text", ja);
                                            }
                                        }
                                        d7.put("time", td7.get("createtime"));
                                        maps.put("d7", d7);
                                        break;
                                    case "a8":
                                        Map<String, Object> ta8 = this.findByQidAndNV(qid, "v7", value, tableName);
                                        JSONObject a8 = new JSONObject();
                                        a8.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a8.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a8.put("text", ja);
                                            }
                                        }
                                        a8.put("time", ta8.get("createtime"));
                                        maps.put("a8", a8);
                                        break;
                                    case "b8":
                                        Map<String, Object> tb8 = this.findByQidAndNV(qid, "v7", value, tableName);
                                        JSONObject b8 = new JSONObject();
                                        b8.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b8.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b8.put("text", ja);
                                            }
                                        }
                                        b8.put("time", tb8.get("createtime"));
                                        maps.put("b8", b8);
                                        break;
                                    case "c8":
                                        Map<String, Object> tc8 = this.findByQidAndNV(qid, "v13", value, tableName);
                                        JSONObject c8 = new JSONObject();
                                        c8.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c8.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                c8.put("text", ja);
                                            }
                                        }
                                        c8.put("time", tc8.get("createtime"));
                                        maps.put("c8", c8);
                                        break;
                                    case "d8":
                                        Map<String, Object> td8 = this.findByQidAndNV(qid, "v13", value, tableName);
                                        JSONObject d8 = new JSONObject();
                                        d8.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                d8.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                d8.put("text", ja);
                                            }
                                        }
                                        d8.put("time", td8.get("createtime"));
                                        maps.put("d8", d8);
                                        break;

                                    case "a11":
                                        Map<String, Object> ta11 = this.findByQidAndNV(qid, "v15", value, tableName);
                                        JSONObject a11 = new JSONObject();
                                        a11.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a11.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a11.put("text", ja);
                                            }
                                        }
                                        a11.put("time", ta11.get("createtime"));
                                        maps.put("a11", a11);
                                        break;
                                    case "b11":
                                        Map<String, Object> tb11 = this.findByQidAndNV(qid, "v15", value, tableName);
                                        JSONObject b11 = new JSONObject();
                                        b11.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b11.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b11.put("text", ja);
                                            }
                                        }
                                        b11.put("time", tb11.get("createtime"));
                                        maps.put("b11", b11);
                                        break;
                                    case "a12":
                                        Map<String, Object> ta12 = this.findByQidAndNV(qid, "v16", value, tableName);
                                        JSONObject a12 = new JSONObject();
                                        a12.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a12.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a12.put("text", ja);
                                            }
                                        }
                                        a12.put("time", ta12.get("createtime"));
                                        maps.put("a12", a12);
                                        break;
                                    case "b12":
                                        Map<String, Object> tb12 = this.findByQidAndNV(qid, "v16", value, tableName);
                                        JSONObject b12 = new JSONObject();
                                        b12.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b12.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                tb12.put("text", ja);
                                            }
                                        }
                                        b12.put("time", tb12.get("createtime"));
                                        maps.put("b12", b12);
                                        break;
                                    case "a13":
                                        Map<String, Object> ta13 = this.findByQidAndNV(qid, "v17", value, tableName);
                                        JSONObject a13 = new JSONObject();
                                        a13.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a13.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a13.put("text", ja);
                                            }
                                        }
                                        a13.put("time", ta13.get("createtime"));
                                        maps.put("a11", a13);
                                        break;
                                    case "b13":
                                        Map<String, Object> tb13 = this.findByQidAndNV(qid, "v17", value, tableName);
                                        Integer status13 = (Integer) tb13.get("v2");
                                        JSONObject b13 = new JSONObject();
                                        b13.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status13 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b13.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            } else if (status13 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b13.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            ja.add(text);
                                            b13.put("text", ja);

                                        }
                                        b13.put("time", tb13.get("createtime"));
                                        maps.put("b13", b13);
                                        break;
                                    case "a14":
                                        Map<String, Object> ta14 = this.findByQidAndNV(qid, "v18", value, tableName);
                                        JSONObject a14 = new JSONObject();
                                        a14.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a14.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a14.put("text", ja);
                                            }
                                        }
                                        a14.put("time", ta14.get("createtime"));
                                        maps.put("a14", a14);
                                        break;
                                    case "b14":
                                        Map<String, Object> tb14 = this.findByQidAndNV(qid, "v18", value, tableName);
                                        Integer status14 = (Integer) tb14.get("v2");
                                        JSONObject b14 = new JSONObject();
                                        b14.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status14 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b14.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            } else if (status14 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b14.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            ja.add(text);
                                            b14.put("text", ja);

                                        }
                                        b14.put("time", tb14.get("createtime"));
                                        maps.put("b14", b14);
                                        break;
                                    case "a15":
                                        Map<String, Object> ta15 = this.findByQidAndNV(qid, "v19", value, tableName);
                                        JSONObject a15 = new JSONObject();
                                        a15.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a15.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a15.put("text", ja);
                                            }
                                        }
                                        a15.put("time", ta15.get("createtime"));
                                        maps.put("a15", a15);
                                        break;
                                    case "b15":
                                        Map<String, Object> tb15 = this.findByQidAndNV(qid, "v19", value, tableName);
                                        Integer status15 = (Integer) tb15.get("v2");
                                        JSONObject b15 = new JSONObject();
                                        b15.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status15 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b15.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            } else if (status15 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b15.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b15.put("text", ja);

                                        }
                                        b15.put("time", b15.get("createtime"));
                                        maps.put("b15", b15);
                                        break;


                                    case "a16":
                                        Map<String, Object> ta16 = this.findByQidAndNV(qid, "v20", value, tableName);
                                        JSONObject a16 = new JSONObject();
                                        a16.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a16.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a16.put("text", ja);
                                            }
                                        }
                                        a16.put("time", ta16.get("createtime"));
                                        maps.put("a16", a16);
                                        break;
                                    case "b16":
                                        Map<String, Object> tb16 = this.findByQidAndNV(qid, "v20", value, tableName);
                                        Integer status16 = (Integer) tb16.get("v2");
                                        JSONObject b16 = new JSONObject();
                                        b16.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status16 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b16.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            } else if (status16 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b16.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            ja.add(text);
                                            b16.put("text", ja);

                                        }
                                        b16.put("time", tb16.get("createtime"));
                                        maps.put("b16", b16);
                                        break;
                                    case "a17":
                                        Map<String, Object> ta17 = this.findByQidAndNV(qid, "v21", value, tableName);
                                        Integer status171 = (Integer) ta17.get("v2");
                                        JSONObject a17 = new JSONObject();
                                        a17.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status171 == 1) {
                                                if (!"——".equals(m1.getAlarmSuperiorK())) {
                                                    a17.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperiorK()));
                                                    text.put("label", "报警上限(空闲)");
                                                    text.put("value", m1.getAlarmSuperiorK());
                                                }
                                            } else if (status171 == 2) {
                                                if (!"——".equals(m1.getAlarmSuperiorZ())) {
                                                    a17.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperiorZ()));
                                                    text.put("label", "报警上限(占用)");
                                                    text.put("value", m1.getAlarmSuperiorZ());
                                                }
                                            }
                                            ja.add(text);
                                            a17.put("text", ja);
                                        }
                                        a17.put("time", ta17.get("createtime"));
                                        maps.put("a17", a17);
                                        break;
                                    case "b17":
                                        Map<String, Object> tb17 = this.findByQidAndNV(qid, "v21", value, tableName);
                                        JSONObject b17 = new JSONObject();
                                        b17.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b17.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b17.put("text", ja);
                                            }
                                        }
                                        b17.put("time", tb17.get("createtime"));
                                        maps.put("b17", b17);
                                        break;
                                    case "c17":
                                        Map<String, Object> tc17 = this.findByQidAndNV(qid, "v26", value, tableName);
                                        Integer status172 = (Integer) tc17.get("v2");
                                        JSONObject c17 = new JSONObject();
                                        c17.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status172 == 1) {
                                                if (!"——".equals(m2.getAlarmSuperiorK())) {
                                                    c17.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperiorK()));
                                                    text.put("label", "报警上限(空闲)");
                                                    text.put("value", m2.getAlarmSuperiorK());
                                                }
                                            } else if (status172 == 2) {
                                                if (!"——".equals(m2.getAlarmSuperiorZ())) {
                                                    c17.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperiorZ()));
                                                    text.put("label", "报警上限(占用)");
                                                    text.put("value", m2.getAlarmSuperiorZ());
                                                }
                                            }
                                            ja.add(text);
                                            c17.put("text", ja);
                                        }
                                        c17.put("time", tc17.get("createtime"));
                                        maps.put("c17", c17);
                                        break;
                                    case "d17":
                                        Map<String, Object> td17 = this.findByQidAndNV(qid, "v26", value, tableName);
                                        JSONObject d17 = new JSONObject();
                                        d17.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d17.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d17.put("text", ja);
                                            }
                                        }
                                        d17.put("time", td17.get("createtime"));
                                        maps.put("d17", d17);
                                        break;
                                    case "a18":
                                        Map<String, Object> ta18 = this.findByQidAndNV(qid, "v22", value, tableName);
                                        Integer status181 = (Integer) ta18.get("v2");
                                        JSONObject a18 = new JSONObject();
                                        a18.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status181 == 1) {
                                                if (!"——".equals(m1.getAlarmSuperiorK())) {
                                                    a18.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperiorK()));
                                                    text.put("label", "报警上限(空闲)");
                                                    text.put("value", m1.getAlarmSuperiorK());
                                                }
                                            }
                                            ja.add(text);
                                            a18.put("text", ja);
                                        }
                                        a18.put("time", ta18.get("createtime"));
                                        maps.put("a18", a18);
                                        break;
                                    case "b18":
                                        Map<String, Object> tb18 = this.findByQidAndNV(qid, "v22", value, tableName);
                                        Integer status182 = (Integer) tb18.get("v2");
                                        JSONObject b18 = new JSONObject();
                                        b18.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status182 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b18.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status182 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b18.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b18.put("text", ja);
                                        }
                                        b18.put("time", tb18.get("createtime"));
                                        maps.put("b18", b18);
                                        break;
                                    case "c18":
                                        Map<String, Object> tc18 = this.findByQidAndNV(qid, "v27", value, tableName);
                                        Integer status183 = (Integer) tc18.get("v2");
                                        JSONObject c18 = new JSONObject();
                                        c18.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status183 == 1) {
                                                if (!"——".equals(m2.getAlarmSuperiorK())) {
                                                    c18.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperiorK()));
                                                    text.put("label", "报警上限(空闲)");
                                                    text.put("value", m2.getAlarmSuperiorK());
                                                }
                                            }
                                            ja.add(text);
                                            c18.put("text", ja);
                                        }
                                        c18.put("time", tc18.get("createtime"));
                                        maps.put("c18", c18);
                                        break;
                                    case "d18":
                                        Map<String, Object> td18 = this.findByQidAndNV(qid, "v27", value, tableName);
                                        Integer status184 = (Integer) td18.get("v2");
                                        JSONObject d18 = new JSONObject();
                                        d18.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status184 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    d18.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status184 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d18.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d18.put("text", ja);
                                        }
                                        d18.put("time", td18.get("createtime"));
                                        maps.put("d18", d18);
                                        break;
                                    case "a19":
                                        Map<String, Object> ta19 = this.findByQidAndNV(qid, "v23", value, tableName);
                                        JSONObject a19 = new JSONObject();
                                        a19.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a19.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a19.put("text", ja);
                                            }
                                        }
                                        a19.put("time", ta19.get("createtime"));
                                        maps.put("a18", a19);
                                        break;
                                    case "b19":
                                        Map<String, Object> tb19 = this.findByQidAndNV(qid, "v23", value, tableName);
                                        JSONObject b19 = new JSONObject();
                                        Integer status192 = (Integer) tb19.get("v2");
                                        b19.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status192 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b19.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status192 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b19.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b19.put("text", ja);
                                        }
                                        b19.put("time", tb19.get("createtime"));
                                        maps.put("b19", b19);
                                        break;
                                    case "c19":
                                        Map<String, Object> tc19 = this.findByQidAndNV(qid, "v28", value, tableName);
                                        JSONObject c19 = new JSONObject();
                                        c19.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c19.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c19.put("text", ja);
                                            }
                                        }
                                        c19.put("time", tc19.get("createtime"));
                                        maps.put("a18", c19);
                                        break;
                                    case "d19":
                                        Map<String, Object> td19 = this.findByQidAndNV(qid, "v28", value, tableName);
                                        JSONObject d19 = new JSONObject();
                                        Integer status194 = (Integer) td19.get("v2");
                                        d19.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status194 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    d19.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmLowerK());
                                                }
                                            }
                                            if (status194 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d19.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d19.put("text", ja);
                                        }
                                        d19.put("time", td19.get("createtime"));
                                        maps.put("d19", d19);
                                        break;

                                    case "a24":
                                        Map<String, Object> ta24 = this.findByQidAndNV(qid, "v42", value, tableName);
                                        JSONObject a24 = new JSONObject();
                                        a24.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (!"——".equals(m1.getAlarmSuperiorK())) {
                                                a24.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperiorK()));
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperiorK());
                                                ja.add(text);
                                                a24.put("text", ja);
                                            }
                                        }
                                        a24.put("time", ta24.get("createtime"));
                                        maps.put("a24", a24);
                                        break;
                                    case "b24":
                                        Map<String, Object> td24 = this.findByQidAndNV(qid, "v42", value, tableName);
                                        JSONObject b24 = new JSONObject();
                                        Integer status242 = (Integer) td24.get("v2");
                                        b24.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status242 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    b24.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmSuperiorK());
                                                }
                                            }
                                            ja.add(text);
                                            b24.put("text", ja);
                                        }
                                        b24.put("time", td24.get("createtime"));
                                        maps.put("b24", b24);
                                        break;
                                    case "a25":
                                        Map<String, Object> ta25 = this.findByQidAndNV(qid, "v43", value, tableName);
                                        JSONObject a25 = new JSONObject();
                                        a25.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a25.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a25.put("text", ja);
                                            }
                                        }
                                        a25.put("time", ta25.get("createtime"));
                                        maps.put("a25", a25);
                                        break;
                                    case "b25":
                                        Map<String, Object> tb25 = this.findByQidAndNV(qid, "v43", value, tableName);
                                        JSONObject b25 = new JSONObject();
                                        b25.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b25.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b25.put("text", ja);
                                            }
                                        }
                                        b25.put("time", tb25.get("createtime"));
                                        maps.put("b25", b25);
                                        break;
                                    case "a26":
                                        Map<String, Object> ta26 = this.findByQidAndNV(qid, "v44", value, tableName);
                                        JSONObject a26 = new JSONObject();
                                        a26.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a26.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a26.put("text", ja);
                                            }
                                        }
                                        a26.put("time", ta26.get("createtime"));
                                        maps.put("a26", a26);
                                        break;
                                    case "b26":
                                        Map<String, Object> tb26 = this.findByQidAndNV(qid, "v44", value, tableName);
                                        JSONObject b26 = new JSONObject();
                                        b26.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b26.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b26.put("text", ja);
                                            }
                                        }
                                        b26.put("time", tb26.get("createtime"));
                                        maps.put("b26", b26);
                                        break;
                                    case "a27":
                                        Map<String, Object> ta27 = this.findByQidAndNV(qid, "v45", value, tableName);
                                        JSONObject a27 = new JSONObject();
                                        a27.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a27.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a27.put("text", ja);
                                            }
                                        }
                                        a27.put("time", ta27.get("createtime"));
                                        maps.put("a27", a27);
                                        break;
                                    case "b27":
                                        Map<String, Object> tb27 = this.findByQidAndNV(qid, "v45", value, tableName);
                                        JSONObject b27 = new JSONObject();
                                        b27.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b27.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b27.put("text", ja);
                                            }
                                        }
                                        b27.put("time", tb27.get("createtime"));
                                        maps.put("b27", b27);
                                        break;
                                    case "a28":
                                        Map<String, Object> ta28 = this.findByQidAndNV(qid, "v46", value, tableName);
                                        JSONObject a28 = new JSONObject();
                                        a28.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a28.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a28.put("text", ja);
                                            }
                                        }
                                        a28.put("time", ta28.get("createtime"));
                                        maps.put("a28", a28);
                                        break;
                                    case "b28":
                                        Map<String, Object> tb28 = this.findByQidAndNV(qid, "v46", value, tableName);
                                        JSONObject b28 = new JSONObject();
                                        b28.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b28.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b28.put("text", ja);
                                            }
                                        }
                                        b28.put("time", tb28.get("createtime"));
                                        maps.put("b28", b28);
                                        break;
                                    case "a29":
                                        Map<String, Object> ta29 = this.findByQidAndNV(qid, "v52", value, tableName);
                                        JSONObject a29 = new JSONObject();
                                        a29.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a29.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a29.put("text", ja);
                                            }
                                        }
                                        a29.put("time", ta29.get("createtime"));
                                        maps.put("a29", a29);
                                        break;
                                    case "b29":
                                        Map<String, Object> tb29 = this.findByQidAndNV(qid, "v52", value, tableName);
                                        JSONObject b29 = new JSONObject();
                                        b29.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b29.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b29.put("text", ja);
                                            }
                                        }
                                        b29.put("time", tb29.get("createtime"));
                                        maps.put("b29", b29);
                                        break;
                                    case "a30":
                                        Map<String, Object> ta30 = this.findByQidAndNV(qid, "v54", value, tableName);
                                        JSONObject a30 = new JSONObject();
                                        a30.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a30.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a30.put("text", ja);
                                            }
                                        }
                                        a30.put("time", ta30.get("createtime"));
                                        maps.put("a30", a30);
                                        break;
                                    case "b30":
                                        Map<String, Object> tb30 = this.findByQidAndNV(qid, "v54", value, tableName);
                                        JSONObject b30 = new JSONObject();
                                        b30.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b30.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b30.put("text", ja);
                                            }
                                        }
                                        b30.put("time", tb30.get("createtime"));
                                        maps.put("b30", b30);
                                        break;
                                    case "c30":
                                        Map<String, Object> tc30 = this.findByQidAndNV(qid, "v58", value, tableName);
                                        JSONObject c30 = new JSONObject();
                                        c30.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c30.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c30.put("text", ja);
                                            }
                                        }
                                        c30.put("time", tc30.get("createtime"));
                                        maps.put("c30", c30);
                                        break;
                                    case "d30":
                                        Map<String, Object> td30 = this.findByQidAndNV(qid, "v58", value, tableName);
                                        JSONObject d30 = new JSONObject();
                                        d30.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d30.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d30.put("text", ja);
                                            }
                                        }
                                        d30.put("time", td30.get("createtime"));
                                        maps.put("d30", d30);
                                        break;
                                    case "a31":
                                        Map<String, Object> ta31 = this.findByQidAndNV(qid, "v55", value, tableName);
                                        JSONObject a31 = new JSONObject();
                                        a31.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a31.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a31.put("text", ja);
                                            }
                                        }
                                        a31.put("time", ta31.get("createtime"));
                                        maps.put("a31", a31);
                                        break;
                                    case "b31":
                                        Map<String, Object> tb31 = this.findByQidAndNV(qid, "v55", value, tableName);
                                        JSONObject b31 = new JSONObject();
                                        b31.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b31.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b31.put("text", ja);
                                            }
                                        }
                                        b31.put("time", tb31.get("createtime"));
                                        maps.put("b31", b31);
                                        break;
                                    case "c31":
                                        Map<String, Object> tc31 = this.findByQidAndNV(qid, "v59", value, tableName);
                                        JSONObject c31 = new JSONObject();
                                        c31.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c31.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c31.put("text", ja);
                                            }
                                        }
                                        c31.put("time", tc31.get("createtime"));
                                        maps.put("c31", c31);
                                        break;
                                    case "d31":
                                        Map<String, Object> td31 = this.findByQidAndNV(qid, "v59", value, tableName);
                                        JSONObject d31 = new JSONObject();
                                        d31.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d31.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d31.put("text", ja);
                                            }
                                        }
                                        d31.put("time", td31.get("createtime"));
                                        maps.put("d31", d31);
                                        break;
                                    case "a32":
                                        Map<String, Object> ta32 = this.findByQidAndNV(qid, "v56", value, tableName);
                                        JSONObject a32 = new JSONObject();
                                        a32.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a32.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a32.put("text", ja);
                                            }
                                        }
                                        a32.put("time", ta32.get("createtime"));
                                        maps.put("a32", a32);
                                        break;
                                    case "b32":
                                        Map<String, Object> tb32 = this.findByQidAndNV(qid, "v56", value, tableName);
                                        JSONObject b32 = new JSONObject();
                                        b32.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b32.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b32.put("text", ja);
                                            }
                                        }
                                        b32.put("time", tb32.get("createtime"));
                                        maps.put("b32", b32);
                                        break;
                                    case "c32":
                                        Map<String, Object> tc32 = this.findByQidAndNV(qid, "v60", value, tableName);
                                        JSONObject c32 = new JSONObject();
                                        c32.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c32.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c32.put("text", ja);
                                            }
                                        }
                                        c32.put("time", tc32.get("createtime"));
                                        maps.put("c32", c32);
                                        break;
                                    case "d32":
                                        Map<String, Object> td32 = this.findByQidAndNV(qid, "v60", value, tableName);
                                        JSONObject d32 = new JSONObject();
                                        d32.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d32.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d32.put("text", ja);
                                            }
                                        }
                                        d32.put("time", td32.get("createtime"));
                                        maps.put("d32", d32);
                                        break;
                                    case "a33":
                                        Map<String, Object> ta33 = this.findByQidAndNV(qid, "v57", value, tableName);
                                        JSONObject a33 = new JSONObject();
                                        a33.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a33.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a33.put("text", ja);
                                            }
                                        }
                                        a33.put("time", ta33.get("createtime"));
                                        maps.put("a33", a33);
                                        break;
                                    case "b33":
                                        Map<String, Object> tb33 = this.findByQidAndNV(qid, "v57", value, tableName);
                                        JSONObject b33 = new JSONObject();
                                        b33.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b33.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b33.put("text", ja);
                                            }
                                        }
                                        b33.put("time", tb33.get("createtime"));
                                        maps.put("b33", b33);
                                        break;
                                    case "c33":
                                        Map<String, Object> tc33 = this.findByQidAndNV(qid, "v61", value, tableName);
                                        JSONObject c33 = new JSONObject();
                                        c33.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c33.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c33.put("text", ja);
                                            }
                                        }
                                        c33.put("time", tc33.get("createtime"));
                                        maps.put("c33", c33);
                                        break;
                                    case "d33":
                                        Map<String, Object> td33 = this.findByQidAndNV(qid, "v61", value, tableName);
                                        JSONObject d33 = new JSONObject();
                                        d33.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmLower())) {
                                                d33.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m2.getAlarmLower());
                                                ja.add(text);
                                                d33.put("text", ja);
                                            }
                                        }
                                        d33.put("time", td33.get("createtime"));
                                        maps.put("d33", d33);
                                        break;
                                    case "a34":
                                        Map<String, Object> ta34 = this.findByQidAndNV(qid, "v62", value, tableName);
                                        JSONObject a34 = new JSONObject();
                                        a34.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a34.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a34.put("text", ja);
                                            }
                                        }
                                        a34.put("time", ta34.get("createtime"));
                                        maps.put("a34", a34);
                                        break;
                                    case "b34":
                                        Map<String, Object> tb34 = this.findByQidAndNV(qid, "v62", value, tableName);
                                        JSONObject b34 = new JSONObject();
                                        Integer status342 = (Integer) tb34.get("v2");
                                        b34.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status342 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b34.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status342 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b34.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b34.put("text", ja);
                                        }
                                        b34.put("time", tb34.get("createtime"));
                                        maps.put("b19", b34);
                                        break;
                                    case "c34":
                                        Map<String, Object> tc34 = this.findByQidAndNV(qid, "v66", value, tableName);
                                        JSONObject c34 = new JSONObject();
                                        c34.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c34.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c34.put("text", ja);
                                            }
                                        }
                                        c34.put("time", tc34.get("createtime"));
                                        maps.put("c34", c34);
                                        break;
                                    case "d34":
                                        Map<String, Object> td34 = this.findByQidAndNV(qid, "v66", value, tableName);
                                        JSONObject d34 = new JSONObject();
                                        Integer status344 = (Integer) td34.get("v2");
                                        d34.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status344 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    d34.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmLowerK());
                                                }
                                            }
                                            if (status344 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d34.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d34.put("text", ja);
                                        }
                                        d34.put("time", td34.get("createtime"));
                                        maps.put("d34", d34);
                                        break;
                                    case "a35":
                                        Map<String, Object> ta35 = this.findByQidAndNV(qid, "v63", value, tableName);
                                        JSONObject a35 = new JSONObject();
                                        a35.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a35.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a35.put("text", ja);
                                            }
                                        }
                                        a35.put("time", ta35.get("createtime"));
                                        maps.put("a34", a35);
                                        break;
                                    case "b35":
                                        Map<String, Object> tb35 = this.findByQidAndNV(qid, "v63", value, tableName);
                                        JSONObject b35 = new JSONObject();
                                        Integer status352 = (Integer) tb35.get("v2");
                                        b35.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status352 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b35.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status352 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b35.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b35.put("text", ja);
                                        }
                                        b35.put("time", tb35.get("createtime"));
                                        maps.put("b35", b35);
                                        break;
                                    case "c35":
                                        Map<String, Object> tc35 = this.findByQidAndNV(qid, "v67", value, tableName);
                                        JSONObject c35 = new JSONObject();
                                        c35.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c35.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c35.put("text", ja);
                                            }
                                        }
                                        c35.put("time", tc35.get("createtime"));
                                        maps.put("c35", c35);
                                        break;
                                    case "d35":
                                        Map<String, Object> td35 = this.findByQidAndNV(qid, "v67", value, tableName);
                                        JSONObject d35 = new JSONObject();
                                        Integer status354 = (Integer) td35.get("v2");
                                        d35.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status354 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    d35.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmLowerK());
                                                }
                                            }
                                            if (status354 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d35.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d35.put("text", ja);
                                        }
                                        d35.put("time", td35.get("createtime"));
                                        maps.put("d35", d35);
                                        break;
                                    case "a36":
                                        Map<String, Object> ta36 = this.findByQidAndNV(qid, "v64", value, tableName);
                                        JSONObject a36 = new JSONObject();
                                        a36.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a36.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a36.put("text", ja);
                                            }
                                        }
                                        a36.put("time", ta36.get("createtime"));
                                        maps.put("a36", a36);
                                        break;
                                    case "b36":
                                        Map<String, Object> tb36 = this.findByQidAndNV(qid, "v64", value, tableName);
                                        JSONObject b36 = new JSONObject();
                                        Integer status362 = (Integer) tb36.get("v2");
                                        b36.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status362 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b36.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status362 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b36.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b36.put("text", ja);
                                        }
                                        b36.put("time", tb36.get("createtime"));
                                        maps.put("b36", b36);
                                        break;
                                    case "c36":
                                        Map<String, Object> tc36 = this.findByQidAndNV(qid, "v68", value, tableName);
                                        JSONObject c36 = new JSONObject();
                                        c36.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c36.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c36.put("text", ja);
                                            }
                                        }
                                        c36.put("time", tc36.get("createtime"));
                                        maps.put("c36", c36);
                                        break;
                                    case "d36":
                                        Map<String, Object> td36 = this.findByQidAndNV(qid, "v68", value, tableName);
                                        JSONObject d36 = new JSONObject();
                                        Integer status364 = (Integer) td36.get("v2");
                                        d36.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status364 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    d36.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmLowerK());
                                                }
                                            }
                                            if (status364 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d36.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d36.put("text", ja);
                                        }
                                        d36.put("time", td36.get("createtime"));
                                        maps.put("d36", d36);
                                        break;
                                    case "a37":
                                        Map<String, Object> ta37 = this.findByQidAndNV(qid, "v65", value, tableName);
                                        JSONObject a37 = new JSONObject();
                                        a37.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a37.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a37.put("text", ja);
                                            }
                                        }
                                        a37.put("time", ta37.get("createtime"));
                                        maps.put("a37", a37);
                                        break;
                                    case "b37":
                                        Map<String, Object> tb37 = this.findByQidAndNV(qid, "v65", value, tableName);
                                        JSONObject b37 = new JSONObject();
                                        Integer status372 = (Integer) tb37.get("v2");
                                        b37.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status372 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b37.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status372 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b37.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b37.put("text", ja);
                                        }
                                        b37.put("time", tb37.get("createtime"));
                                        maps.put("b37", b37);
                                        break;
                                    case "c37":
                                        Map<String, Object> tc37 = this.findByQidAndNV(qid, "v68", value, tableName);
                                        JSONObject c37 = new JSONObject();
                                        c37.put("value", value);
                                        if (m2 != null) {
                                            if (!"——".equals(m2.getAlarmSuperior())) {
                                                c37.put("isOver", Double.parseDouble(value) > Double.parseDouble(m2.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m2.getAlarmSuperior());
                                                ja.add(text);
                                                c37.put("text", ja);
                                            }
                                        }
                                        c37.put("time", tc37.get("createtime"));
                                        maps.put("c37", c37);
                                        break;
                                    case "d37":
                                        Map<String, Object> td37 = this.findByQidAndNV(qid, "v65", value, tableName);
                                        JSONObject d37 = new JSONObject();
                                        Integer status374 = (Integer) td37.get("v2");
                                        d37.put("value", value);
                                        if (m2 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status374 == 1) {
                                                if (!"——".equals(m2.getAlarmLowerK())) {
                                                    d37.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m2.getAlarmLowerK());
                                                }
                                            }
                                            if (status374 == 2) {
                                                if (!"——".equals(m2.getAlarmLowerZ())) {
                                                    d37.put("isOver", Double.parseDouble(value) < Double.parseDouble(m2.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m2.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            d37.put("text", ja);
                                        }
                                        d37.put("time", td37.get("createtime"));
                                        maps.put("d37", d37);
                                        break;
                                    case "a38":
                                        Map<String, Object> ta38 = this.findByQidAndNV(qid, "v47", value, tableName);
                                        JSONObject a38 = new JSONObject();
                                        a38.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a38.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a38.put("text", ja);
                                            }
                                        }
                                        a38.put("time", ta38.get("createtime"));
                                        maps.put("a37", a38);
                                        break;
                                    case "b38":
                                        Map<String, Object> tb38 = this.findByQidAndNV(qid, "v47", value, tableName);
                                        JSONObject b38 = new JSONObject();
                                        Integer status382 = (Integer) tb38.get("v2");
                                        b38.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status382 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b38.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status382 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b38.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b38.put("text", ja);
                                        }
                                        b38.put("time", tb38.get("createtime"));
                                        maps.put("b38", b38);
                                        break;
                                    case "a39":
                                        Map<String, Object> ta39 = this.findByQidAndNV(qid, "v48", value, tableName);
                                        JSONObject a39 = new JSONObject();
                                        a39.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a39.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a39.put("text", ja);
                                            }
                                        }
                                        a39.put("time", ta39.get("createtime"));
                                        maps.put("a37", a39);
                                        break;
                                    case "b39":
                                        Map<String, Object> tb39 = this.findByQidAndNV(qid, "v48", value, tableName);
                                        JSONObject b39 = new JSONObject();
                                        Integer status392 = (Integer) tb39.get("v2");
                                        b39.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status392 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b39.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status392 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b39.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b39.put("text", ja);
                                        }
                                        b39.put("time", tb39.get("createtime"));
                                        maps.put("b39", b39);
                                        break;
                                    case "a40":
                                        Map<String, Object> ta40 = this.findByQidAndNV(qid, "v49", value, tableName);
                                        JSONObject a40 = new JSONObject();
                                        a40.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a40.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a40.put("text", ja);
                                            }
                                        }
                                        a40.put("time", ta40.get("createtime"));
                                        maps.put("a37", a40);
                                        break;
                                    case "b40":
                                        Map<String, Object> tb40 = this.findByQidAndNV(qid, "v49", value, tableName);
                                        JSONObject b40 = new JSONObject();
                                        Integer status402 = (Integer) tb40.get("v2");
                                        b40.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status402 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b40.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status402 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b40.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b40.put("text", ja);
                                        }
                                        b40.put("time", tb40.get("createtime"));
                                        maps.put("b40", b40);
                                        break;
                                    case "a41":
                                        Map<String, Object> ta41 = this.findByQidAndNV(qid, "v50", value, tableName);
                                        JSONObject a41 = new JSONObject();
                                        a41.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a41.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a41.put("text", ja);
                                            }
                                        }
                                        a41.put("time", ta41.get("createtime"));
                                        maps.put("a41", a41);
                                        break;
                                    case "b41":
                                        Map<String, Object> tb41 = this.findByQidAndNV(qid, "v50", value, tableName);
                                        JSONObject b41 = new JSONObject();
                                        Integer status412 = (Integer) tb41.get("v2");
                                        b41.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status412 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b41.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status412 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b41.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b41.put("text", ja);
                                        }
                                        b41.put("time", tb41.get("createtime"));
                                        maps.put("b41", b41);
                                        break;
                                    case "a42":
                                        Map<String, Object> ta42 = this.findByQidAndNV(qid, "v51", value, tableName);
                                        JSONObject a42 = new JSONObject();
                                        a42.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a42.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a42.put("text", ja);
                                            }
                                        }
                                        a42.put("time", ta42.get("createtime"));
                                        maps.put("a42", a42);
                                        break;
                                    case "b42":
                                        Map<String, Object> tb42 = this.findByQidAndNV(qid, "v51", value, tableName);
                                        JSONObject b42 = new JSONObject();
                                        Integer status422 = (Integer) tb42.get("v2");
                                        b42.put("value", value);
                                        if (m1 != null) {
                                            JSONArray ja = new JSONArray();
                                            JSONObject text = new JSONObject();
                                            if (status422 == 1) {
                                                if (!"——".equals(m1.getAlarmLowerK())) {
                                                    b42.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerK()));
                                                    text.put("label", "报警下限(空闲)");
                                                    text.put("value", m1.getAlarmLowerK());
                                                }
                                            }
                                            if (status422 == 2) {
                                                if (!"——".equals(m1.getAlarmLowerZ())) {
                                                    b42.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLowerZ()));
                                                    text.put("label", "报警下限(占用)");
                                                    text.put("value", m1.getAlarmLowerZ());
                                                }
                                            }
                                            ja.add(text);
                                            b42.put("text", ja);
                                        }
                                        b42.put("time", tb42.get("createtime"));
                                        maps.put("b42", b42);
                                        break;
                                    case "a43":
                                        Map<String, Object> ta43 = this.findByQidAndNV(qid, "v53", value, tableName);
                                        JSONObject a43 = new JSONObject();
                                        a43.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmSuperior())) {
                                                a43.put("isOver", Double.parseDouble(value) > Double.parseDouble(m1.getAlarmSuperior()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警上限");
                                                text.put("value", m1.getAlarmSuperior());
                                                ja.add(text);
                                                a43.put("text", ja);
                                            }
                                        }
                                        a43.put("time", ta43.get("createtime"));
                                        maps.put("a43", a43);
                                        break;
                                    case "b43":
                                        Map<String, Object> tb43 = this.findByQidAndNV(qid, "v53", value, tableName);
                                        JSONObject b43 = new JSONObject();
                                        b43.put("value", value);
                                        if (m1 != null) {
                                            if (!"——".equals(m1.getAlarmLower())) {
                                                b43.put("isOver", Double.parseDouble(value) < Double.parseDouble(m1.getAlarmLower()));
                                                JSONArray ja = new JSONArray();
                                                JSONObject text = new JSONObject();
                                                text.put("label", "报警下限");
                                                text.put("value", m1.getAlarmLower());
                                                ja.add(text);
                                                b43.put("text", ja);
                                            }
                                        }
                                        b43.put("time", tb43.get("createtime"));
                                        maps.put("b43", b43);
                                        break;
                                }
                            }
                        }

                        //GJ采集 落下状态
                        Integer a21 = (Integer) map.get("a21");
                        if (a21 == 2) {
                            map.put("a13", "");//受端电缆侧主电压V
                            map.put("b13", "");
                            map.put("a15", "");//受端电缆侧电流mA
                            map.put("b15", "");
                            map.put("a16", ""); //轨入电压V
                            map.put("b16", "");
                            map.put("a17", "");//主接入电压mV
                            map.put("b17", "");
                            map.put("c17", "");
                            map.put("d17", "");

                            map.put("a38", "");//JBP电缆电流mA
                            map.put("b38", "");
                            map.put("a39", "");//JBP长内电流A
                            map.put("b39", "");
                            map.put("a40", "");//JBP长外电流A
                            map.put("b40", "");
                            map.put("a41", "");//JBP短内电流A
                            map.put("b41", "");
                            map.put("a42", "");//JBP短外电流A
                            map.put("b42", "");
                            map.put("a43", "");//JBP温度°C
                            map.put("b43", "");
                        }
                        //后方GJ采集 落下状态
                        Integer a22 = (Integer) map.get("a22");//主
                        Integer c22 = (Integer) map.get("c22");//并
                        if (a22 == 2 || c22 == 2) {
                            map.put("a14", "");//受端电缆侧调电压V
                            map.put("b14", "");
                            map.put("a18", "");//调接入电压mV
                            map.put("b18", "");

                            map.put("c34", "");//JBA长内电流A(调信号)
                            map.put("d34", "");
                            map.put("c35", "");//JBA长外电流A(调信号)
                            map.put("d35", "");
                            map.put("c36", "");//JBA短内电流A(调信号)
                            map.put("d36", "");
                            map.put("c37", "");//JBA短外电流A(调信号)
                            map.put("d37", "");
                        }
                        maps.put("quDuanYunYingName", quDuanBaseEntity.getQuduanyunyingName());
                        mapss.add(maps);
                    }
                }
            }
        }
        jo.put("data", mapss);
        return jo;
    }

    @Override
    public Map<String, Object> findByQidAndNV(Integer qid, String n, String v, String tableName) {
        return quDuanInfoDaoV2.selectByQidAndNV(qid, n, v, tableName);
    }


}
