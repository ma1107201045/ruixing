package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksCheZhanEntity;
import org.apache.ibatis.annotations.Mapper;


public interface AnZhuangTiaoShiWorksCheZhanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorksCheZhanEntity record);

    AnZhuangTiaoShiWorksCheZhanEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorksCheZhanEntity record);


    //////////////////////////////////////////////////

    int insertSelective(AnZhuangTiaoShiWorksCheZhanEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWorksCheZhanEntity record);
}