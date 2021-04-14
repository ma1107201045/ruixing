package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongSecondaryEntity;

import java.util.List;

public interface PaiGongGuanLiBaoGongSecondaryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongSecondaryEntity record);

    PaiGongGuanLiBaoGongSecondaryEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongSecondaryEntity record);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongSecondaryEntity record);
////////////////////////////////////////////////////////////////
    int insertSelective(PaiGongGuanLiBaoGongSecondaryEntity record);

    List<PaiGongGuanLiBaoGongSecondaryEntity> findBaoGongByBid(Integer bid);
}