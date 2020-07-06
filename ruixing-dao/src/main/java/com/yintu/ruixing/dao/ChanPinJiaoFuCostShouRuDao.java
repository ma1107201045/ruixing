package com.yintu.ruixing.dao;

import com.yintu.ruixing.entity.ChanPinJiaoFuCostShouRuEntity;

import java.util.List;

public interface ChanPinJiaoFuCostShouRuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuCostShouRuEntity record);

    int insertSelective(ChanPinJiaoFuCostShouRuEntity record);

    ChanPinJiaoFuCostShouRuEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuCostShouRuEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuCostShouRuEntity record);

    ///////////////////////////////////////////////////////

    List<ChanPinJiaoFuCostShouRuEntity> findAllShouRuCost();

    void deletShouRuCostByIds(Integer[] ids);

    List<ChanPinJiaoFuCostShouRuEntity> findShouRuCostByName(String xiangMuName);

    void editShouRuCost(ChanPinJiaoFuCostShouRuEntity chanPinJiaoFuCostShouRuEntity);

    void addShouRuCost(ChanPinJiaoFuCostShouRuEntity chanPinJiaoFuCostShouRuEntity);


    List<ChanPinJiaoFuCostShouRuEntity> selectByCondition( Integer[] ids);
}