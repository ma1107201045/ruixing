package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainFileDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiTrainService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/4 10:44
 * @Version 1.0
 * 需求:安装调试 培训管理
 */
@Service
@Transactional
public class AnZhuangTiaoShiTrainServiceImpl implements AnZhuangTiaoShiTrainService {
    @Autowired
    private AnZhuangTiaoShiTrainDao anZhuangTiaoShiTrainDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuDao anZhuangTiaoShiXiangMuDao;

    @Autowired
    private AnZhuangTiaoShiTrainFileDao anZhuangTiaoShiTrainFileDao;

    @Override
    public List<AnZhuangTiaoShiTrainEntity> findTrainByid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiTrainDao.findTrainByid(id);
    }

    @Override
    public List<AnZhuangTiaoShiTrainEntity> findAllTrainByType(Integer typeId, Integer page, Integer size) {
        return anZhuangTiaoShiTrainDao.findTrainBytraintype(typeId);
    }

    @Override
    public List<TreeNodeUtil> findReJiShu() {
        List<AnZhuangTiaoShiTrainEntity> trainEntityList=anZhuangTiaoShiTrainDao.findReJiShu();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (AnZhuangTiaoShiTrainEntity trainEntity : trainEntityList) {
            //第一级
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            //treeNodeUtil.setId(trainEntity.getId().longValue());
            treeNodeUtil.setLabel(trainEntity.getTraintype().toString());
            List<AnZhuangTiaoShiTrainEntity> trainEntities=anZhuangTiaoShiTrainDao.findTrainBytraintype(trainEntity.getTraintype());
            List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
            for (AnZhuangTiaoShiTrainEntity shiTrainEntity : trainEntities) {
                //第二级
                TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                treeNodeUtil1.setId(shiTrainEntity.getId().longValue());
                treeNodeUtil1.setLabel(shiTrainEntity.getCustomer());
                treeNodeUtilss.add(treeNodeUtil1);
                treeNodeUtil.setChildren(treeNodeUtilss);
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        Integer d = 0;
        for (TreeNodeUtil nodeUtil : treeNodeUtils) {
            if (nodeUtil.getLabel().equals("1")) {
                a++;
            }
            if (nodeUtil.getLabel().equals("2")) {
                b++;
            }
            if (nodeUtil.getLabel().equals("3")) {
                c++;
            }
            if (nodeUtil.getLabel().equals("4")) {
                d++;
            }
        }

        if (a == 0) {
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setLabel("施工单位");
            treeNodeUtils.add(0, treeNodeUtil1);
        }
        if (b == 0) {
            TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
            treeNodeUtil2.setLabel("设计单位");
            treeNodeUtils.add(1, treeNodeUtil2);
        }
        if (c == 0) {
            TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
            treeNodeUtil3.setLabel("运用维护单位");
            treeNodeUtils.add(2, treeNodeUtil3);
        }
        if (d == 0) {
            TreeNodeUtil treeNodeUtil4 = new TreeNodeUtil();
            treeNodeUtil4.setLabel("其他单位");
            treeNodeUtils.add(3, treeNodeUtil4);
        }
        return treeNodeUtils;
    }

    @Override
    public List<AnZhuangTiaoShiTrainFileEntity> findTrainFile(Integer id) {
        return anZhuangTiaoShiTrainFileDao.findTrainFile(id);
    }

    @Override
    public void deleteTrainFilesByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            anZhuangTiaoShiTrainFileDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public List<AnZhuangTiaoShiTrainFileEntity> findAllTrainFiles(Integer id,Integer page, Integer size, String filename) {
        return anZhuangTiaoShiTrainFileDao.findAllTrainFiles(id,filename);
    }

    @Override
    public AnZhuangTiaoShiTrainFileEntity findById(Integer id) {
        return anZhuangTiaoShiTrainFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void addFile(AnZhuangTiaoShiTrainFileEntity anZhuangTiaoShiFileEntity) {
        anZhuangTiaoShiTrainFileDao.insertSelective(anZhuangTiaoShiFileEntity);
    }

    @Override
    public void deleteTrainByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            anZhuangTiaoShiTrainDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public List<AnZhuangTiaoShiTrainEntity> findAllTrain(Integer page, Integer size, String xdName, String customer) {
        return anZhuangTiaoShiTrainDao.findAllTrain(xdName,customer);
    }

    @Override
    public void editTrainById(AnZhuangTiaoShiTrainEntity anZhuangTiaoShiTrainEntity,String username) {
        anZhuangTiaoShiTrainEntity.setUpdatename(username);
        anZhuangTiaoShiTrainEntity.setUpdatetime(new Date());
        anZhuangTiaoShiTrainDao.updateByPrimaryKeySelective(anZhuangTiaoShiTrainEntity);
    }

    @Override
    public void addTrain(AnZhuangTiaoShiTrainEntity anZhuangTiaoShiTrainEntity,String username) {
        anZhuangTiaoShiTrainEntity.setCreatename(username);
        anZhuangTiaoShiTrainEntity.setCreatetime(new Date());
        anZhuangTiaoShiTrainDao.insertSelective(anZhuangTiaoShiTrainEntity);
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuan() {
        return anZhuangTiaoShiXiangMuDao.findXianDuan();
    }
}
