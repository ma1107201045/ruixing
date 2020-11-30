package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiCostEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;


public interface PaiGongGuanLiCostDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiCostEntity record);

    PaiGongGuanLiCostEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiCostEntity record);

    ///////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(PaiGongGuanLiCostEntity record);

    int insertSelective(PaiGongGuanLiCostEntity record);

    BigDecimal findCostTotal(Integer id);

    void deleteCostBycid(Integer id);

    List<PaiGongGuanLiCostEntity> findAllCostByCid(Integer id);

    BigDecimal findAllCost();

}