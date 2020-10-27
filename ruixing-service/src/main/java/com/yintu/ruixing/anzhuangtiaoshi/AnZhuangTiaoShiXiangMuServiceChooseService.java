package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:36
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiXiangMuServiceChooseService {
    List<AnZhuangTiaoShiXiangMuEntity> findAllByXDid( Integer xdid);

    AnZhuangTiaoShiXiangMuServiceStatusEntity findServiceStatusById(Integer id);

    void addXiangMu(AnZhuangTiaoShiXiangMuEntity xiangMuEntity);

    void addXiangMuServiceChooseEntity(AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity);


    JSONObject findAllByXDidddd(Integer xdid);
}
