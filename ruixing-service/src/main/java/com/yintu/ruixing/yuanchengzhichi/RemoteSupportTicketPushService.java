package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/10 13:46
 */
public interface RemoteSupportTicketPushService extends BaseService<RemoteSupportTicketPushEntity, Integer> {
    long countByOperator(String operator);

    void add(RemoteSupportTicketPushEntity remoteSupportTicketPushEntity, Integer[] userIds);

    void remove(Integer[] ids);

    List<RemoteSupportTicketPushEntity> findByCondition(Integer[] ids, String operator);
}
