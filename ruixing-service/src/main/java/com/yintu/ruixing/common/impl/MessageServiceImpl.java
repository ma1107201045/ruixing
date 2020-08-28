package com.yintu.ruixing.common.impl;

import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:mlf
 * @date:2020/7/7 14:44
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    public final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public void add(MessageEntity entity) {

        messageDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        messageDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MessageEntity entity) {
        messageDao.updateByPrimaryKeyWithBLOBs(entity);
    }

    @Override
    public MessageEntity findById(Integer id) {
        return messageDao.selectByPrimaryKey(id);
    }

    @Override
    public List<MessageEntity> findByTypeAndStatus(Short type, Short Status) {
        return messageDao.selectByTypeAndStatus(type, Status);
    }

    @Override
    public void changeStatus(Integer id) {
        MessageEntity messageEntity = this.findById(id);
        if (messageEntity != null && messageEntity.getStatus().equals((short) 1)) {
            messageEntity.setStatus((short) 2);
            this.edit(messageEntity);
        }

    }

    @Override
    public void sendMessage(MessageEntity messageEntity) {
        executorService.submit(new MessageRunnable(this, messageEntity));
    }


}

/**
 * 异步发送消息
 */
class MessageRunnable implements Runnable {
    private final MessageService messageService;

    private final MessageEntity messageEntity;

    MessageRunnable(MessageService messageService, MessageEntity messageEntity) {
        this.messageService = messageService;
        this.messageEntity = messageEntity;
    }

    @Override
    public void run() {
        messageService.add(messageEntity);
    }
}
