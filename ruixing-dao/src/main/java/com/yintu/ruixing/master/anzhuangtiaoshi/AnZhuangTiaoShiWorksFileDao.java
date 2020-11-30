package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnZhuangTiaoShiWorksFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorksFileEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWorksFileEntity record);


    ////////////////////////////////////////////
    AnZhuangTiaoShiWorksFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorksFileEntity record);

    int insertSelective(AnZhuangTiaoShiWorksFileEntity record);

    List<AnZhuangTiaoShiWorksFileEntity> findShuRuFileByXid(Integer id);

    List<AnZhuangTiaoShiWorksFileEntity> findShuChuFileByXid(Integer id);

    List<AnZhuangTiaoShiWorksFileEntity> findFileByNmae(@Param("xdid") Integer xdid,@Param("filename") String filename,@Param("filetype") Integer filetype);

    void deletFileByIds(Integer[] ids);
}