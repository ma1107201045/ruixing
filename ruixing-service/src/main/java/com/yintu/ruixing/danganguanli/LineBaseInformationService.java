package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:42
 * @Version: 1.0
 */
public interface LineBaseInformationService extends BaseService<LineBaseInformationEntity, Integer> {

    void add(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    void edit(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    List<Map<String, Object>> findRailwaysBureauTid();

    List<Map<String, Object>> findByTid(Integer tid);

    List<Map<String, Object>> findStationById(Integer id);

    List<TreeNodeUtil> findTree();


    List<LineBaseInformationEntity> findNewLineByTid(Integer tid);


    List<LineBaseInformationEntity> findHistoryByExample(Integer tid, Integer id, String name, Integer[] ids);


    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);


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
