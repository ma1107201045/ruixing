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

    void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username, Integer senderid);

    List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum);

    void editPaiGongDanById(Integer id, PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username);

    void deletePaiGongDanByIds(Integer[] ids);

    void doSomeThing(Integer receiverid,Integer senderid, Integer id, Integer isNotRefuse, String reason, String username);
}
