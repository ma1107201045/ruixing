package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
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