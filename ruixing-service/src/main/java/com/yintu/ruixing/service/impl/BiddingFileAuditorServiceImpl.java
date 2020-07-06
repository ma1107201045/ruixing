package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.BiddingFileAuditorDao;
import com.yintu.ruixing.entity.BiddingFileAuditorEntity;
import com.yintu.ruixing.service.BiddingFileAuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:mlf
 * @date:2020/7/6 18:42
 */
@Service
@Transactional
public class BiddingFileAuditorServiceImpl implements BiddingFileAuditorService {
    @Autowired
    private BiddingFileAuditorDao biddingFileAuditorDao;

    @Override
    public void add(BiddingFileAuditorEntity entity) {
        biddingFileAuditorDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        biddingFileAuditorDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(BiddingFileAuditorEntity entity) {
        biddingFileAuditorDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public BiddingFileAuditorEntity findById(Integer id) {
        return biddingFileAuditorDao.selectByPrimaryKey(id);
    }
}
