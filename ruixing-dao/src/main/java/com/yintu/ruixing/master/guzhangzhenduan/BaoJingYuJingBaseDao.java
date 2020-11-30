package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaoJingYuJingBaseDao {
    int insert(BaoJingYuJingBaseEntity record);

    BaoJingYuJingBaseEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BaoJingYuJingBaseEntity record);

    ////////////////////////////////////////////////////////////////////

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaoJingYuJingBaseEntity record);

    int insertSelective(BaoJingYuJingBaseEntity record);

    List<BaoJingYuJingBaseEntity> findBJYJData(@Param("context") String context,@Param("bjyjType") Integer bjyjType);

    List<BaoJingYuJingBaseEntity> findAllBaoJing();

    List<BaoJingYuJingBaseEntity> findBJDataBySomething(@Param("context") String context);

    String findAlarmContext(@Param("alarmcode")Integer alarmcode,@Param("bjyjType") Integer bjyjType);
}