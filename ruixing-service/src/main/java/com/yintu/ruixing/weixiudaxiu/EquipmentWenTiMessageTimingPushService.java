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
}
