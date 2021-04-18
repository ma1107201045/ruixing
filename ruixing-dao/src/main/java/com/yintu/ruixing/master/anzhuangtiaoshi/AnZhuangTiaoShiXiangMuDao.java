package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AnZhuangTiaoShiXiangMuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiXiangMuEntity record);

    int insertSelective(AnZhuangTiaoShiXiangMuEntity record);

    AnZhuangTiaoShiXiangMuEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiXiangMuEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiXiangMuEntity record);


    ////////////////////////////////////////////////


    List<AnZhuangTiaoShiXiangMuEntity> findSanJiShu();

    List<AnZhuangTiaoShiXiangMuEntity> findDiErJi(Integer xdFenlei);

    Object findOneXiangMU(Integer id);

    void addSanJiShuXiangMu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity);

    void editSanJiShu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity);

    void deletSanJiShuById(Integer id);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanDataByLeiXing(Integer leiXingId);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanNameAndYear();

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanBySomedata(@Param("xdname") String xdname, @Param("year") String year,
                                                              @Param("xdtype") String xdtype, @Param("xdleixing") Integer xdleixing);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanBySomedatas(Integer[] ids);

    List<AnZhuangTiaoShiXiangMuEntity> findXiangMuData(Integer[] ids);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuan();

    List<AnZhuangTiaoShiXiangMuEntity> findLastMonthXiangMu(@Param("today") String today, @Param("lastMothDay") String lastMothDay);

    AnZhuangTiaoShiXiangMuEntity findXiangMuById(Integer xdid);

    List<AnZhuangTiaoShiXiangMuEntity> findByXdId(Integer xdId);

    long selectProjectSum();

    String findXianDuanByXDid(Integer xdid);

    List<AnZhuangTiaoShiXiangMuEntity> findXiangMU(Integer xianDuanId);
}