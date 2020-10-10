package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.yuanchengzhichi.RemoteSupportTicketPushUserDao;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportTicketPushUserEntity;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportTicketPushUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/10 13:59
 */
@Service
@Transactional
public class RemoteSupportTicketPushUserServiceImpl implements RemoteSupportTicketPushUserService {

    @Autowired
    private RemoteSupportTicketPushUserDao remoteSupportTicketPushUserDao;

    @Override
    public void add(RemoteSupportTicketPushUserEntity remoteSupportTicketPushUserEntity) {
        remoteSupportTicketPushUserDao.insertSelective(remoteSupportTicketPushUserEntity);
    }

    @Override
    public void deleteByPushId(Integer pushId) {
        remoteSupportTicketPushUserDao.deleteByPushId(pushId);
    }
}
