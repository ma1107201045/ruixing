package com.yintu.ruixing.weixiudaxiu;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/21 17:22
 * @Version 1.0
 * 需求:
 */
public interface EquipmentWenTiReturnVisitService {

    void editReturnVisitById(EquipmentWenTiReturnVisitEntity equipmentWenTiReturnVisitEntity,
                             String fristFileName,String firstFilePath,String secondFileName,String secondFilePath,Integer longinUserid);

    List<EquipmentWenTiReturnVisitEntity> findAllReturnVisit(String renWuNumber, String recordNumber,
                                                             String tljName, String dwdName, String xdName,
                                                             Integer returnUserid, Integer renWuState,
                                                             Integer pushState, String returnWenti,
                                                             Integer wentiState, String startTime,
                                                             String endTime, String years, Integer week,Integer longinuserid);

    void deleteReturnVisitByIds(Integer[] ids);

    void addFile(EquipmentWenTiReturnVisitFileEntity equipmentWenTiReturnVisitFileEntity);

    List<EquipmentWenTiReturnVisitFileEntity> findFie(Integer filetype,Integer vid);

    void deleteFileById(Integer id);

    void addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity);

    List<EquipmentWenTiReturnVisitCommentEntity> findCommentByVid(Integer vid);

    EquipmentWenTiReturnVisitEntity findOneReturnVisitById(Integer id);

    void pushMessage(EquipmentWenTiReturnVisitPushRecordEntity equipmentWenTiReturnVisitPushRecordEntity);

    List<EquipmentWenTiReturnVisitPushRecordEntity> findPushMessageRecordById(Integer vid);
}
