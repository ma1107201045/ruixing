package com.yintu.ruixing.paigongguanli;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/10/9 11:07
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiBaoGongService {
    void addBaoGong(PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity, String username, Integer senderid);

    List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(Integer page, Integer size, Integer senderid, Date datetime);

    void editBaoGongById(String username, Integer senderid, PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity);

    void deleteBaoGongByIds(Integer[] ids);

    List<PaiGongGuanLiRiQinEntity> findPropleAddress(Integer page, Integer size);

    List<PaiGongGuanLiRiQinEntity> findPeopleAddressOnMap(Integer page, Integer size);
}
