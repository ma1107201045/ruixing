package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.AlarmEntity;

import java.util.List;

public interface AlarmDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmEntity record);

    int insertSelective(AlarmEntity record);

    AlarmEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmEntity record);

    int updateByPrimaryKey(AlarmEntity record);

    List<AlarmEntity> selectByExample(Integer tid, Integer did, Integer xid, Integer cid, Integer qid, Integer beginTime, Integer endTime, Integer id, Integer xtType, Integer alarmlevel, Integer faultStatus, Integer disposeStatus, Integer idea);
}