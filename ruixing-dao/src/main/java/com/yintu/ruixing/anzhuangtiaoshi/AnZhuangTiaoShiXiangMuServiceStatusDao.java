package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnZhuangTiaoShiXiangMuServiceStatusDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    AnZhuangTiaoShiXiangMuServiceStatusEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    //////////////////////////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    int insertSelective(AnZhuangTiaoShiXiangMuServiceStatusEntity record);
}