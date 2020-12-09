package com.yintu.ruixing.chanpinjiaofu;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/12/9 15:24
 * @Version 1.0
 * 需求:产品交付费用
 */
public interface ChanPinJiaoFuCostService {
    List<ChanPinJiaoFuXiangMuEntity> findXiangMu();

    void addCost(ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity,String username);

    void editCostById(ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity,String username);

    List<ChanPinJiaoFuEntity> findCost(String xmName, Integer page, Integer size);

    void deleteByIds(Integer[] ids);
}
