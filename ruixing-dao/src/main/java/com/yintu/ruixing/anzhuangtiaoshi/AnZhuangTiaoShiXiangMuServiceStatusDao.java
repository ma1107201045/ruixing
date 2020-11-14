package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceStatusEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiXiangMuServiceStatusDao {
    int insert(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    AnZhuangTiaoShiXiangMuServiceStatusEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuServiceStatusEntity record);


    //////////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    int insertSelective(AnZhuangTiaoShiXiangMuServiceStatusEntity record);

    List<AnZhuangTiaoShiXiangMuServiceStatusEntity> findAllOrSomething(String serviceName);

    List<AnZhuangTiaoShiXiangMuServiceStatusEntity> findAllServiceStatus();

    Integer findSerid();
}