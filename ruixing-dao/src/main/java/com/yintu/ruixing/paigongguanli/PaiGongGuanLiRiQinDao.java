package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
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
}