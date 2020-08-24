package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:11
 * @Version 1.0
 * 需求: 派工管理  日勤
 */
@Service
@Transactional
public class PaiGongGuanLiRiQinServiceImpl implements PaiGongGuanLiRiQinService {
    @Autowired
    private PaiGongGuanLiRiQinDao paiGongGuanLiRiQinDao;

    @Override
    public List<PaiGongGuanLiRiQinEntity> findAllRiQin(Integer page, Integer size) {
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinDao.findAllRiQin();
        for (PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity : riQinEntityList) {
            Integer uid = paiGongGuanLiRiQinEntity.getUid();
        }

        return null;
    }
}
