package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostZhiChuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ChanPinJiaoFuCostZhiChuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuCostZhiChuEntity record);

    int insertSelective(ChanPinJiaoFuCostZhiChuEntity record);

    ChanPinJiaoFuCostZhiChuEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuCostZhiChuEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuCostZhiChuEntity record);

    /////////////////////////////////////////////////

    List<ChanPinJiaoFuXiangMuEntity> findXiangMu();

    void addZhiChuCost(ChanPinJiaoFuCostZhiChuEntity chanPinJiaoFuCostZhiChuEntity);

    void editZhiChuCost(ChanPinJiaoFuCostZhiChuEntity chanPinJiaoFuCostZhiChuEntity);

    List<ChanPinJiaoFuCostZhiChuEntity> findZhiChuCostByName(String xiangMuName);

    void deletZhiChuCostByIds(Integer[] ids);

    List<ChanPinJiaoFuCostZhiChuEntity> findAllZhiChuCost();

    List<ChanPinJiaoFuCostZhiChuEntity> selectByCondition(Integer[] ids);
}