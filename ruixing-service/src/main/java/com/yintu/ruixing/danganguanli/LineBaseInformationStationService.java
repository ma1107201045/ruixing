package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:07
 * @Version: 1.0
 */
public interface LineBaseInformationStationService extends BaseService<LineBaseInformationStationEntity, Integer> {
    void add(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds);

    void edit(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds);


    List<LineBaseInformationStationEntity> findByExample(Integer lineBaseInformationId, Integer id, String name, Integer[] ids);

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
     * @param list 数据
     */
    void importDate(Integer lineBaseInformationId, List<List<String>> list, String loginUsername);

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
