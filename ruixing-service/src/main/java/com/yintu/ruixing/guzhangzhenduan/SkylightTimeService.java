package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.common.util.BaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/18 17:54
 */
public interface SkylightTimeService extends BaseService<SkylightTimeEntity, Integer> {


    void add(SkylightTimeEntity entity, Integer[] qdIds);

    void remove(Integer[] ids);

    SkylightTimeEntity findByCzIdAndQdId(Integer czId, Integer qdId);

    List<SkylightTimeEntity> findByCondition(Integer id, Date startTime, Date endTime, Integer czId);

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

    /**
     * 是否是天窗时间
     *
     * @param czId 车站
     * @param qdId 区段id
     * @param time 时间
     * @return
     */
    boolean isSkylightTime(Integer czId, Integer qdId, Date time);

}
