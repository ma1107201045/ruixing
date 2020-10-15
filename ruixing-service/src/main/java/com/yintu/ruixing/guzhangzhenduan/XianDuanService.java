package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 线段
 */
public interface XianDuanService {
    void addXianDuan(XianDuanEntity xianDuanEntity,Long[] dwdids,Long[] dids);

    void delXianDuan(Long xid);

    void editXianDuan(XianDuanEntity xianDuanEntity);

    XianDuanEntity findXianDuanById(Long xid);

    List<Integer> findId(Long xid);

    List<XianDuanEntity> findAllJsonByDid(Integer did);
}
