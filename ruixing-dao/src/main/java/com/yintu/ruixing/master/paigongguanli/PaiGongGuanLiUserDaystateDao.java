package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserDaystateEntity;

import java.util.List;

public interface PaiGongGuanLiUserDaystateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiUserDaystateEntity record);

    PaiGongGuanLiUserDaystateEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiUserDaystateEntity record);

    int updateByPrimaryKey(PaiGongGuanLiUserDaystateEntity record);

/////////////////////////////////////////////////////////////////////////

    int insertSelective(PaiGongGuanLiUserDaystateEntity record);

    List<PaiGongGuanLiUserDaystateEntity> findAllUser(String monthfirst, String monthlast);

    List<PaiGongGuanLiUserDaystateEntity> findOneUser(Integer userid, String monthfirst, String monthlast);

    void updateUserDayState(Integer paigongpeopleid, String chuChaStart, String chuChaEnd);
}