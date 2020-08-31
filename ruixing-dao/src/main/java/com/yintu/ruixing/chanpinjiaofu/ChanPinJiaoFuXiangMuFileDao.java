package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;

public interface ChanPinJiaoFuXiangMuFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuXiangMuFileEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuXiangMuFileEntity record);

    ////////////////////////////////////////////////////////////////////////

    ChanPinJiaoFuXiangMuFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuXiangMuFileEntity record);

    int insertSelective(ChanPinJiaoFuXiangMuFileEntity record);
}