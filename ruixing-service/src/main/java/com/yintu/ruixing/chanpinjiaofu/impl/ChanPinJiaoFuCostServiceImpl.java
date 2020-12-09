package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostService;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuCostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/12/9 15:25
 * @Version 1.0
 * 需求:产品交付费用
 */
@Service
@Transactional
public class ChanPinJiaoFuCostServiceImpl implements ChanPinJiaoFuCostService {
    @Autowired
    private ChanPinJiaoFuCostDao chanPinJiaoFuCostDao;

    @Override
    public void deleteByIds(Integer[] ids) {
        chanPinJiaoFuCostDao.deleteByIds(ids);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findCost(String xmName, Integer page, Integer size) {
        return chanPinJiaoFuCostDao.findCost(xmName);
    }

    @Override
    public void editCostById(ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity,String username) {
        Date date=new Date();
        chanPinJiaoFuCostEntity.setUpdateName(username);
        chanPinJiaoFuCostEntity.setUpdateTime(date);
        chanPinJiaoFuCostDao.updateByPrimaryKeySelective(chanPinJiaoFuCostEntity);
    }

    @Override
    public void addCost(ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity,String username) {
        Date date=new Date();
        chanPinJiaoFuCostEntity.setCreateName(username);
        chanPinJiaoFuCostEntity.setCreateTime(date);
        chanPinJiaoFuCostDao.insertSelective(chanPinJiaoFuCostEntity);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMu() {
         return chanPinJiaoFuCostDao.findXiangMu();
    }
}
