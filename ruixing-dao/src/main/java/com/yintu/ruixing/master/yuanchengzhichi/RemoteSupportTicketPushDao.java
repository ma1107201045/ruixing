package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.RemoteSupportTicketPushEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface RemoteSupportTicketPushDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportTicketPushEntity record);

    int insertSelective(RemoteSupportTicketPushEntity record);

    RemoteSupportTicketPushEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportTicketPushEntity record);

    int updateByPrimaryKey(RemoteSupportTicketPushEntity record);

    long countByOperator(String operator);

    List<RemoteSupportTicketPushEntity> selectByCondition(Integer[] ids, String operator);
}