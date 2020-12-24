package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryEntity;

import java.util.List;

public interface LineBaseInformationAccessoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationAccessoryEntity record);

    int insertSelective(LineBaseInformationAccessoryEntity record);

    LineBaseInformationAccessoryEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationAccessoryEntity record);

    int updateByPrimaryKey(LineBaseInformationAccessoryEntity record);

    List<LineBaseInformationAccessoryEntity> selectByExample(LineBaseInformationAccessoryEntity query);
}