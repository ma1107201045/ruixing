package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:10
 * @Version 1.0
 * 需求: 派工管理 日勤
 */
public interface PaiGongGuanLiRiQinService {
    List<PaiGongGuanLiRiQinEntity> findAllRiQin(Integer page, Integer size);

    List<PaiGongGuanLiRiQinEntity> findAllRiQinByUserName(Integer page, Integer size, String username);

    void addRiQin(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity,Integer senderid);

    void editRiQinById(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity);

    List<PaiGongGuanLiRiQinEntity> findAllRiQinByUid(Integer uid);

    List<PaiGongGuanLiRiQinEntity> findAllRiQinDatas(Integer page, Integer size);

    JSONObject findAllPeopleRiQinDatas();

}
