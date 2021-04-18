package com.yintu.ruixing.master.paigongguanli;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserDaystateEntity;
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

    List<PaiGongGuanLiUserEntity> finaAlreadyPaiGongPeople();

    String findUserName(Integer userid);


    List<PaiGongGuanLiUserEntity> findUser(Integer uidSet);

    String findUserNameById(Integer coordinationuserid);

    List<PaiGongGuanLiUserEntity> findBaoGongUser(Integer baoGongType);

    Integer findUseridByid(Integer coordinationuserid);

    void updateUserChuChaiTime(PaiGongGuanLiUserEntity userEntity);

    List<PaiGongGuanLiUserEntity> findUserByChuChaiTime(Integer uidSet, String startTime, String endTime);

    void deleteByUserid(Long userid);

    List<PaiGongGuanLiUserEntity> findUserID();

}