package com.yintu.ruixing.yunxingweihu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanInfoEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/9 16:40
 */
public interface MaintenancePlanInfoService extends BaseService<MaintenancePlanInfoEntity, Integer> {


    /**
     * 批量添加
     *
     * @param maintenancePlanInfoEntities 维护计划详情实体类集
     */
    void add(List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);

    /**
     * @param ids               维护计划详情id
     * @param maintenancePlanId 维护计划id
     * @param date              当前日期
     * @return
     */
    List<MaintenancePlanInfoEntity> findByCondition(Integer[] ids, Integer maintenancePlanId, Date date);


    /**
     * 批量导入excel数据
     *
     * @param inputStream 输入流
     */
    String[][] importFile(InputStream inputStream, String fileName) throws IOException;


    /**
     * 批量导入excel数据
     *
     * @param maintenancePlanId 维护计划id
     */
    void importData(Integer maintenancePlanId, String[][] context, String loginUsername);


    /**
     * 下载excel数据模板
     *
     * @param outputStream 输出流
     * @throws IOException io异常
     */
    void templateFile(OutputStream outputStream) throws IOException;

    /**
     * 批量导出excel数据
     *
     * @param outputStream 输出流
     * @param ids          维护计划详情id集
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;


}
