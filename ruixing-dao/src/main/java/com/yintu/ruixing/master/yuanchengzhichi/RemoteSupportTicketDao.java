package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.RemoteSupportTicketEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface RemoteSupportTicketDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportTicketEntity record);

    int insertSelective(RemoteSupportTicketEntity record);

    RemoteSupportTicketEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportTicketEntity record);

    int updateByPrimaryKeyWithBLOBs(RemoteSupportTicketEntity record);

    int updateByPrimaryKey(RemoteSupportTicketEntity record);

    RemoteSupportTicketEntity selectLastByAlarmId(String alarmId);

    List<RemoteSupportTicketEntity> selectByCondition(Integer[] ids, Short status, String alarmId);
}