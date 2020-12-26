package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuFileAuditorEntity;

import java.util.List;

public interface ChanPinJiaoFuFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuFileAuditorEntity record);

    int insertSelective(ChanPinJiaoFuFileAuditorEntity record);

    ChanPinJiaoFuFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuFileAuditorEntity record);

    int updateByPrimaryKeyWithBLOBs(ChanPinJiaoFuFileAuditorEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuFileAuditorEntity record);

    List<ChanPinJiaoFuFileAuditorEntity> findAuditorDatas(Integer objectid, Integer objectType, Integer auditorid, Integer sort, Short activate);

    List<Integer> findUserIdByids(Integer objectid, Integer objectType);
}