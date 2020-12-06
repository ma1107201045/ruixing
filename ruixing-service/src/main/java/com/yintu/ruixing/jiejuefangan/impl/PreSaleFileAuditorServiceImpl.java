package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.master.jiejuefangan.PreSaleFileAuditorDao;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/6 18:42
 */
@Service
@Transactional
public class PreSaleFileAuditorServiceImpl implements PreSaleFileAuditorService {
    @Autowired
    private PreSaleFileAuditorDao preSaleFileAuditorDao;

    @Override
    public void add(PreSaleFileAuditorEntity entity) {
        preSaleFileAuditorDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        preSaleFileAuditorDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(PreSaleFileAuditorEntity entity) {
        preSaleFileAuditorDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public PreSaleFileAuditorEntity findById(Integer id) {
        return preSaleFileAuditorDao.selectByPrimaryKey(id);
    }

    @Override
    public List<PreSaleFileAuditorEntity> findByPreSaleFileId(Integer preSaleFileId) {
        return preSaleFileAuditorDao.selectByPreSaleFileId(preSaleFileId);
    }

    @Override
    public List<PreSaleFileAuditorEntity> findByExample(Integer preSaleFileId, Integer auditorId, Integer sort, Short activate) {
        return preSaleFileAuditorDao.selectByExample(preSaleFileId, auditorId, sort, activate);
    }

    @Override
    public void removeByPreSaleFileId(Integer preSaleFileId) {
        preSaleFileAuditorDao.deleteByPreSaleFileId(preSaleFileId);
    }

    @Override
    public void addMuch(List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities) {
        preSaleFileAuditorDao.insertMuch(preSaleFileAuditorEntities);
    }
}
