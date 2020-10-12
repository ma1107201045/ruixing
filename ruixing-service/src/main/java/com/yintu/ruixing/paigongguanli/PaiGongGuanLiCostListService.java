package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/10/10 10:54
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiCostListService {
    void addCostList(PaiGongGuanLiCostListEntity costListEntity);

    void addCost(PaiGongGuanLiCostEntity costEntity);

    List<PaiGongGuanLiCostListEntity> findAllCostList(Integer page, Integer size, String xmNumber);

    void deitCostListById(PaiGongGuanLiCostListEntity paiGongGuanLiCostListEntity, String username);

    void deleteCostListByIds(Integer[] ids);

    List<PaiGongGuanLiCostEntity> findAllCostByCid(Integer id);

    void editCostById(PaiGongGuanLiCostEntity paiGongGuanLiCostEntity);

    void deleteCostByIds(Integer[] ids);

    List<ChanPinJiaoFuXiangMuEntity> findXmNumberAndName();

    List<PaiGongGuanLiCostListEntity> findDatasByUid(Integer page, Integer size, Integer uid);

    BigDecimal findAllCost();

    List<PaiGongGuanLiCostListEntity> findDatasByXMname(Integer page, Integer size, String xmName);

    List<PaiGongGuanLiCostListEntity> findDatasByYWtype(Integer page, Integer size, String ywType);
}
