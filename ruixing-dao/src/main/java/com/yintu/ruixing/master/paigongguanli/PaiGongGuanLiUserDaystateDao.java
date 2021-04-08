package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserDaystateEntity;

public interface PaiGongGuanLiUserDaystateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiUserDaystateEntity record);

    PaiGongGuanLiUserDaystateEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiUserDaystateEntity record);

    int updateByPrimaryKey(PaiGongGuanLiUserDaystateEntity record);

/////////////////////////////////////////////////////////////////////////

    int insertSelective(PaiGongGuanLiUserDaystateEntity record);
}