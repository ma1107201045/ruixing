package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.BaoJingYuJingDao;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingEntity;
import com.yintu.ruixing.guzhangzhenduan.BaojingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:mlf
 * @date:2020/6/1 11:28
 */
@Service
@Transactional
public class BaoJingServiceImpl implements BaojingService {
    @Autowired
    private BaoJingYuJingDao baojingDaoJingYu;

    @Override
    public void add(BaoJingYuJingEntity baoJingYuJingEntity) {
        baojingDaoJingYu.insertSelective(baoJingYuJingEntity);
    }

    @Override
    public void remove(Integer id) {
        baojingDaoJingYu.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(BaoJingYuJingEntity baoJingYuJingEntity) {
        baojingDaoJingYu.updateByPrimaryKeySelective(baoJingYuJingEntity);
    }

    @Override
    public BaoJingYuJingEntity findById(Integer id) {
        return baojingDaoJingYu.selectByPrimaryKey(id);
    }
}
