package com.yintu.ruixing.yunxingweihu;

import com.yintu.ruixing.common.util.BaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public interface SparePartsInfoService extends BaseService<SparePartsInfoEntity, Integer> {


    /**
     * 批量添加
     *
     * @param sparePartsInfoEntities 备品试验详情实体类集
     */
    void add(List<SparePartsInfoEntity> sparePartsInfoEntities);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);

    /**
     * @param ids          备品试验详情id
     * @param context      维护内容
     * @param sparePartsId 备品试验id
     * @param date         当前日期
     * @return
     */
    List<SparePartsInfoEntity> findByCondition(Integer[] ids, String context, Integer sparePartsId, Date date);


    /**
     * 批量导入excel数据
     *
     * @param inputStream 输入流
     */
    String[][] importFile(InputStream inputStream, String fileName) throws IOException;


    /**
     * 批量导入excel数据
     *
     * @param sparePartsId 备品试验id
     */
    void importData(Integer sparePartsId, String[][] context, String loginUsername);


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
     * @param ids          备品试验详情id集
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;


}
