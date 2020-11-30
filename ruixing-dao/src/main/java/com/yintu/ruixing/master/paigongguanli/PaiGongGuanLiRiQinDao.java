package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PaiGongGuanLiRiQinDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiRiQinEntity record);

    PaiGongGuanLiRiQinEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiRiQinEntity record);


    ////////////////////////////////
    int updateByPrimaryKeySelective(PaiGongGuanLiRiQinEntity record);

    int insertSelective(PaiGongGuanLiRiQinEntity record);

    List<PaiGongGuanLiRiQinEntity> findAllRiQin();

    String findUserDongTai(Integer uid);

    List<PaiGongGuanLiRiQinEntity> findAllRiQinByUid(Integer uid);

    PaiGongGuanLiRiQinEntity findRiQin(@Param("senderid") Integer senderid,@Param("starttime") Date starttime);

    void deleteRiQin(@Param("starttime")Date starttime);

    List<PaiGongGuanLiRiQinEntity> findAllRiQinDatas();

    List<PaiGongGuanLiRiQinEntity> findRiQinByUid(Integer uid);

    List<PaiGongGuanLiRiQinEntity> findPropleAddress();

    List<PaiGongGuanLiRiQinEntity> findPeopleAddressOnMap();

}