package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationUnitEntity;

public interface LineBaseInformationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationUnitEntity record);

    int insertSelective(LineBaseInformationUnitEntity record);

    LineBaseInformationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationUnitEntity record);

    int updateByPrimaryKey(LineBaseInformationUnitEntity record);
}