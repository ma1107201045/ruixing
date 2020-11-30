package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialOutInEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface AnZhuangTiaoShiMaterialOutInDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiMaterialOutInEntity record);

    AnZhuangTiaoShiMaterialOutInEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiMaterialOutInEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiMaterialOutInEntity record);


    ////////////////////////////////////////////////////////////
    int insertSelective(AnZhuangTiaoShiMaterialOutInEntity record);

    List<AnZhuangTiaoShiMaterialOutInEntity> findAllOutMaterial(String materialNumber);

    List<AnZhuangTiaoShiMaterialOutInEntity> findAllInMaterial(String materialNumber);
}