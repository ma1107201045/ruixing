package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.DesignLiaisonFileAuditorDao;
import com.yintu.ruixing.entity.DesignLiaisonFileAuditorEntity;
import com.yintu.ruixing.service.DesignLiaisonFileAuditorService;
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
public class DesignLiaisonFileAuditorServiceImpl implements DesignLiaisonFileAuditorService {
    @Autowired
    private DesignLiaisonFileAuditorDao designLiaisonFileAuditorDao;

    @Override
    public void add(DesignLiaisonFileAuditorEntity entity) {
        designLiaisonFileAuditorDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        designLiaisonFileAuditorDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(DesignLiaisonFileAuditorEntity entity) {
        designLiaisonFileAuditorDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public DesignLiaisonFileAuditorEntity findById(Integer id) {
        return designLiaisonFileAuditorDao.selectByPrimaryKey(id);
    }

    @Override
    public List<DesignLiaisonFileAuditorEntity> findByDesignLiaisonFileId(Integer designLiaisonFileId) {
        return designLiaisonFileAuditorDao.selectByDesignLiaisonFileId(designLiaisonFileId);
    }

    @Override
    public void addMuch(List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities) {
        designLiaisonFileAuditorDao.insertMuch(designLiaisonFileAuditorEntities);
    }

    @Override
    public void removeByDesignLiaisonFileId(Integer designLiaisonFileId) {
        designLiaisonFileAuditorDao.deleteByDesignLiaisonFileId(designLiaisonFileId);
    }
}
