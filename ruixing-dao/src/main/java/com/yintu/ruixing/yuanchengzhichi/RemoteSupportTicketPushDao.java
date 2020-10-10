package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RemoteSupportTicketPushDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportTicketPushEntity record);

    int insertSelective(RemoteSupportTicketPushEntity record);

    RemoteSupportTicketPushEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportTicketPushEntity record);

    int updateByPrimaryKey(RemoteSupportTicketPushEntity record);

    List<RemoteSupportTicketPushEntity> selectByCondition(Integer[] ids, String operator);
}