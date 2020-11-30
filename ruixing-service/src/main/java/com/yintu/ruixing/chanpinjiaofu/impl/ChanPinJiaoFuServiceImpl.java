package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuPropertyEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/6/23 16:33
 * @Version 1.0
 * 需求:产品交付模块
 */
@Service
@Transactional
public class ChanPinJiaoFuServiceImpl implements ChanPinJiaoFuService {
    @Autowired
    private ChanPinJiaoFuDao chanPinJiaoFuDao;

    @Override
    public Integer findAllDataShu() {
        return chanPinJiaoFuDao.findAllDataShu();
    }

    @Override
    public List<Integer> findParedId(Integer id) {
        return chanPinJiaoFuDao.findParedId(id);
    }


    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuDataByIdThird(Integer id, Integer page, Integer size) {
        return chanPinJiaoFuDao.findXiangMuDataByIdThird(id);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuDataByIdSecond(Integer id, Integer page, Integer size) {
        return chanPinJiaoFuDao.findXiangMuDataByIdSecond(id);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuDataByIdFirst(Integer id, Integer page, Integer size) {
        return chanPinJiaoFuDao.findXiangMuDataByIdFirst(id);
    }

    @Override
    public void deletXiangMuDataByIds(Integer[] ids) {
        chanPinJiaoFuDao.deletXiangMuDataByIds(ids);
    }

    @Override
    public void deletXiangMuDataById(Integer id) {
        chanPinJiaoFuDao.deletXiangMuDataById(id);
    }

    @Override
    public void editXiangMuDataById(ChanPinJiaoFuEntity chanPinJiaoFuEntity) {
        chanPinJiaoFuDao.editXiangMuDataById(chanPinJiaoFuEntity);
    }

    @Override
    public void addXiangMuData(ChanPinJiaoFuEntity chanPinJiaoFuEntity) {
        chanPinJiaoFuDao.addXiangMuData(chanPinJiaoFuEntity);
    }

    @Override
    public ChanPinJiaoFuEntity findById(Integer id) {
        return chanPinJiaoFuDao.findById(id);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuDataByIds(Integer firstid, Integer secondid, Integer fileid, Integer page, Integer size) {
        return chanPinJiaoFuDao.findXiangMuDataByIds(firstid,secondid,fileid);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuDataById(Integer idd) {
        return chanPinJiaoFuDao.findXiangMuDataById(idd);
    }

    @Override
    public List<Integer> findIdS(Integer id) {
        return chanPinJiaoFuDao.findIdS(id);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findXiangMuData(String bianhao, String name, Integer page, Integer size) {
        return chanPinJiaoFuDao.findXiangMuData(bianhao,name);
    }

    @Override
    public List<ChanPinJiaoFuEntity> findAllXiangMuState(Integer page,Integer size) {
        return chanPinJiaoFuDao.findAllXiangMuState();
    }

    @Override
    public void deleteDataShuById(Integer id) {
        chanPinJiaoFuDao.deleteDataShuById(id);
    }

    @Override
    public void editDataShuById(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity) {
        chanPinJiaoFuDao.editDataShuById(chanPinJiaoFuPropertyEntity);
    }

    @Override
    public List<TreeNodeUtil> findChanPinJiaoFuShuXing(int i) {
        List<ChanPinJiaoFuPropertyEntity>chanPinJiaoFuPropertyEntities=chanPinJiaoFuDao.findChanPinJiaoFuShuXing(i);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity : chanPinJiaoFuPropertyEntities) {
            TreeNodeUtil treeNodeUtil=new TreeNodeUtil();
            treeNodeUtil.setId((long) chanPinJiaoFuPropertyEntity.getId());
            treeNodeUtil.setLabel(chanPinJiaoFuPropertyEntity.getName());
            Map<String,Object> map=new HashMap<>();
            map.put("nameType",(long) chanPinJiaoFuPropertyEntity.getNameType());
            map.put("parentId",(long) chanPinJiaoFuPropertyEntity.getParentId());
            treeNodeUtil.setA_attr(map);
            if (chanPinJiaoFuDao.findChanPinJiaoFuShuXing(chanPinJiaoFuPropertyEntity.getId()).size()>0){
                treeNodeUtil.setChildren(this.findChanPinJiaoFuShuXing(chanPinJiaoFuPropertyEntity.getId()));
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

    @Override
    public void addDataShu(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity) {
        chanPinJiaoFuDao.fristData(chanPinJiaoFuPropertyEntity);
    }

    @Override
    public void addfristData(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity) {
        chanPinJiaoFuPropertyEntity.setParentId(-1);
        chanPinJiaoFuDao.fristData(chanPinJiaoFuPropertyEntity);
    }
}
