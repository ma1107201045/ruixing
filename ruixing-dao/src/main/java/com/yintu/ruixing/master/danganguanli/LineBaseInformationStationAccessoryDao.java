package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationAccessoryEntity;

import java.util.List;

public interface LineBaseInformationStationAccessoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationStationAccessoryEntity record);

    int insertSelective(LineBaseInformationStationAccessoryEntity record);

    LineBaseInformationStationAccessoryEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationStationAccessoryEntity record);

    int updateByPrimaryKey(LineBaseInformationStationAccessoryEntity record);

    List<LineBaseInformationStationAccessoryEntity> selectByExample(LineBaseInformationStationAccessoryEntity query);
}