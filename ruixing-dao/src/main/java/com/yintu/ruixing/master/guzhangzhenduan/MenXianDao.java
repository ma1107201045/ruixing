package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.MenXianEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface MenXianDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MenXianEntity record);

    int insertSelective(MenXianEntity record);

    MenXianEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MenXianEntity record);

    int updateByPrimaryKey(MenXianEntity record);

    MenXianEntity selectByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quduanId, Integer propertyId);

}