package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.SkylightTimeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface SkylightTimeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SkylightTimeEntity record);

    int insertSelective(SkylightTimeEntity record);

    SkylightTimeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SkylightTimeEntity record);

    int updateByPrimaryKey(SkylightTimeEntity record);

    SkylightTimeEntity selectByCzIdAndQdId(Integer czId, Integer qdId);

    List<SkylightTimeEntity> connectSelectByCondition(Integer id, Date startTime, Date endTime, Integer czId, Integer qdId);

    SkylightTimeEntity findSkyLight(@Param("stationId") Integer stationId, @Param("sectionId") Integer sectionId);

    long countByCzIdAndQdIdAndTime(Integer czId, Integer qdId, Date time);
}