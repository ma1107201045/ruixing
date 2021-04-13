package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


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

    List<PaiGongGuanLiPaiGongDanEntity> findPaiGongDan(String paiGongNumber,
                                                       String startTime, String endTime, String xdName,
                                                       String czName, String renWuShuXing, Integer peopeleId, Integer paiGongState);

    List<PaiGongGuanLiPaiGongDanEntity> findAllPaiGongDan();

    String findAdressByUid(@Param("uid") Integer uid);

    long selectWorkOrderSum();

    List<PaiGongGuanLiPaiGongDanEntity> findAllNotOverPaiGong(Integer userid);
}