package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.AlarmTableEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    void editAlarmState(AlarmTableEntity alarmTableEntity,String tableName);

    List<AlarmTableEntity> findAllNotReadAlarmDatas(@Param("tableName")String tableName);

}