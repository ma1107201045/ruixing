package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface PaiGongGuanLiPaiGongDanRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    int insertSelective(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    PaiGongGuanLiPaiGongDanRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    int updateByPrimaryKey(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    List<PaiGongGuanLiPaiGongDanRecordMessageEntity> findRecordMessageByid(Integer id);
}