package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.danganguanli.LineBaseInformationStationEntity;

import java.util.List;

public interface LineBaseInformationStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationStationEntity record);

    int insertSelective(LineBaseInformationStationEntity record);

    LineBaseInformationStationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationStationEntity record);

    int updateByPrimaryKey(LineBaseInformationStationEntity record);

    List<LineBaseInformationEntity> selectHistoryByExample(Integer lineBaseInformationId, Integer id, String name, Integer[] ids);
}