package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongGuanLiEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


public interface PaiGongGuanLiBaoGongGuanLiDao {
    int insert(PaiGongGuanLiBaoGongGuanLiEntity record);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongGuanLiEntity record);



    ///////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    PaiGongGuanLiBaoGongGuanLiEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongGuanLiEntity record);

    int insertSelective(PaiGongGuanLiBaoGongGuanLiEntity record);

    List<PaiGongGuanLiBaoGongGuanLiEntity> findAllBaoGong();

    List<PaiGongGuanLiBaoGongGuanLiEntity> findBaoGongByThreeSome(Date daytime, Integer uId, String xiangMuName);

    List<PaiGongGuanLiBaoGongGuanLiEntity> findBaoGongByTwoSome(Date daytime, String xiangMuName);
}