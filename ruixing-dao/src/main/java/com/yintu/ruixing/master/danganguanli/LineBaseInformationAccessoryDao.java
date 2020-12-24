package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationAccessoryEntity;

public interface LineBaseInformationAccessoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationAccessoryEntity record);

    int insertSelective(LineBaseInformationAccessoryEntity record);

    LineBaseInformationAccessoryEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationAccessoryEntity record);

    int updateByPrimaryKey(LineBaseInformationAccessoryEntity record);
}