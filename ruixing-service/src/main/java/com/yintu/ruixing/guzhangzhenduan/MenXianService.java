package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author:mlf
 * @date:2020/6/12 11:39
 */
public interface MenXianService {

    /**
     * 添加门限参数信息
     *
     * @param menXianEntity 门限参数信息
     */
    void add(MenXianEntity menXianEntity);


    /**
     * 按照id删除门限参数
     *
     * @param id 门限参数id
     */
    void remove(Integer id);

    /**
     * 修改门限参数信息
     *
     * @param menXianEntity 门限参数信息
     */
    void edit(MenXianEntity menXianEntity);

    /**
     * @param id 门限参数id
     * @return 门限参数信息
     */
    MenXianEntity findById(Integer id);

    /**
     * @param czId       车站id
     * @param quanduanId 区段id
     * @param propertyId 属性id
     * @return
     */
    MenXianEntity findByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quanduanId, Integer propertyId);

    /**
     * @param properties 属性集合
     * @param czId       车站id
     * @return
     */
    JSONObject findByCzIdAndProperties(Integer czId, Integer[] properties);

    /**
     * 批量导入excel数据
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 解析后的数据
     * @throws IOException io异常
     */
    String[][] importFile(InputStream inputStream, String fileName) throws IOException;

    /**
     * 添加预览修改过的数据
     *
     * @param context 文件解析的修改数据
     */
    void importData(String[][] context, String loginUserName);

    /**
     * 下载excel数据模板
     *
     * @param outputStream 输出流
     * @throws IOException io异常
     */
    void templateFile(OutputStream outputStream) throws IOException;


    String findMaxNumber(Integer czid, Integer qdid, Integer mid);

    String findMinNumber(Integer czid, Integer qdid, Integer mid);
}
