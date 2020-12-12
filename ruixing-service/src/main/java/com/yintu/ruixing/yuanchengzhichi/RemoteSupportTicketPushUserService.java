package com.yintu.ruixing.yuanchengzhichi;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/10 13:58
 */
public interface RemoteSupportTicketPushUserService {

    void add(RemoteSupportTicketPushUserEntity remoteSupportTicketPushUserEntity);

    void deleteByPushId(Integer pushId);
}
