package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuXiangMuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiCostDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiCostListDao;
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
    public List<PaiGongGuanLiCostListEntity> findDatasByYWtype(Integer page, Integer size, String ywType) {
        List<PaiGongGuanLiCostListEntity> costListEntityList = paiGongGuanLiCostListDao.findDatasByYWtype(ywType);
        for (PaiGongGuanLiCostListEntity costListEntity : costListEntityList) {
            Integer id = costListEntity.getId();
            BigDecimal costTotal = paiGongGuanLiCostDao.findCostTotal(id);
            if (costTotal == null) {
                BigDecimal num = new BigDecimal(0);
                costListEntity.setCostTotal(num);
            } else {
                costListEntity.setCostTotal(costTotal);
            }
        }
        return costListEntityList;
    }

    @Override
    public List<PaiGongGuanLiCostListEntity> findDatasByXMname(Integer page, Integer size, String xmName) {
        List<PaiGongGuanLiCostListEntity> costListEntityList = paiGongGuanLiCostListDao.findDatasByXMname(xmName);
        for (PaiGongGuanLiCostListEntity costListEntity : costListEntityList) {
            Integer id = costListEntity.getId();
            BigDecimal costTotal = paiGongGuanLiCostDao.findCostTotal(id);
            if (costTotal == null) {
                BigDecimal num = new BigDecimal(0);
                costListEntity.setCostTotal(num);
            } else {
                costListEntity.setCostTotal(costTotal);
            }
        }
        return costListEntityList;
    }

    @Override
    public BigDecimal findAllCost() {
        return paiGongGuanLiCostDao.findAllCost();
    }

    @Override
    public List<PaiGongGuanLiCostListEntity> findDatasByUid(Integer page, Integer size, Integer uid) {
        List<PaiGongGuanLiCostListEntity> costListEntityList = paiGongGuanLiCostListDao.findDatasByUid(uid);
        for (PaiGongGuanLiCostListEntity costListEntity : costListEntityList) {
            Integer id = costListEntity.getId();
            BigDecimal costTotal = paiGongGuanLiCostDao.findCostTotal(id);
            if (costTotal == null) {
                BigDecimal num = new BigDecimal(0);
                costListEntity.setCostTotal(num);
            } else {
                costListEntity.setCostTotal(costTotal);
            }
        }
        return costListEntityList;
    }

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
        List<PaiGongGuanLiCostListEntity> costListEntityList = paiGongGuanLiCostListDao.findAllCostList(xmNumber);
        for (PaiGongGuanLiCostListEntity costListEntity : costListEntityList) {
            Integer id = costListEntity.getId();
            BigDecimal costTotal = paiGongGuanLiCostDao.findCostTotal(id);
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
