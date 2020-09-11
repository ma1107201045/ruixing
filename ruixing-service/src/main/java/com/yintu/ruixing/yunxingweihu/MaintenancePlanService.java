package com.yintu.ruixing.yunxingweihu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/8 15:50
 */
public interface MaintenancePlanService extends BaseService<MaintenancePlanEntity, Integer> {


    /**
     * 批量添加
     *
     * @param maintenancePlanEntities 维护计划实体类集
     */
    void batchAdd(List<MaintenancePlanEntity> maintenancePlanEntities);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);


    /**
     * 多条件查询
     *
     * @param ids  id集合
     * @param name 名称
     * @return 维修计划信息集
     */
    List<MaintenancePlanEntity> findByExample(Integer[] ids, String name);


    /**
     * 批量导入excel数据
     *
     * @param inputStream 输入流
     */
    String[][] importFile(InputStream inputStream, String fileName) throws IOException;

    /**
     * 批量导入excel数据
     *
     * @param context 数据
     */
    void importDate(String[][] context, String loginUsername);

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
     * @param ids          id集合
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;


}
