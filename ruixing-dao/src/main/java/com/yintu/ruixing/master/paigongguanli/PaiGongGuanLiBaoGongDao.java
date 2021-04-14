package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PaiGongGuanLiBaoGongDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongEntity record);

    PaiGongGuanLiBaoGongEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongEntity record);


    ////////////////////////////////////////////////////////////////////////
    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongEntity record);

    int insertSelective(PaiGongGuanLiBaoGongEntity record);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(@Param("senderid") Integer senderid,@Param("datetime") Date datetime);

    PaiGongGuanLiBaoGongEntity findJingWeiDuByUid(@Param("uid") Integer uid);

    String findAdressByUid(@Param("uid") Integer uid);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGong(String yesterdayDate, String tommorowDate, Integer baoGongType);

    List<PaiGongGuanLiBaoGongEntity> findBaoGongBySomethings(String startTime, String endTime, Integer userid, String xianDuan, Integer isNotClose, Integer baoGongType);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongAndAllComment(Integer userid, String riqi);

    List<PaiGongGuanLiBaoGongEntity> findAllChuChaiPeopele(String lastDateTime, String nowDateTime);
}