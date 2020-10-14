package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaiGongGuanLiPaiGongDanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiPaiGongDanEntity record);

    int updateByPrimaryKeySelective(PaiGongGuanLiPaiGongDanEntity record);

    int updateByPrimaryKey(PaiGongGuanLiPaiGongDanEntity record);

    ////////////////////////////////////////////////////////////

    PaiGongGuanLiPaiGongDanEntity selectByPrimaryKey(Integer id);

    int insertSelective(PaiGongGuanLiPaiGongDanEntity record);

    String findPaiGongDanNum(String suoxie);

    List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum);

    List<PaiGongGuanLiPaiGongDanEntity> findUserByName(String truename);

    List<PaiGongGuanLiPaiGongDanEntity> findUserByUserid(Integer userid);

    List<PaiGongGuanLiPaiGongDanEntity> findPaiGongDan(String paiGongNumber);

    List<PaiGongGuanLiPaiGongDanEntity> findAllPaiGongDan();

    String findAdressByUid(@Param("uid") Integer uid);
}