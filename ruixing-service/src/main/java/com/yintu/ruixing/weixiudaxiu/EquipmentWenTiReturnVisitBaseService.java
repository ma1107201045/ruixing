package com.yintu.ruixing.weixiudaxiu;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/22 15:29
 * @Version 1.0
 * 需求:
 */
public interface EquipmentWenTiReturnVisitBaseService {

    String findRenWuNumber(Integer returncycletype);

    void addReturnVisitRenWu(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity);

    List<EquipmentWenTiReturnVisitRecordmessageEntity> findRecordById(Integer id);

    void deleteReturnVisitRenWuByIds(Integer[] ids);

    void editReturnVisitRenWuById(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity);

    List<EquipmentWenTiReturnVisitBaseEntity> findAllReturnVisitRenWu(String tljName, String dwdName, String xdName,
                                                                      Integer returnuserid, Integer returncycletype,
                                                                      String statrTime, String endTime, Integer implementstate);

    void editImplementstateById(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity);

    void findRenWuByNowTime(String today);

    void findRenWuByNowMonth(String today);

    void findRenWuByNowQuarter(String today);

    void findRenWuByNowYear(String today);
}
