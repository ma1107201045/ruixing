package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.util.TreeNodeUtil;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 10:43
 * @Version 1.0
 * 需求:安装调试模块
 */
public interface AnZhuangTiaoShiXiangMuService {
    List<TreeNodeUtil> findSanJiShu();

    void addSanJiShuXiangMu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity) throws Exception;

    void editSanJiShu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity);

    void deletSanJiShuById(Integer[] ids);

    List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAllXiangMuType();

    List<ChanPinJiaoFuXiangMuFileEntity> findXiangMuAndBianHao();

    AnZhuangTiaoShiFileEntity findById(Integer id);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanDataByLeiXing(Integer leiXingId, Integer page, Integer size);

    List<AnZhuangTiaoShiXiangMuEntity> findXianDuanNameAndYear();

    JSONObject findXianDuanBySomedata(Integer num, Integer size, String xdname, String year, String xdtype, Integer xdleixing);

    JSONArray findXianDuanBySomedatas(Integer[] ids);

    Integer findCheZhanTotal(Integer id);

    void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException;

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findLastMonthXiangMu(String today, String lastMothDay,Integer page, Integer size);

    List<MessageEntity> findXiaoXi(Integer senderid);

    void exportStatisticalFile(OutputStream outputStream, Integer[] ids) throws IOException;

    long findProjectSum();

    List<AnZhuangTiaoShiXiangMuServiceChooseEntity> findNextMonthXiangMu(String today, String nextMothDay,Integer page, Integer size);

    List<AnZhuangTiaoShiXiangMuEntity> findOneCheZhanByXianDuanId(Integer xianDuanId);
}
