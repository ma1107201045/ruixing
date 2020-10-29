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

    List<Integer> findCZidByXDid(Integer xdid,String czName);

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllByCZid(Integer czid);

    Integer findCheZhanTotal(Integer id);

    List<Integer> findAllSeridByXDid(Integer id);

    List<Integer> findAllChoidBySerid(@Param("serid") Integer serid);

    Integer findTitleTotal(@Param("serid") Integer serid, @Param("id") Integer id);

    Integer findChooseTotal(@Param("choid") Integer choid, @Param("id") Integer id);

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findCheZhanByXDid(Integer id);

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllChoose();

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findServiceChoose(@Param("serid") Integer serid, @Param("xdid") Integer xdid, @Param("czid") Integer czid);

    void deleteByCzId(Integer czId);

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllChoidBySeridAndCzId(Integer serid, Integer czId);
}