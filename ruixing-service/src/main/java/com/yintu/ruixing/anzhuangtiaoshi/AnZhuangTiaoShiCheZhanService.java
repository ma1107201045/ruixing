package com.yintu.ruixing.anzhuangtiaoshi;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 16:48
 * @Version 1.0
 * 需求:安装调试的车站
 */
public interface AnZhuangTiaoShiCheZhanService {
    void addCheZhan(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanById(Integer id, Integer page, Integer size);


    void editCheZhanById(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity);

    void deleteCheZhanByIds(Integer[] ids);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByName(String czname, Integer page, Integer size);

    void exportFile(ServletOutputStream outputStream, Integer[] ids)throws IOException;
}
