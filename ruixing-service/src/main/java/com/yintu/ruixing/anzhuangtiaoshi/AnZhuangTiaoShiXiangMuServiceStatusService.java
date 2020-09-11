package com.yintu.ruixing.anzhuangtiaoshi;

/**
 * @Author Mr.liu
 * @Date 2020/9/11 18:55
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiXiangMuServiceStatusService {
    void addServiceStatus(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username);

    void editServiceStatusById(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity, String username);
}
