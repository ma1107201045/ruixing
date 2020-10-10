package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiCostListEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaiGongGuanLiCostListDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiCostListEntity record);

    PaiGongGuanLiCostListEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiCostListEntity record);

    ////////////////////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(PaiGongGuanLiCostListEntity record);

    int insertSelective(PaiGongGuanLiCostListEntity record);

    List<PaiGongGuanLiCostListEntity> findAllCostList(String xmNumber);
}