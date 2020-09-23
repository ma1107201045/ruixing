package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiWorksDingDao {
    int insert(AnZhuangTiaoShiWorksDingEntity record);

    AnZhuangTiaoShiWorksDingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorksDingEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWorksDingEntity record);



    ////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int insertSelective(AnZhuangTiaoShiWorksDingEntity record);

    List<AnZhuangTiaoShiWorksCheZhanEntity> findCheZhanDatasByXid(Integer xid);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorksDatasByCid(Integer cid);

    AnZhuangTiaoShiWorksDingEntity findDataByIDS(@Param("czid") Integer czid,
                                                 @Param("worksid") Integer worksid,
                                                 @Param("wnlid") Integer wnlid);

    AnZhuangTiaoShiWorksDingEntity findOneWork(@Param("cid")Integer cid,
                                               @Param("wntid") Integer wntid,
                                               @Param("wnlId") Integer wnlId);


}