package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RemoteSupportTicketDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportTicketEntity record);

    int insertSelective(RemoteSupportTicketEntity record);

    RemoteSupportTicketEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportTicketEntity record);

    int updateByPrimaryKeyWithBLOBs(RemoteSupportTicketEntity record);

    int updateByPrimaryKey(RemoteSupportTicketEntity record);

    RemoteSupportTicketEntity selectLastByAlarmId(Long alarmId);

    List<RemoteSupportTicketEntity> selectByCondition(Integer[] ids, Short status, Long alarmId);
}