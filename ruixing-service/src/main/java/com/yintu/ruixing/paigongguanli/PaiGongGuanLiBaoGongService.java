package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/10/9 11:07
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiBaoGongService {
    void addBaoGong(List<PaiGongGuanLiBaoGongEntity> paiGongGuanLiBaoGongEntity, String username, Integer senderid);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(Integer page, Integer size, Integer senderid, Date datetime);

    void editBaoGongById(String username, Integer senderid, PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity);

    void deleteBaoGongByIds(Integer[] ids);

    List<PaiGongGuanLiRiQinEntity> findPropleAddress(Integer page, Integer size);

    List<PaiGongGuanLiRiQinEntity> findPeopleAddressOnMap( );

    List<PaiGongGuanLiPaiGongDanEntity> findAllNotOverPaiGong(Integer userid);

    List<XianDuanEntity> findAllXianDuan();

    void addBaoGongDan(JSONArray baoGongDatas, String username, Integer senderid);

    void addFile(PaiGongGuanLiBaoGongFileEntity paiGongGuanLiBaoGongFileEntity);

    void deleteFileByIds(Integer[] ids);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGong(Integer baoGongType);

    List<PaiGongGuanLiBaoGongEntity> findBaoGongBySomethings(String startTime, String endTime, Integer userid, String xianDuan, Integer isNotClose,Integer baoGongType);

    List<PaiGongGuanLiUserEntity> findBaoGongUser(Integer baoGongType);

    List<PaiGongGuanLiBaoGongFileEntity> findFileByBid(Integer bid, Integer baoGongType);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongAndAllComment(Integer userid, String riqi);

    List<PaiGongGuanLiBaoGongEntity> findAllChuChaiPeopele(String lastDateTime, String nowDateTime);
}
