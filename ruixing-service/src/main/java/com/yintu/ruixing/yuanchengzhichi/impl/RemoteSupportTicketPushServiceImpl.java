package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private MessageService messageService;

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
    public long countByOperator(String operator) {
        return remoteSupportTicketPushDao.countByOperator(operator);
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
            if (remoteSupportTicketPushEntity.getType() != null && remoteSupportTicketPushEntity.getType() == 2) {
                //当推送消息为2（站内）方式时，发消息。
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(remoteSupportTicketPushEntity.getCreateBy());
                messageEntity.setCreateTime(remoteSupportTicketPushEntity.getCreateTime());
                messageEntity.setModifiedBy(remoteSupportTicketPushEntity.getModifiedBy());
                messageEntity.setModifiedTime(remoteSupportTicketPushEntity.getModifiedTime());
                messageEntity.setTitle("消息");
                messageEntity.setContext("报警/预警处置意见");
                messageEntity.setType((short) 20);
                messageEntity.setSmallType((short) 1);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(null);
                messageEntity.setFileId(null);
                messageEntity.setSenderId(null);
                messageEntity.setReceiverId(userId);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
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
