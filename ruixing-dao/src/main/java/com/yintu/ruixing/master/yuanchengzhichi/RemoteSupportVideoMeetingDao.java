package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.RemoteSupportVideoMeetingEntity;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportVideoMeetingEntityWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface RemoteSupportVideoMeetingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteSupportVideoMeetingEntityWithBLOBs record);

    int insertSelective(RemoteSupportVideoMeetingEntityWithBLOBs record);

    RemoteSupportVideoMeetingEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteSupportVideoMeetingEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(RemoteSupportVideoMeetingEntityWithBLOBs record);

    int updateByPrimaryKey(RemoteSupportVideoMeetingEntity record);

    List<RemoteSupportVideoMeetingEntityWithBLOBs> selectByCondition(Integer[] ids, String context);
}