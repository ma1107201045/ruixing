package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.AlarmTableEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AlarmTableDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmTableEntity record);

    int insertSelective(AlarmTableEntity record);

    AlarmTableEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmTableEntity record);

    int updateByPrimaryKey(AlarmTableEntity record);

    /////////////////////////////////////////////////////////
    Integer findAlarmNumber(@Param("tableName") String tableName);

    void editAlarmState( @Param("status")Integer status, @Param("tableName") String tableName);

    List<AlarmTableEntity> findAllNotReadAlarmDatas(@Param("tableName") String tableName);

    List<AlarmTableEntity> findAllAlarmDatas(@Param("tableName") String tableName);

    List<AlarmTableEntity> findAllAlarmDatasByTimes(@Param("tableName")String tableName,@Param("starTime") Long starTime,@Param("endTime") Long endTime);

    List<AlarmTableEntity> findAllAlarmDatasBySomethings(@Param("toString") String toString,@Param("starTime") Date starTime,@Param("endTime") Date endTime);
}