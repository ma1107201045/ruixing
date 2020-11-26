package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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


    @Override
    public boolean isTableExist(String tableName) {
        return quDuanInfoDaoV2.isTableExist(tableName) > 0;
    }

    @Override
    public QuDuanInfoEntityV2 findLastBycZId(Integer czId) {
        String tableName = StringUtil.getTableName(czId, new Date());
        if (this.isTableExist(tableName)) {
            QuDuanInfoEntityV2 quDuanInfoEntityV2 = quDuanInfoDaoV2.selectLastByCzId(czId, tableName);
            quDuanInfoEntityV2.setQuDuanBaseEntity(quDuanBaseService.findByCzIdAndQdId(czId, quDuanInfoEntityV2.getQid(), null).get(0));
            return quDuanInfoEntityV2;
        }
        return new QuDuanInfoEntityV2();
    }


    @Override
    public QuDuanInfoEntityV2 findFirstByCzId1(Integer czId, Integer qid) {
        String tableName = StringUtil.getTableName(czId, new Date());
        return this.isTableExist(tableName) ? quDuanInfoDaoV2.selectFirstByCzId1(czId, qid, tableName) : new QuDuanInfoEntityV2();
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


    @Override
    public List<JSONObject> findByCondition(Integer czId, Date startTime, Date endTime) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (startTime == null || endTime == null) {
            Boolean czStutrs = cheZhanService.findCzStutrs(Long.parseLong(czId.toString()), false);
            if (czStutrs) {
                List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, false);
                for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
                    QuDuanInfoEntityV2 quDuanInfoEntityV2 = this.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
                    if (quDuanInfoEntityV2 == null) {
                        jsonObjects.add(null);
                        continue;
                    }
                    JSONObject jo = this.convert(quDuanInfoPropertyEntities, quDuanInfoEntityV2, false);
                    jsonObjects.add(jo);
                }
            }
        } else {
            Integer[] qids = quDuanBaseService.findByCzIdAndQdId(czId, null, false)
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


    public JSONObject findNullProperties(Integer czId) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = this.findPropertiesByCzId(czId);
        return convert(quDuanInfoPropertyEntities, new QuDuanInfoEntityV2(), true);
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
                    jsonObject.put("v", quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("c", 1);
                    break;
                case 4:
                    jsonObject.put("v", quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    jsonObject.put("c", 1);
                    break;
                case 5:
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei());
                    jsonObject.put("v", jsonArray5);
                    jsonObject.put("c", 2);
                    break;
                case 6:
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jsonObject.put("v", jsonArray6);
                    jsonObject.put("c", 2);
                    break;
                case 7:
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jsonObject.put("v", jsonArray7);
                    jsonObject.put("c", 2);
                    break;
                case 8:
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jsonObject.put("v", jsonArray8);
                    jsonObject.put("c", 2);
                    break;
                case 9:
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jsonObject.put("v", jsonArray9);
                    jsonObject.put("c", 2);
                    break;
                case 10:
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jsonObject.put("v", jsonArray10);
                    jsonObject.put("c", 2);
                    break;
                case 11:
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jsonObject.put("v", jsonArray11);
                    jsonObject.put("c", 2);
                    break;
                case 12:
                    jsonObject.put("v", quDuanInfoEntityV2.getVSongduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 13:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaSongduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 14:
                    jsonObject.put("v", quDuanInfoEntityV2.getVShouduanCableHost());
                    jsonObject.put("c", 3);
                    break;
                case 15:
                    jsonObject.put("v", quDuanInfoEntityV2.getVShouduanCableSpare());
                    jsonObject.put("c", 3);
                    break;
                case 16:
                    jsonObject.put("v", quDuanInfoEntityV2.getMaShouduanCable());
                    jsonObject.put("c", 3);
                    break;
                case 17:
                    jsonObject.put("v", quDuanInfoEntityV2.getVInAll());
                    jsonObject.put("c", 4);
                    break;
                case 18:
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jsonObject.put("v", jsonArray18);
                    jsonObject.put("c", 4);
                    break;
                case 19:
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jsonObject.put("v", jsonArray19);
                    jsonObject.put("c", 4);
                    break;
                case 20:
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jsonObject.put("v", jsonArray20);
                    jsonObject.put("c", 4);
                    break;
                case 21:
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jsonObject.put("v", jsonArray21);
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
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
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
     * 实时 报表
     *
     * @param properties 属性集合
     * @param czId       车站id
     * @return
     */
    @Override
    public JSONObject realTimeReport(Integer czId, Integer[] properties) {
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
        jo.put("title", quDuanInfoPropertyEntities);

        //表头对应数据数组
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, null);
        JSONArray dataJa = new JSONArray();
        Boolean czStutrs = cheZhanService.findCzStutrs(Long.parseLong(czId.toString()), false);
        if (czStutrs) {
            for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
                QuDuanInfoEntityV2 quDuanInfoEntityV2 = this.findFirstByCzId1(czId, quDuanBaseEntity.getQdid());
                if (quDuanInfoEntityV2 == null) {
                    quDuanInfoEntityV2 = new QuDuanInfoEntityV2();
                }
                quDuanInfoEntityV2.setQuDuanBaseEntity(quDuanBaseEntity);
                JSONObject jsonObject = this.findDate(quDuanInfoPropertyEntities, quDuanInfoEntityV2);
                dataJa.add(jsonObject);
            }
        }
        jo.put("data", dataJa);

        return jo;
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
            String idStr = String.valueOf(quDuanInfoPropertyEntity.getId());
            switch (idStr) {
                case "1":
                    jo.put(idStr, quDuanInfoEntityV2.getDesignCarrier());
                    break;
                case "2":
                    jo.put(idStr, quDuanInfoEntityV2.getDirection() == null ? null : quDuanInfoEntityV2.getDirection().equals(1) ? "正向" : quDuanInfoEntityV2.getDirection().equals(2) ? "反向" : "无效");
                    break;
                case "3":
                    jo.put(idStr, quDuanInfoEntityV2.getGjcollection() == null ? null : quDuanInfoEntityV2.getGjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getGjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "4":
                    jo.put(idStr, quDuanInfoEntityV2.getDjcollection() == null ? null : quDuanInfoEntityV2.getDjcollection().equals("2") ? "落下" : quDuanInfoEntityV2.getDjcollection().equals("1") ? "吸起" : "无效");
                    break;
                case "5":
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.add(quDuanInfoEntityV2.getVOutZhu());
                    jsonArray5.add(quDuanInfoEntityV2.getVOutBei().toString());
                    jo.put(idStr, jsonArray5.get(0) + "/" + jsonArray5.get(1));
                    break;
                case "6":
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutZhu());
                    jsonArray6.add(quDuanInfoEntityV2.getMaOutBei());
                    jo.put(idStr, jsonArray6.get(0) + "/" + jsonArray6.get(1));
                    break;
                case "7":
                    JSONArray jsonArray7 = new JSONArray();
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpZhu());
                    jsonArray7.add(quDuanInfoEntityV2.getHzUpBei());
                    jo.put(idStr, jsonArray7.get(0) + "/" + jsonArray7.get(1));
                    break;
                case "8":
                    JSONArray jsonArray8 = new JSONArray();
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownZhu());
                    jsonArray8.add(quDuanInfoEntityV2.getHzDownBei());
                    jo.put(idStr, jsonArray8.get(0) + "/" + jsonArray8.get(1));
                    break;
                case "9":
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowZhu());
                    jsonArray9.add(quDuanInfoEntityV2.getHzLowBei());
                    jo.put(idStr, jsonArray9.get(0) + "/" + jsonArray9.get(1));
                    break;
                case "10":
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveZhu() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray10.add(quDuanInfoEntityV2.getFbjDriveBei() == null ? null : quDuanInfoEntityV2.getFbjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getFbjDriveBei() == 2 ? "无" : "无效");
                    jo.put(idStr, jsonArray10.get(0) + "/" + jsonArray10.get(1));
                    break;
                case "11":
                    JSONArray jsonArray11 = new JSONArray();
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionZhu() == null ? null : quDuanInfoEntityV2.getFbjCollectionZhu().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray11.add(quDuanInfoEntityV2.getFbjCollectionBei() == null ? null : quDuanInfoEntityV2.getFbjCollectionBei().equals("2") ? "落下" : quDuanInfoEntityV2.getFbjCollectionBei().equals("1") ? "吸起" : "无效");
                    jo.put(idStr, jsonArray11.get(0) + "/" + jsonArray11.get(1));
                    break;
                case "12":
                    jo.put(idStr, quDuanInfoEntityV2.getVSongduanCable());
                    break;
                case "13":
                    jo.put(idStr, quDuanInfoEntityV2.getMaSongduanCable());
                    break;
                case "14":
                    jo.put(idStr, quDuanInfoEntityV2.getVShouduanCableHost());
                    break;
                case "15":
                    jo.put(idStr, quDuanInfoEntityV2.getVShouduanCableSpare());
                    break;
                case "16":
                    jo.put(idStr, quDuanInfoEntityV2.getMaShouduanCable());
                    break;
                case "17":
                    jo.put(idStr, quDuanInfoEntityV2.getVInAll());
                    break;
                case "18":
                    JSONArray jsonArray18 = new JSONArray();
                    jsonArray18.add(quDuanInfoEntityV2.getMvInZhu());
                    jsonArray18.add(quDuanInfoEntityV2.getMvInBing());
                    jo.put(idStr, jsonArray18.get(0) + "/" + jsonArray18.get(1));
                    break;
                case "19":
                    JSONArray jsonArray19 = new JSONArray();
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoZhu());
                    jsonArray19.add(quDuanInfoEntityV2.getMvInDiaoBing());
                    jo.put(idStr, jsonArray19.get(0) + "/" + jsonArray19.get(1));
                    break;
                case "20":
                    JSONArray jsonArray20 = new JSONArray();
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowZhu());
                    jsonArray20.add(quDuanInfoEntityV2.getHzInLowBing());
                    jo.put(idStr, jsonArray20.get(0) + "/" + jsonArray20.get(1));
                    break;
                case "21":
                    JSONArray jsonArray21 = new JSONArray();
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveZhu() == null ? null : quDuanInfoEntityV2.getGjDriveZhu() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveZhu() == 2 ? "无" : "无效");
                    jsonArray21.add(quDuanInfoEntityV2.getGjDriveBing() == null ? null : quDuanInfoEntityV2.getGjDriveBing() == 1 ? "正常" : quDuanInfoEntityV2.getGjDriveBing() == 2 ? "无" : "无效");
                    jo.put(idStr, jsonArray21.get(0) + "/" + jsonArray21.get(1));
                    break;
                case "22":
                    JSONArray jsonArray22 = new JSONArray();
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionZhu() == null ? null : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("2") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionZhu().equals("1") ? "吸起" : "无效");
                    jsonArray22.add(quDuanInfoEntityV2.getGjRearCollectionBing() == null ? null : quDuanInfoEntityV2.getGjRearCollectionBing().equals("2") ? "落下" : quDuanInfoEntityV2.getGjRearCollectionBing().equals("1") ? "吸起" : "无效");
                    jo.put(idStr, jsonArray22.get(0) + "/" + jsonArray22.get(1));
                    break;
                case "23":
                    JSONArray jsonArray23 = new JSONArray();
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingZhu() ? null : quDuanInfoEntityV2.getBaojingZhu() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jsonArray23.add(null == quDuanInfoEntityV2.getBaojingBing() ? null : quDuanInfoEntityV2.getBaojingBing() == 1 ? "正常" : quDuanInfoEntityV2.getBaojingZhu() == 2 ? "报警" : "无效");
                    jo.put(idStr, jsonArray23.get(0) + "/" + jsonArray23.get(1));
                    break;

                case "24":
                    jo.put(idStr, quDuanInfoEntityV2.getMaCableFbp());
                    break;
                case "25":
                    jo.put(idStr, quDuanInfoEntityV2.getALonginFbp());
                    break;
                case "26":
                    jo.put(idStr, quDuanInfoEntityV2.getALongoutFbp());
                    break;
                case "27":
                    jo.put(idStr, quDuanInfoEntityV2.getAShortinFbp());
                    break;
                case "28":
                    jo.put(idStr, quDuanInfoEntityV2.getAShortoutFbp());
                    break;
                case "29":
                    jo.put(idStr, quDuanInfoEntityV2.getTFbp());
                    break;

                case "30":
                    JSONArray jsonArray30 = new JSONArray();
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaZhu());
                    jsonArray30.add(quDuanInfoEntityV2.getALonginFbaDiao());
                    jo.put(idStr, jsonArray30.get(0) + "/" + jsonArray30.get(1));
                    break;
                case "31":
                    JSONArray jsonArray31 = new JSONArray();
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaZhu());
                    jsonArray31.add(quDuanInfoEntityV2.getALongoutFbaDiao());
                    jo.put(idStr, jsonArray31.get(0) + "/" + jsonArray31.get(1));
                    break;
                case "32":
                    JSONArray jsonArray32 = new JSONArray();
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaZhu());
                    jsonArray32.add(quDuanInfoEntityV2.getAShortinFbaDiao());
                    jo.put(idStr, jsonArray32.get(0) + "/" + jsonArray32.get(1));
                    break;
                case "33":
                    JSONArray jsonArray33 = new JSONArray();
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaZhu());
                    jsonArray33.add(quDuanInfoEntityV2.getAShortoutFbaDiao());
                    jo.put(idStr, jsonArray33.get(0) + "/" + jsonArray33.get(1));
                    break;
                case "34":
                    JSONArray jsonArray34 = new JSONArray();
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaZhu());
                    jsonArray34.add(quDuanInfoEntityV2.getALonginJbaDiao());
                    jo.put(idStr, jsonArray34.get(0) + "/" + jsonArray34.get(1));
                    break;
                case "35":
                    JSONArray jsonArray35 = new JSONArray();
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaZhu());
                    jsonArray35.add(quDuanInfoEntityV2.getALongoutJbaDiao());
                    jo.put(idStr, jsonArray35.get(0) + "/" + jsonArray35.get(1));
                    break;
                case "36":
                    JSONArray jsonArray36 = new JSONArray();
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaZhu());
                    jsonArray36.add(quDuanInfoEntityV2.getAShortinJbaDiao());
                    jo.put(idStr, jsonArray36.get(0) + "/" + jsonArray36.get(1));
                    break;
                case "37":
                    JSONArray jsonArray37 = new JSONArray();
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jsonArray37.add(quDuanInfoEntityV2.getAShortoutJbaZhu());
                    jo.put(idStr, jsonArray37.get(0) + "/" + jsonArray37.get(1));
                    break;
                case "38":
                    jo.put(idStr, quDuanInfoEntityV2.getMaCableJbp());
                    break;
                case "39":
                    jo.put(idStr, quDuanInfoEntityV2.getALonginJbp());
                    break;
                case "40":
                    jo.put(idStr, quDuanInfoEntityV2.getALongoutJbp());
                    break;
                case "41":
                    jo.put(idStr, quDuanInfoEntityV2.getAShortinJbp());
                    break;
                case "42":
                    jo.put(idStr, quDuanInfoEntityV2.getAShortoutJbp());
                    break;
                case "43":
                    jo.put(idStr, quDuanInfoEntityV2.getTJbp());
                    break;
            }
        }
        return jo;
    }


    @Override
    public List<Map<String, Object>> findStatisticsByCzIdAndTime(Integer czId, Date time) {
        String tableName = StringUtil.getTableName(czId, time);
        if (this.isTableExist(tableName)) {
            List<Map<String, Object>> maps = quDuanInfoDaoV2.selectStatisticsByCzIdAndTime(czId, (int) (DateUtil.beginOfDay(time).getTime() / 1000), (int) (DateUtil.endOfDay(time).getTime() / 1000), tableName);
            for (Map<String, Object> map : maps) {
                map.put("quDuanYunYingName", quDuanBaseService.findByCzIdAndQdId(czId, (Integer) map.get("v1"), null).get(0).getQuduanyunyingName());
            }
            return maps;
        }
        return new ArrayList<>();
    }

}
