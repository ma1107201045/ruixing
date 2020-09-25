package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiWorkNameLibraryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorkNameLibraryEntity record);

    AnZhuangTiaoShiWorkNameLibraryEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiWorkNameLibraryEntity record);


    ////////////////////////////////////

    int insertSelective(AnZhuangTiaoShiWorkNameLibraryEntity record);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorkName(@Param("workname") String workname);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorkNameLibraryEntity record);

    void deleteWorkNameByIds(Integer[] ids);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorkName();

    String findWorkNameByid(Integer wnlid);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorksById(Integer id);

    AnZhuangTiaoShiWorkNameLibraryEntity findOneWorkNameByid(Integer id);

    String findWorkNameById(@Param("wnlid") Integer wnlid);

}