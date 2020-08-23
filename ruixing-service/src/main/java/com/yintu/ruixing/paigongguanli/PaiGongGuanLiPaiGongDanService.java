package com.yintu.ruixing.paigongguanli;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:32
 * @Version 1.0
 * 需求: 派工单
 */
public interface PaiGongGuanLiPaiGongDanService {

    String findPaiGongDanNum(String suoxie);

    void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity);

    List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum);
}
