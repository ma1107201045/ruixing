package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;


import java.util.List;

public interface ChanPinJiaoFuCostDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuCostEntity record);

    ChanPinJiaoFuCostEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ChanPinJiaoFuCostEntity record);


    //////////////////////////////////////////////////////////////////
    int updateByPrimaryKeySelective(ChanPinJiaoFuCostEntity record);

    int insertSelective(ChanPinJiaoFuCostEntity record);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMu();

    List<ChanPinJiaoFuEntity> findCost(String xmName);

    void deleteByIds(Integer[] ids);
}