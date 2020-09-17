package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusChooseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
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
}