package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanShenQingEntity;

import java.util.List;

public interface PaiGongGuanLiPaiGongDanShenQingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiPaiGongDanShenQingEntity record);

    PaiGongGuanLiPaiGongDanShenQingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiPaiGongDanShenQingEntity record);

    ///////////////////////////////////////////////////////////////////////////////
    int updateByPrimaryKeySelective(PaiGongGuanLiPaiGongDanShenQingEntity record);

    int insertSelective(PaiGongGuanLiPaiGongDanShenQingEntity record);

    List<PaiGongGuanLiPaiGongDanShenQingEntity> findShenQing(Integer paiGongId, Integer userid);
}