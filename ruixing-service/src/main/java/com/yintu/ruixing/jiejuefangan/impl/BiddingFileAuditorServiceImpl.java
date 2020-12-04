package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.master.jiejuefangan.BiddingFileAuditorDao;
import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorService;
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


    @Override
    public List<BiddingFileAuditorEntity> findByBiddingFileIdId(Integer biddingFileId) {
        return biddingFileAuditorDao.selectByBiddingFileId(biddingFileId);
    }

    @Override
    public List<BiddingFileAuditorEntity> findByExample(Integer biddingFileId, Integer auditorId, Short isCheck) {
        return biddingFileAuditorDao.selectByExample(biddingFileId,auditorId,isCheck);
    }

    @Override
    public void addMuch(List<BiddingFileAuditorEntity> biddingFileAuditorEntities) {
        biddingFileAuditorDao.insertMuch(biddingFileAuditorEntities);
    }

    @Override
    public void removeByBiddingFileId(Integer biddingFileId) {
        biddingFileAuditorDao.deleteByBiddingFileId(biddingFileId);
    }
}
