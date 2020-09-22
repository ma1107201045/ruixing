package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    ///////////////////////////////////////////////////

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    int insertSelective(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameById(Integer id);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameByWorkname(@Param("workname") String workname);

    Integer findWorkNameTatol(Integer id);

    void deleteWorkNameByIdss(Integer[] ids);

    List<Integer> findAllwnlidByWorksid(@Param("worksid")Integer worksid);

    Integer findWorkStateByIDS(@Param("worksid") Integer worksid,@Param("wnlid") Integer wnlid);
}