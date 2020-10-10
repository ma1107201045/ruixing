package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.paigongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/10/10 10:54
 * @Version 1.0
 * 需求:费用管理
 */
@Service
@Transactional
public class PaiGongGuanLiCostListServiceImpl implements PaiGongGuanLiCostListService {
    @Autowired
    private PaiGongGuanLiCostListDao paiGongGuanLiCostListDao;

    @Autowired
    private PaiGongGuanLiCostDao paiGongGuanLiCostDao;

    @Autowired
    private ChanPinJiaoFuXiangMuDao chanPinJiaoFuXiangMuDao;

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXmNumberAndName() {
        return chanPinJiaoFuXiangMuDao.findXmNumberAndName();
    }

    @Override
    public void deleteCostByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiCostDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editCostById(PaiGongGuanLiCostEntity paiGongGuanLiCostEntity) {
        paiGongGuanLiCostDao.updateByPrimaryKeySelective(paiGongGuanLiCostEntity);
    }

    @Override
    public List<PaiGongGuanLiCostEntity> findAllCostByCid(Integer id) {
        return paiGongGuanLiCostDao.findAllCostByCid(id);
    }

    @Override
    public void deleteCostListByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiCostListDao.deleteByPrimaryKey(id);
            paiGongGuanLiCostDao.deleteCostBycid(id);
        }
    }

    @Override
    public void deitCostListById(PaiGongGuanLiCostListEntity paiGongGuanLiCostListEntity, String username) {
        paiGongGuanLiCostListEntity.setUpdatename(username);
        paiGongGuanLiCostListEntity.setUpdatetime(new Date());
        paiGongGuanLiCostListDao.updateByPrimaryKeySelective(paiGongGuanLiCostListEntity);
    }

    @Override
    public List<PaiGongGuanLiCostListEntity> findAllCostList(Integer page, Integer size, String xmNumber) {
        List<PaiGongGuanLiCostListEntity> costListEntityList=paiGongGuanLiCostListDao.findAllCostList(xmNumber);
        for (PaiGongGuanLiCostListEntity costListEntity : costListEntityList) {
            Integer id = costListEntity.getId();
            BigDecimal costTotal=paiGongGuanLiCostDao.findCostTotal(id);
            costListEntity.setCostTotal(costTotal);
        }
        return costListEntityList;
    }

    @Override
    public void addCost(PaiGongGuanLiCostEntity costEntity) {
        paiGongGuanLiCostDao.insertSelective(costEntity);
    }

    @Override
    public void addCostList(PaiGongGuanLiCostListEntity costListEntity) {
        paiGongGuanLiCostListDao.insertSelective(costListEntity);
    }
}
