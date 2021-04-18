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

    void editUserDayState(Integer userid, String dayTime, Integer dayState);

    void editUserotherState(Integer userid, String today, Integer otherState);

    void editUserBaoGongState(Integer daiUserid, String today, Integer baoGongState);

    void updateUserDayStateRiQin(Integer userid, String dayTime, Integer dayState);

    void editUserotherStateOverChuChai(Integer paigongpeople, String chuchaiKaiShiTime, String today);

    void deleteByUserid(Integer userid);
}