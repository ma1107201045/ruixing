package com.yintu.ruixing.weixiudaxiu;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/5/7 14:39
 * @Version 1.0
 * 需求:
 */
public interface EquipmentWenTiMessageTimingPushService {
    String findNumber();

    void addMessagePush(EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity);

    void editById(EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity);

    void deleteByIds(Integer[] ids);

    List<EquipmentWenTiMessageTimingPushEntity> findAllMessagePush(String tljName, String dwdName, String xdName,
                                                                   String startTime, String endTime, Integer implementState,
                                                                   Integer pushType);

    String findRecordNumberByPid(Integer pid);

    void addMessagePushRecord(EquipmentWenTiMessageTimingPushRecordEntity equipmentWenTiMessageTimingPushRecordEntity,
                              String filename, String filepath,Integer longinUserid);

    List<EquipmentWenTiReturnVisitRecordmessageEntity> findRecordByPid(Integer pid);

    void editMessagePushRecordById(EquipmentWenTiMessageTimingPushRecordEntity equipmentWenTiMessageTimingPushRecordEntity,
                                   String filename, String filepath,Integer longinUserid);

    void deleteMessagePushRecordByIds(Integer[] ids);

    List<EquipmentWenTiMessageTimingPushRecordEntity> findAllMessagePushRecord(Integer pushtype);
}
