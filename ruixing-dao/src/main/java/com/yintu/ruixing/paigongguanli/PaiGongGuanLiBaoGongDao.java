package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PaiGongGuanLiBaoGongDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongEntity record);

    PaiGongGuanLiBaoGongEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongEntity record);


    ////////////////////////////////////////////////////////////////////////
    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongEntity record);

    int insertSelective(PaiGongGuanLiBaoGongEntity record);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(@Param("senderid") Integer senderid,@Param("datetime") Date datetime);
}