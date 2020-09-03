package com.yintu.ruixing.common;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.MessageEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/7 14:44
 */
public interface MessageService extends BaseService<MessageEntity, Integer> {

    /**
     * 按照类型和状态查询消息
     *
     * @param type   类型
     * @param Status 状态
     * @return 消息集
     */
    List<MessageEntity> findByTypeAndStatus(Short type, Integer loginUserId, Short Status);

    /**
     * 按照多条件查询
     *
     * @param messageEntity 条件
     * @return
     */
    List<MessageEntity> findByExample(MessageEntity messageEntity);

    /**
     * 从未读消息改为已读消息
     *
     * @param id 主键
     */
    void changeStatus(Integer id);

    /**
     * @param messageEntity 消息实体类
     */
    void sendMessage(MessageEntity messageEntity);

}
