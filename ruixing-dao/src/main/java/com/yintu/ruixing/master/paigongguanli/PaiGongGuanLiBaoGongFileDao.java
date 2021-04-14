package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongFileEntity;

import java.util.List;

public interface PaiGongGuanLiBaoGongFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongFileEntity record);

    PaiGongGuanLiBaoGongFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongFileEntity record);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongFileEntity record);
/////////////////////////////////////////////////////////////////////////
    int insertSelective(PaiGongGuanLiBaoGongFileEntity record);

    List<PaiGongGuanLiBaoGongFileEntity> findFileByBid(Integer bid, Integer baoGongType);
}