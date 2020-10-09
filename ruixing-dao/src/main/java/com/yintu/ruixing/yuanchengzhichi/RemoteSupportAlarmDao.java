package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface RemoteSupportAlarmDao {
    int isTableExist(String tableName);

    void deleteByPrimaryKey(String tableName, Integer id);

    List<RemoteSupportAlarmEntity> selectByCondition(String tableName, Integer stationId, Date startTime, Date endTime);
}