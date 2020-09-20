package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiWenTiDao {
    int insert(AnZhuangTiaoShiWenTiEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiEntity record);


    ///////////////////////////////////////////////////////
    AnZhuangTiaoShiWenTiEntity selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiEntity record);

    int insertSelective(AnZhuangTiaoShiWenTiEntity record);

    List<AnZhuangTiaoShiWenTiEntity> findSomeWenTi(@Param("xdname") String xdname,@Param("wenTiMiaoShu") String wenTiMiaoShu);

    List<AnZhuangTiaoShiWenTiEntity> findAllNotDoWellWenTi();

}