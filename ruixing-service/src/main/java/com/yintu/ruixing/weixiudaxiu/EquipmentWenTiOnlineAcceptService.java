package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.danganguanli.CustomerEntity;
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

    List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAllAcceptFeedback(String number, String statrTime, String endTime,
                                                                         String tljName, String dwdName, String xdName,
                                                                         String feedbackName, String wentiType,
                                                                         String wentiMiaoshu, Integer feedbackState,
                                                                         Integer acceptUserid, Integer wentiState,
                                                                         Integer pushState);


    void editAcceptFeedbackByIdByUser(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity,
                                      String fileName, String filePath, Integer longinUserid);

    List<EquipmentWenTiOnlineAcceptFeedbackFileEntity> findFileByfid(Integer fid);

    void addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity);

    List<EquipmentWenTiReturnVisitCommentEntity> findCommentByFid(Integer fid);

    EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity findOneAcceptFeedbackById(Integer id);

    void pushMessage(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity equipmentWenTiOnlineAcceptFeedbackPushRecordEntity);

    List<EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity> findPushMessageRecordById(Integer fid);

    List<CustomerEntity> findAllCustomer();
}
