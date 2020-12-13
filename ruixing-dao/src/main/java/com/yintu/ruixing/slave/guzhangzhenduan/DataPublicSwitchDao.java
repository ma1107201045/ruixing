package com.yintu.ruixing.slave.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.DataPublicSwitchEntity;

import java.util.List;

public interface DataPublicSwitchDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DataPublicSwitchEntity record);

    int insertSelective(DataPublicSwitchEntity record);

    DataPublicSwitchEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataPublicSwitchEntity record);

    int updateByPrimaryKey(DataPublicSwitchEntity record);

    int isTableExist(String tableName);

    DataPublicSwitchEntity selectNewFirstData(String tableName);
}