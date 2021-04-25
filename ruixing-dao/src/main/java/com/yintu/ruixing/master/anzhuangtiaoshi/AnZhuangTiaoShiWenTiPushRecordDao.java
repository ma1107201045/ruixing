package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiPushRecordEntity;

import java.util.List;

public interface AnZhuangTiaoShiWenTiPushRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWenTiPushRecordEntity record);

    AnZhuangTiaoShiWenTiPushRecordEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiPushRecordEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiPushRecordEntity record);
/////////////////////////////////////////////////////////////////////////////
    int insertSelective(AnZhuangTiaoShiWenTiPushRecordEntity record);

    List<AnZhuangTiaoShiWenTiPushRecordEntity> findPushMessageRecordById(Integer wid);

    String findFristPushNumber();


}