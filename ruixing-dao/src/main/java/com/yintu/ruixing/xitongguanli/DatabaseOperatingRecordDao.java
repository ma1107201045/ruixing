package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DatabaseOperatingRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(DatabaseOperatingRecordEntity record);

    int insertSelective(DatabaseOperatingRecordEntity record);

    DatabaseOperatingRecordEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DatabaseOperatingRecordEntity record);

    int updateByPrimaryKey(DatabaseOperatingRecordEntity record);

    List<String> selectLikeTableNames(String databaseName, String... tableNames);
}