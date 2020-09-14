package com.yintu.ruixing.anzhuangtiaoshi;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:36
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiXiangMuServiceChooseService {
    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findAllByXDid(Integer xdid, Integer page, Integer size);
}
