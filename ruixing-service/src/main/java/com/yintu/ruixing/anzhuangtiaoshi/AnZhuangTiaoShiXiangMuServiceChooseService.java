package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:36
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiXiangMuServiceChooseService {

    void addXiangMuServiceChooseEntity(AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity);

    void addXiangMu(AnZhuangTiaoShiXiangMuEntity xiangMuEntity);

    AnZhuangTiaoShiXiangMuServiceStatusEntity findServiceStatusById(Integer id);

    JSONArray findStatusByCzId(Integer czId);

    void addXiangMuServiceChoose(JSONArray ja, String username, Integer senderid);

    void removeByCzId(Integer[] czIds);

    void editByCzId(Map<String, Object> cheZhanData, String username, Integer senderid);

    JSONObject findAllByXdId(Integer pageNumber, Integer pageSize, Integer xdId, String czName);


}
