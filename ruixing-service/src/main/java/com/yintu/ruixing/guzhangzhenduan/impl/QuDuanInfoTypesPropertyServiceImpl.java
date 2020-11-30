package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.QuDuanInfoTypesPropertyDao;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoTypesPropertyEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoTypesPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/4 18:55
 */
@Service
@Transactional
public class QuDuanInfoTypesPropertyServiceImpl implements QuDuanInfoTypesPropertyService {
    @Autowired
    private QuDuanInfoTypesPropertyDao quDuanInfoTypesPropertyDao;

    @Override
    public void add(QuDuanInfoTypesPropertyEntity entity) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void edit(QuDuanInfoTypesPropertyEntity entity) {

    }

    @Override
    public QuDuanInfoTypesPropertyEntity findById(Integer id) {
        return null;
    }

    @Override
    public List<QuDuanInfoTypesPropertyEntity> connectFindByCondition(String types) {
        return quDuanInfoTypesPropertyDao.connectSelectByCondition(types);
    }

    @Override
    public String countByType(List<Integer> types) {
        return quDuanInfoTypesPropertyDao.countByType(types);
    }
}
