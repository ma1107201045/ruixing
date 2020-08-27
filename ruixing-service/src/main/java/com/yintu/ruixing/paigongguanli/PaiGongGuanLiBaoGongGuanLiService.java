package com.yintu.ruixing.paigongguanli;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/27 15:50
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiBaoGongGuanLiService {
    List<PaiGongGuanLiBaoGongGuanLiEntity> findAllBaoGong(Integer page, Integer size);

    void addBaoGong(PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity);

    void editBaoGongById(PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity);

    List<PaiGongGuanLiBaoGongGuanLiEntity> findBaoGongBySome(Date daytime, String xiangMuName, String userName, Integer page, Integer size);

    PaiGongGuanLiBaoGongGuanLiEntity findById(Integer id);

    void deleteBaoGongByIds(Integer[] ids);
}
