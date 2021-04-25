package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitEntity;

import java.util.List;

public interface EquipmentWenTiReturnVisitDao {
    int insert(EquipmentWenTiReturnVisitEntity record);

    int updateByPrimaryKey(EquipmentWenTiReturnVisitEntity record);


    /////////////////////////////////////////////////////////////////////////
    EquipmentWenTiReturnVisitEntity selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentWenTiReturnVisitEntity record);

    int insertSelective(EquipmentWenTiReturnVisitEntity record);

    List<EquipmentWenTiReturnVisitEntity> findAllReturnVisit(String renWuNumber, String recordNumber,
                                                             String tljName, String dwdName, String xdName,
                                                             Integer returnUserid, Integer renWuState,
                                                             Integer pushState, String returnWenti,
                                                             Integer wentiState, String startTime,
                                                             String endTime, String years, Integer week,
                                                             Integer longinuserid,String todaystring);

    void editStateByYearAndDatas(String years, Integer dataNum);
}