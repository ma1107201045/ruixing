package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceChooseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiXiangMuServiceChooseDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiXiangMuServiceChooseEntity record);

    int insertSelective(AnZhuangTiaoShiXiangMuServiceChooseEntity record);

    AnZhuangTiaoShiXiangMuServiceChooseEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuServiceChooseEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuServiceChooseEntity record);

    //////////////////////////////////////////////////////////////////////

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllByXDid(Integer xdid);

    List<Integer> findCZidByXDid(Integer xdid);

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllByCZid(Integer czid);
}