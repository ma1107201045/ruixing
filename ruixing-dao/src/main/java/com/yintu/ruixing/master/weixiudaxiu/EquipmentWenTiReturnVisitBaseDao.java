package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitBaseEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitBaseDao {
    int insert(EquipmentWenTiReturnVisitBaseEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitBaseEntity record);


    ////////////////////////////////////////////////////////////////////////////////

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitBaseEntity record);

    EquipmentWenTiReturnVisitBaseEntity selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(EquipmentWenTiReturnVisitBaseEntity record);

    String findRenWuNumberByType(Integer returncycletype);

    List<EquipmentWenTiReturnVisitBaseEntity> findAllReturnVisitRenWu(String tljName, String dwdName,
                                                                      String xdName, Integer returnuserid,
                                                                      Integer returncycletype, String statrTime,
                                                                      String endTime, Integer implementstate);


    List<EquipmentWenTiReturnVisitBaseEntity> findRenWuByNowTime(String today);
}