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

    MenXianEntity selectByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quduanId, Integer propertyId);


    void insertBatch(List<MenXianEntity> menXianEntities);

    void deleteBatch(List<MenXianEntity> menXianEntities);

    String findMaxNumber(Integer czid, Integer qdid, Integer mid, Integer typee);

    String findMinNumber(Integer czid, Integer qdid, Integer mid, Integer typee);

    String findMaxNumberK(Integer czid, Integer qdid, Integer mid, Integer typee);

    String findMaxNumberZ(Integer czid, Integer qdid, Integer mid, Integer typee);

    String findMinNumberK(Integer czid, Integer qdid, Integer mid, Integer typee);

    String findMinNumberZ(Integer czid, Integer qdid, Integer mid, Integer typee);
}