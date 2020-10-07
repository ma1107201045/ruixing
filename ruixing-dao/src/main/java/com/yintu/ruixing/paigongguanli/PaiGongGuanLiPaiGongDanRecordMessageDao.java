package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaiGongGuanLiPaiGongDanRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    int insertSelective(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    PaiGongGuanLiPaiGongDanRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiPaiGongDanRecordMessageEntity record);

    int updateByPrimaryKey(PaiGongGuanLiPaiGongDanRecordMessageEntity record);
}