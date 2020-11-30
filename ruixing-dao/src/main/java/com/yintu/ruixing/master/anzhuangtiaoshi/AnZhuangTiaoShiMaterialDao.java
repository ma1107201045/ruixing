package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnZhuangTiaoShiMaterialDao {
    int insert(AnZhuangTiaoShiMaterialEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiMaterialEntity record);



    ////////////////////////////////////////////////////////////

    AnZhuangTiaoShiMaterialEntity selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiMaterialEntity record);

    int insertSelective(AnZhuangTiaoShiMaterialEntity record);

    List<AnZhuangTiaoShiMaterialEntity> findAllMaterial(String materialName);

    List<AnZhuangTiaoShiMaterialEntity> findAllMaterialDatas(String materialNumber);

    Integer totalNumber(Integer id);

    List<AnZhuangTiaoShiMaterialEntity> findAllMaterials();

    void editMaterial(@Param("totalnumber") Integer totalnumber,@Param("id") Integer id);
}