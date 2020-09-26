package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiWorkNameTotalDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorkNameTotalEntity record);

    AnZhuangTiaoShiWorkNameTotalEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiWorkNameTotalEntity record);


    //////////////////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorkNameTotalEntity record);

    int insertSelective(AnZhuangTiaoShiWorkNameTotalEntity record);

    List<AnZhuangTiaoShiWorkNameTotalEntity> findWorkNameTotal(String workname);

    void deleteWorkNameTotalByIds(Integer[] ids);

    List<AnZhuangTiaoShiWorkNameTotalEntity> findAllWorks();

    AnZhuangTiaoShiWorkNameTotalEntity findOneWorksById(@Param("id")Integer id,@Param("receiverid") Integer receiverid);

    String findWorkNameTotalById(@Param("wntid") Integer wntid);

    AnZhuangTiaoShiWorkNameTotalEntity findOneWorksByIdd(Integer id);

    AnZhuangTiaoShiWorkNameTotalEntity findOneWorksByid(Integer id);
}