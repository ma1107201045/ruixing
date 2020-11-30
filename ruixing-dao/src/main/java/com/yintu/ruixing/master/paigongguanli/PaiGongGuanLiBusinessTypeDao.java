package com.yintu.ruixing.master.paigongguanli;


import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBusinessTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PaiGongGuanLiBusinessTypeDao {
    int insert(PaiGongGuanLiBusinessTypeEntity record);

    PaiGongGuanLiBusinessTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiBusinessTypeEntity record);



    //////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBusinessTypeEntity record);

    int insertSelective(PaiGongGuanLiBusinessTypeEntity record);

    List<PaiGongGuanLiBusinessTypeEntity> findSomeBusinessTypea(@Param("businessTypeaName") String businessTypeaName);

    List<PaiGongGuanLiBusinessTypeEntity> findSomeChuChaRenWu(@Param("id") Integer id,@Param("businessTypeaName") String businessTypeaName);

    List<PaiGongGuanLiBusinessTypeEntity> findChuChaRenWu(Integer id);

    List<PaiGongGuanLiBusinessTypeEntity> findAllBusinessType();

    List<PaiGongGuanLiBusinessTypeEntity> findChuChaiById(Integer id);

    List<PaiGongGuanLiBusinessTypeEntity> findAllBuiness();

    List<PaiGongGuanLiBusinessTypeEntity> findBuinessById(Integer id);
}