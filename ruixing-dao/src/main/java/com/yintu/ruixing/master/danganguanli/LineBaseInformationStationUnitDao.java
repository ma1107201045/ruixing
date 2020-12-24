package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationStationUnitEntity;

public interface LineBaseInformationStationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationStationUnitEntity record);

    int insertSelective(LineBaseInformationStationUnitEntity record);

    LineBaseInformationStationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationStationUnitEntity record);

    int updateByPrimaryKey(LineBaseInformationStationUnitEntity record);
}