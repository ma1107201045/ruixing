package com.yintu.ruixing.dao;

import com.yintu.ruixing.entity.QuDuanBaseEntity;

import java.util.List;

public interface QuDuanBaseDao {
    //int deleteByPrimaryKey(Integer id);

    int insert(QuDuanBaseEntity record);

    //int insertSelective(QuDuanBaseEntity record);

    QuDuanBaseEntity selectByPrimaryKey(Integer id);

    List<QuDuanBaseEntity> selectByXidAndCid(Integer xid, Integer cid);

    //int updateByPrimaryKeySelective(QuDuanBaseEntity record);

    int updateByPrimaryKey(QuDuanBaseEntity record);




    List<QuDuanBaseEntity> findAllQuDuan();

    List<QuDuanBaseEntity> findAllDianMaHua();

    List<QuDuanBaseEntity> findAllQuDuanByCid(Integer cid);

    List<QuDuanBaseEntity> findAllDianMaHuaByCid(Integer cid);

    void insertSelective(QuDuanBaseEntity quDuanBaseEntity);

    void updateByPrimaryKeySelective(QuDuanBaseEntity quDuanBaseEntity);

    void deleteByPrimaryKey(Integer id);

    void deletQuDuanByIds(Integer[] ids);
}