package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiTaskUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaiGongGuanLiTaskUserDao {
    int insert(PaiGongGuanLiTaskUserEntity record);

    PaiGongGuanLiTaskUserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiTaskUserEntity record);



    /////////////////////////////////////////////
    int insertSelective(PaiGongGuanLiTaskUserEntity record);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiTaskUserEntity record);

    List<Integer> findUid();

    void addTask(@Param("id") Integer id, @Param("uid") Integer uid);

    List<PaiGongGuanLiTaskUserEntity> findSomeUserPowerScore(@Param("userName") String userName);

    List<PaiGongGuanLiTaskUserEntity> findUserPowerScoreById(@Param("id") Integer id, @Param("taskTotalName") String taskTotalName);

    List<PaiGongGuanLiTaskUserEntity> findUser(Integer tid, Integer maxTaskshuxingNum, Integer minTaskshuxingNum);



}