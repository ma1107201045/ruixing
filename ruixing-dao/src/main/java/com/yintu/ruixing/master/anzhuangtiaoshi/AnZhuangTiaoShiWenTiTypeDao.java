package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiTypeEntity;

import java.util.List;

public interface AnZhuangTiaoShiWenTiTypeDao {
    int insert(AnZhuangTiaoShiWenTiTypeEntity record);

    AnZhuangTiaoShiWenTiTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiTypeEntity record);



    //////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiTypeEntity record);

    int insertSelective(AnZhuangTiaoShiWenTiTypeEntity record);

    List<AnZhuangTiaoShiWenTiEntity> findWenTiType(String wenTiName, Integer typeId);

    List<AnZhuangTiaoShiWenTiEntity> findAllType(Integer typeId);
}