package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/10 13:47
 */
@Service
@Transactional
public class RemoteSupportTicketPushServiceImpl implements RemoteSupportTicketPushService {
    @Autowired
    private RemoteSupportTicketPushDao remoteSupportTicketPushDao;
    @Autowired
    private RemoteSupportTicketPushUserService remoteSupportTicketPushUserService;

    @Override
    public void add(RemoteSupportTicketPushEntity entity) {
        remoteSupportTicketPushDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        remoteSupportTicketPushDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(RemoteSupportTicketPushEntity entity) {
        remoteSupportTicketPushDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public RemoteSupportTicketPushEntity findById(Integer id) {
        return remoteSupportTicketPushDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(RemoteSupportTicketPushEntity remoteSupportTicketPushEntity, Integer[] userIds) {
        this.add(remoteSupportTicketPushEntity);
        for (Integer userId : userIds) {
            RemoteSupportTicketPushUserEntity remoteSupportTicketPushUserEntity = new RemoteSupportTicketPushUserEntity();
            remoteSupportTicketPushUserEntity.setCreateBy(remoteSupportTicketPushEntity.getCreateBy());
            remoteSupportTicketPushUserEntity.setCreateTime(remoteSupportTicketPushEntity.getCreateTime());
            remoteSupportTicketPushUserEntity.setModifiedBy(remoteSupportTicketPushEntity.getModifiedBy());
            remoteSupportTicketPushUserEntity.setModifiedTime(remoteSupportTicketPushEntity.getModifiedTime());
            remoteSupportTicketPushUserEntity.setPushId(remoteSupportTicketPushEntity.getId());
            remoteSupportTicketPushUserEntity.setUserId(userId);
            remoteSupportTicketPushUserService.add(remoteSupportTicketPushUserEntity);
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
            remoteSupportTicketPushUserService.deleteByPushId(id);
        }
    }

    @Override
    public List<RemoteSupportTicketPushEntity> findByCondition(Integer[] ids, String operator) {
        return remoteSupportTicketPushDao.selectByCondition(ids, operator);
    }

}
