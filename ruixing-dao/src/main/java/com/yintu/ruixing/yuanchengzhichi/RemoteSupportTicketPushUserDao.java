package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RemoteSupportTicketPushUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportTicketPushUserEntity record);

    int insertSelective(RemoteSupportTicketPushUserEntity record);

    RemoteSupportTicketPushUserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportTicketPushUserEntity record);

    int updateByPrimaryKey(RemoteSupportTicketPushUserEntity record);

    void deleteByPushId(Integer pushId);
}