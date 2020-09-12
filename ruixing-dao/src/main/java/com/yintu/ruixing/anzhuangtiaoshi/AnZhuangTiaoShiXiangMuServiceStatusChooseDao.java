package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusChooseEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnZhuangTiaoShiXiangMuServiceStatusChooseDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    AnZhuangTiaoShiXiangMuServiceStatusChooseEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    ////////////////////////////////////////////////////////////////////
    int insertSelective(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    void deleteBySid(Integer sid);
}