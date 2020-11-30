package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusChooseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AnZhuangTiaoShiXiangMuServiceStatusChooseDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    ////////////////////////////////////////////////////////////////////

    AnZhuangTiaoShiXiangMuServiceStatusChooseEntity selectByPrimaryKey(Integer id);

    int insertSelective(AnZhuangTiaoShiXiangMuServiceStatusChooseEntity record);

    void deleteBySid(Integer sid);

    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> findAllBySid(Integer id);

    String findNameBysid(Integer choid);

    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> findOneChooseBySidid(@Param("serid") Integer serid);
}