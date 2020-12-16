package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.MenXianEntity;

import java.util.List;

public interface MenXianDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MenXianEntity record);

    int insertSelective(MenXianEntity record);

    MenXianEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MenXianEntity record);

    int updateByPrimaryKey(MenXianEntity record);

    List<MenXianEntity> selectByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quduanId, Integer propertyId);


    void insertBatch(List<MenXianEntity> menXianEntities);

    void deleteBatch(List<MenXianEntity> menXianEntities);

    String findMaxNumber(Integer czid, Integer qdid, Integer mid);

    String findMinNumber(Integer czid, Integer qdid, Integer mid);
}