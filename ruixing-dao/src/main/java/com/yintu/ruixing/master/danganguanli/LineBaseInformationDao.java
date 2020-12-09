package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.util.List;

public interface LineBaseInformationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationEntity record);

    int insertSelective(LineBaseInformationEntity record);

    LineBaseInformationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationEntity record);

    int updateByPrimaryKeyWithBLOBs(LineBaseInformationEntity record);

    int updateByPrimaryKey(LineBaseInformationEntity record);

    List<LineBaseInformationEntity> selectByExample(Integer[] ids);

    List<DianWuDuanEntity> selectDianWuDuanEntityById(Integer id);
}