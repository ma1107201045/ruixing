package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.DataPublicSwitchEntity;
import com.yintu.ruixing.guzhangzhenduan.DataPublicSwitchService;
import com.yintu.ruixing.slave.guzhangzhenduan.DataPublicSwitchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/13 16:07
 * @Version: 1.0
 */
@Service
public class DataPublicSwitchServiceImpl implements DataPublicSwitchService {
    @Autowired
    private DataPublicSwitchDao dataPublicSwitchDao;

    @Override
    public boolean isTableExist(String tableName) {
        return dataPublicSwitchDao.isTableExist(tableName) > 0;
    }

    public DataPublicSwitchEntity findNewFirstData(Integer czId) {
        String tableName = StringUtil.getTableNames(czId, new Date());
        return this.isTableExist(tableName) ? dataPublicSwitchDao.selectNewFirstData(tableName) : new DataPublicSwitchEntity();
    }

    @Override
    public List<JSONObject> findByCondition(Integer czId) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        DataPublicSwitchEntity dataPublicSwitchEntity = this.findNewFirstData(czId);
        Class<?> clazz = dataPublicSwitchEntity.getClass();
        String methodPrefix = "getPs";
        for (int i = 0; i < 16; i++) {
            JSONObject jo = new JSONObject();
            try {
                Method m1 = clazz.getDeclaredMethod(methodPrefix + (i + 1));
                Object v1 = m1.invoke(dataPublicSwitchEntity);
                jo.put("GJ", get(v1));

                Method m2 = clazz.getDeclaredMethod(methodPrefix + (i + 1 + 16));
                Object v2 = m2.invoke(dataPublicSwitchEntity);
                jo.put("DJ", get(v2));

                Method m3 = clazz.getDeclaredMethod(methodPrefix + (i + 1 + 16 * 2));
                Object v3 = m3.invoke(dataPublicSwitchEntity);
                jo.put("DJS", get(v3));
                jsonObjects.add(jo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObjects;
    }

    public String get(Object v1) {
        return v1 == null ? "" : "1".equals(v1.toString()) ? "吸起" : "2".equals(v1.toString()) ? "落下" : "无效";
    }


}
