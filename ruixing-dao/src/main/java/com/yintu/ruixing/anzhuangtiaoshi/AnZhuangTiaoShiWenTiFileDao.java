package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiWenTiFileDao {
    int insert(AnZhuangTiaoShiWenTiFileEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiFileEntity record);


    //////////////////////////////////////////

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiFileEntity record);

    int deleteByPrimaryKey(Integer id);

    AnZhuangTiaoShiWenTiFileEntity selectByPrimaryKey(Integer id);

    int insertSelective(AnZhuangTiaoShiWenTiFileEntity record);

    List<AnZhuangTiaoShiWenTiFileEntity> findAllFanKuiFileById(@Param("wid") Integer wid,@Param("fileName") String fileName);

    List<AnZhuangTiaoShiWenTiFileEntity> findAllShuChuFileById(@Param("wid") Integer wid,@Param("fileName") String fileName);

    List<AnZhuangTiaoShiWorksFileEntity> findFileById(Integer id);
}