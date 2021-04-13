package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongFileEntity;

public interface PaiGongGuanLiBaoGongFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongFileEntity record);

    PaiGongGuanLiBaoGongFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongFileEntity record);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongFileEntity record);
/////////////////////////////////////////////////////////////////////////
    int insertSelective(PaiGongGuanLiBaoGongFileEntity record);
}