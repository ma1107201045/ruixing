package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiFileEntity;

import java.util.List;

public interface ChanPinJiaoFuWenTiFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuWenTiFileEntity record);

    ChanPinJiaoFuWenTiFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuWenTiFileEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuWenTiFileEntity record);
/////////////////////////////////////////////////////////////////
    int insertSelective(ChanPinJiaoFuWenTiFileEntity record);

    List<ChanPinJiaoFuWenTiFileEntity> findWenTiFileByType(Integer fileType);
}