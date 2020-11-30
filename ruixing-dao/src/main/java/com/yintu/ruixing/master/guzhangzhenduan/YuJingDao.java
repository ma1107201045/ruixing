package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.YuJingEntity;
import org.apache.ibatis.annotations.Mapper;


public interface YuJingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(YuJingEntity record);

    int insertSelective(YuJingEntity record);

    YuJingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YuJingEntity record);

    int updateByPrimaryKey(YuJingEntity record);
}