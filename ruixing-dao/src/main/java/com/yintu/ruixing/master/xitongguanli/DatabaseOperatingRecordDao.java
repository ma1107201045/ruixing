package com.yintu.ruixing.master.xitongguanli;

import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface DatabaseOperatingRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(DatabaseOperatingRecordEntity record);

    int insertSelective(DatabaseOperatingRecordEntity record);

    DatabaseOperatingRecordEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DatabaseOperatingRecordEntity record);

    int updateByPrimaryKey(DatabaseOperatingRecordEntity record);

    List<DatabaseOperatingRecordEntity> selectAll();

    List<String> selectLikeTableNames(String databaseName, String... tableNames);
}