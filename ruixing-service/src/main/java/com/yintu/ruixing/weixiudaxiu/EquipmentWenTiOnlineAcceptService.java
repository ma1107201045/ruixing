package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/27 15:50
 * @Version 1.0
 * 需求:
 */
public interface EquipmentWenTiOnlineAcceptService {
    void addWenTiType(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity);

    void editWenTiTypeById(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity);

    List<EquipmentWenTiCustomerWenTiTypeEntity> findAllWenTiType();

    void deleteWenTiTypeByIds(Integer[] ids);

    void addAcceptFeedback(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity);

    List<DianWuDuanEntity> findAllDianWuDuan();

    List<XianDuanEntity> findAllXianDuan();

    List<TieLuJuEntity> findAllTieLuJu();

    String findNumber(String tljName);

    void editAcceptFeedbackById(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity);

    void deleteByIds(Integer[] ids);

    List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAcceptFeedbackByphone(String phone);

    void acceptById(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity);

}
