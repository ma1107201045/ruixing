package com.yintu.ruixing.yuanchengzhichi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface RemoteSupportAlarmDao {
    int isTableExist(String tableName);

    List<String> selectLikeTable(@Param("databaseName") String databaseName,@Param("tableName") String tableName);

    void deleteByPrimaryKey(String tableName, Integer id);

    RemoteSupportAlarmEntity selectByPrimaryKey(String tableName, Integer id);

    List<RemoteSupportAlarmEntity> selectByCondition(String joinSQL, Date startTime, Date endTime);
}