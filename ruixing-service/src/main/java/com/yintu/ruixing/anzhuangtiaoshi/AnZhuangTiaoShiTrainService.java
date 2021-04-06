package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuEntity;
import com.yintu.ruixing.common.util.TreeNodeUtil;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/4 10:45
 * @Version 1.0
 * 需求:安装调试 培训管理
 */
public interface AnZhuangTiaoShiTrainService {
    List<AnZhuangTiaoShiXiangMuEntity> findXianDuan();

    void addTrain(AnZhuangTiaoShiTrainEntity anZhuangTiaoShiTrainEntity,String username);

    void editTrainById(AnZhuangTiaoShiTrainEntity anZhuangTiaoShiTrainEntity,String username);

    List<AnZhuangTiaoShiTrainEntity> findAllTrain(Integer page, Integer size, String xdName, String customer);

    void deleteTrainByIds(Integer[] ids);

    void addFile(AnZhuangTiaoShiTrainFileEntity anZhuangTiaoShiFileEntity);

    AnZhuangTiaoShiTrainFileEntity findById(Integer id);

    List<AnZhuangTiaoShiTrainFileEntity> findAllTrainFiles(Integer id,Integer page, Integer size, String filename);

    void deleteTrainFilesByIds(Integer[] ids);

    List<AnZhuangTiaoShiTrainFileEntity> findTrainFile(Integer id);

    List<TreeNodeUtil> findReJiShu();

    List<AnZhuangTiaoShiTrainEntity> findAllTrainByType(String xdName, Integer page, Integer size);

    List<AnZhuangTiaoShiTrainEntity> findTrainByid(Integer id);
}
