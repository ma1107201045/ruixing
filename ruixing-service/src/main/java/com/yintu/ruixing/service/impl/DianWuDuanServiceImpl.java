package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.DianWuDuanDao;
import com.yintu.ruixing.entity.DianWuDuanEntity;
import com.yintu.ruixing.service.DianWuDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 电务段的serviceimpl
 */
@Service
@Transactional
public class DianWuDuanServiceImpl implements DianWuDuanService {

    @Autowired
    private DianWuDuanDao dianWuDuanDao;
    @Override
    public DianWuDuanEntity findDianWuDuanById(Long id) {
        return dianWuDuanDao.findDianWuDuanById(id);
    }

    @Override
    public void addDianWuDuan(DianWuDuanEntity dianWuDuanEntity) {
        dianWuDuanDao.addDianWuDuan(dianWuDuanEntity);
    }

    @Override
    public void editDianWuDuan(DianWuDuanEntity dianWuDuanEntity) {
        dianWuDuanDao.editDianWuDuan(dianWuDuanEntity);
    }

    @Override
    public void delDianWuDuan(Long id) {
        dianWuDuanDao.delDianWuDuan(id);
    }
}
