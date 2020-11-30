package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    ///////////////////////////////////////////////////

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    int insertSelective(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity record);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameById(Integer id);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameByWorkname(@Param("awtId") Integer awtId, @Param("workname") String workname);

    Integer findWorkNameTatol(Integer id);

    void deleteWorkNameByIdss(Integer[] ids);

    List<Integer> findAllwnlidByWorksid(@Param("worksid") Integer worksid);

    Integer findWorkStateByIDS(@Param("worksid") Integer worksid, @Param("wnlid") Integer wnlid);

    AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity findOneWorkNameById(@Param("wntid") Integer wntid, @Param("wnlid") Integer wnlid, @Param("receiverid") Integer receiverid);

    Integer findWntidById(Integer id);
}