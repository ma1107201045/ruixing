package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface AnZhuangTiaoShiCheZhanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiCheZhanEntity record);

    int insertSelective(AnZhuangTiaoShiCheZhanEntity record);

    AnZhuangTiaoShiCheZhanEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiCheZhanEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiCheZhanEntity record);


    ///////////////////////////////////////////////////

    void addCheZhan(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanById(Integer id);

    void editCheZhanById(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity);

    void deleteCheZhanByIds(Integer[] ids);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByName(String czname);

    Integer findCheZhanTotal(Integer id);

    Integer findJiGuiTotal(Integer id);

    Integer findIndoorKaBantotal(Integer id);

    Integer findOutdoorSheBeiTotal(Integer id);

    Integer findPeiXianTotal(Integer id);

    Integer findShangDianTotal(Integer id);

    Integer findJingTaiYanShouTotal(Integer id);

    Integer findDongTaiYanShouTotal(Integer id);

    Integer findLianTiaoLianShiTotal(Integer id);

    Integer findShiYunXingTotal(Integer id);

    Integer findKaiTongTotal(Integer id);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanData(Integer[] ids);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByXid(Integer xid);
}