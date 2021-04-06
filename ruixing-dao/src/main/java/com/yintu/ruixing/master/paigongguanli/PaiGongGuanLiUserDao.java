package com.yintu.ruixing.master.paigongguanli;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserEntity;

import java.util.List;

public interface PaiGongGuanLiUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(PaiGongGuanLiUserEntity record);

    PaiGongGuanLiUserEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaiGongGuanLiUserEntity record);


    ////////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(PaiGongGuanLiUserEntity record);

    int insertSelective(PaiGongGuanLiUserEntity record);

    List<PaiGongGuanLiUserEntity> findAllUser(String name);

    List<Integer> findUid();

    void updateByUid(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity);

}