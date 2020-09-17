package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuServiceChooseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    Integer findCheZhanTotal(Integer id);

    List<Integer> findAllSeridByXDid(Integer id);

    List<Integer> findAllChoidBySerid(@Param("serid")Integer serid);

    Integer findTitleTotal(@Param("serid")Integer serid,@Param("id") Integer id);

    Integer findChooseTotal(@Param("choid") Integer choid,@Param("id") Integer id);
}