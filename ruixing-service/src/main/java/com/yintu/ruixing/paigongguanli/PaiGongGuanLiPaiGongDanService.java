package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.common.MessageEntity;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:32
 * @Version 1.0
 * 需求: 派工单
 */
public interface PaiGongGuanLiPaiGongDanService {

    String findPaiGongDanNum(String suoxie);

    void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username, Integer senderid);

    List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum);

    void editPaiGongDanById(Integer id, Integer senderid, Integer uid, PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username);

    void deletePaiGongDanByIds(Integer[] ids);

    void doSomeThing(Integer receiverid, Integer senderid, Integer id, Integer isNotRefuse, String reason, String username);

    void doSomeThingg(Integer receiverid, Integer senderid, Integer id, Integer isNotRefuse, String reason, String username);

    List<PaiGongGuanLiPaiGongDanEntity> findPaiGongDan(Integer page, Integer size, String paiGongNumber,
                                                       String startTime, String endTime, String xdName,
                                                       String czName, String renWuShuXing, Integer peopeleId, Integer paiGongState);

    List<PaiGongGuanLiBusinessTypeEntity> findAllBuiness();

    List<PaiGongGuanLiBusinessTypeEntity> findBuinessById(Integer id);

    List<PaiGongGuanLiPaiGongDanEntity> findAllPaiGongDan();

    List<PaiGongGuanLiPaiGongDanRecordMessageEntity> findRecordMessageByid(Integer id);

    List<MessageEntity> findXiaoXi(Integer senderid);

    PaiGongGuanLiPaiGongDanEntity findPaiGongDanByid(Integer id);

    long findWorkOrderSum();

    void editTaskSignById(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity,String username,Integer userid);

    void editGaiPiaUserById(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username, Integer senderid);

    List<PaiGongGuanLiUserEntity> findUserBySomething(String xiangMuType, String reWuShuXing,String chuChaiType,String yeWuType,String startTime,String endTime);

    List<PaiGongGuanLiPaiGongDanEntity> findAllPaiGongOnHome(Integer page, Integer size);

    List<Integer> findChuChaiPeopleing();

    void updateUserOtherstate(String today, Integer pid);

    void quXiaoPaiGong(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, Integer userid, String username);
}
