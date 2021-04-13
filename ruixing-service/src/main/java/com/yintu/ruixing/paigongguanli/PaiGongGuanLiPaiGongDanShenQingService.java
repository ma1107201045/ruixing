package com.yintu.ruixing.paigongguanli;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/12 14:15
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiPaiGongDanShenQingService {
    void addShenQind(PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity,String username);

    List<PaiGongGuanLiPaiGongDanShenQingEntity> findShenQing(Integer paiGongId, Integer userid);

    void editShenQingById(PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity,String username);

}
