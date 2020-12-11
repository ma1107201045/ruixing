package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.AlarmTableEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlarmTableDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmTableEntity record);

    int insertSelective(AlarmTableEntity record);

    AlarmTableEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmTableEntity record);

    int updateByPrimaryKey(AlarmTableEntity record);

    /////////////////////////////////////////////////////////
    Integer findAlarmNumber(@Param("czid") Integer czid);

    void editAlarmState( @Param("status")boolean status);

    List<AlarmEntity> findAllNotReadAlarmDatas();

    List<AlarmEntity> findAllAlarmDatas();

    List<AlarmTableEntity> findAllAlarmDatasByTimes(@Param("tableName")String tableName,@Param("starTime") Long starTime,@Param("endTime") Long endTime);

    List<AlarmEntity> findAllAlarmDatasBySomethings(@Param("dwdid") Integer dwdid,@Param("starTime") long starTime,@Param("endTime") long endTime);

    //Integer findAllAlarmNumber(String toString);

    List<AlarmEntity> findAllNotReadAlarmDatasByCZid(Integer czid);

    Integer findAllAlarmNumber();

    Integer findAllAlarmNumberByDwdid(Integer dwdid);

    Integer findAllAlarmNumberByDwdidAndXid(Integer dwdid, Integer xdid);

    List<AlarmEntity> findAllAlarmDatasByDwdidAndXdid(Integer dwdid, Integer xdid, long starTime, long endTime);

    List<AlarmEntity> findAllAlarmDatasByczid(Integer czid, long starTime, long endTime);
}