package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialOutInEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface AnZhuangTiaoShiMaterialOutInDao {
    int insert(AnZhuangTiaoShiMaterialOutInEntity record);

    AnZhuangTiaoShiMaterialOutInEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiMaterialOutInEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiMaterialOutInEntity record);


    ////////////////////////////////////////////////////////////

    int deleteByPrimaryKey(Integer id);

    int insertSelective(AnZhuangTiaoShiMaterialOutInEntity record);

    List<AnZhuangTiaoShiMaterialOutInEntity> findAllOutMaterial(String materialNumber,String materialsname,String materialsguige);

    List<AnZhuangTiaoShiMaterialOutInEntity> findAllInMaterial(String materialNumber,String materialsname,String materialsguige);
}