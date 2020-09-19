package com.yintu.ruixing.anzhuangtiaoshi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AnZhuangTiaoShiTrainDao {
    int insert(AnZhuangTiaoShiTrainEntity record);

    AnZhuangTiaoShiTrainEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AnZhuangTiaoShiTrainEntity record);


    /////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiTrainEntity record);

    int insertSelective(AnZhuangTiaoShiTrainEntity record);

    List<AnZhuangTiaoShiTrainEntity> findAllTrain(@Param("xdName") String xdName,@Param("customer") String customer);

    List<AnZhuangTiaoShiTrainEntity> findReJiShu();

    List<AnZhuangTiaoShiTrainEntity> findTrainBytraintype(@Param("traintype") Integer traintype);

    List<AnZhuangTiaoShiTrainEntity> findTrainByid(Integer id);
}