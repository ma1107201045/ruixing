package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    AnZhuangTiaoShiWorkNameLibraryEntity findOneWorkNameByid(@Param("id") Integer id,@Param("receiverid") Integer receiverid);

    String findWorkNameById(@Param("wnlid") Integer wnlid);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorkNameByXMId(Integer xiangMuId);
}