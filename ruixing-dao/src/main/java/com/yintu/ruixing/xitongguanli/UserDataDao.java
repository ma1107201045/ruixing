package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDataDao {
    int deleteByPrimaryKey(Long id);

    int insert(UserDataEntity record);

    int insertSelective(UserDataEntity record);

    UserDataEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDataEntity record);

    int updateByPrimaryKey(UserDataEntity record);

    void deleteByUserId(Long userId);

    List<UserDataEntity> selectByUserId(Long userId);
}