package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiBaoGongGuanLiDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongGuanLiEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongGuanLiService;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/27 15:50
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class PaiGongGuanLiBaoGongGuanLiServiceImpl implements PaiGongGuanLiBaoGongGuanLiService {
    @Autowired
    private PaiGongGuanLiBaoGongGuanLiDao paiGongGuanLiBaoGongGuanLiDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void deleteBaoGongByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            paiGongGuanLiBaoGongGuanLiDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public PaiGongGuanLiBaoGongGuanLiEntity findById(Integer id) {
        return paiGongGuanLiBaoGongGuanLiDao.selectByPrimaryKey(id);
    }

    @Override
    public List<PaiGongGuanLiBaoGongGuanLiEntity> findBaoGongBySome(Date daytime, String xiangMuName, String userName, Integer page, Integer size) {
        if (userName != null) {
            Integer uId = userDao.findid(userName);
            List<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityList = paiGongGuanLiBaoGongGuanLiDao.findBaoGongByThreeSome(daytime, uId, xiangMuName);
            for (PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity : baoGongGuanLiEntityList) {
                Integer uid = paiGongGuanLiBaoGongGuanLiEntity.getUid();
                UserEntity userEntity = userDao.selectByPrimaryKey((long) uid);
                String phone = userEntity.getPhone();
                String trueName = userEntity.getTrueName();
                String bumen = userEntity.getDepartmentEntities().get(0).getName();
                paiGongGuanLiBaoGongGuanLiEntity.setUsername(trueName);
                paiGongGuanLiBaoGongGuanLiEntity.setPhone(phone);
                paiGongGuanLiBaoGongGuanLiEntity.setBumen(bumen);
            }
            return baoGongGuanLiEntityList;
        } else {
            List<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityList = paiGongGuanLiBaoGongGuanLiDao.findBaoGongByTwoSome(daytime, xiangMuName);
            for (PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity : baoGongGuanLiEntityList) {
                Integer uid = paiGongGuanLiBaoGongGuanLiEntity.getUid();
                UserEntity userEntity = userDao.selectByPrimaryKey((long) uid);
                String phone = userEntity.getPhone();
                String trueName = userEntity.getTrueName();
                String bumen = userEntity.getDepartmentEntities().get(0).getName();
                paiGongGuanLiBaoGongGuanLiEntity.setUsername(trueName);
                paiGongGuanLiBaoGongGuanLiEntity.setPhone(phone);
                paiGongGuanLiBaoGongGuanLiEntity.setBumen(bumen);
            }
            return baoGongGuanLiEntityList;
        }
    }

    @Override
    public void editBaoGongById(PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity) {
        paiGongGuanLiBaoGongGuanLiEntity.setUpdattime(new Date());
        paiGongGuanLiBaoGongGuanLiDao.updateByPrimaryKeySelective(paiGongGuanLiBaoGongGuanLiEntity);
    }

    @Override
    public void addBaoGong(PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity) {
        paiGongGuanLiBaoGongGuanLiEntity.setCreattime(new Date());
        paiGongGuanLiBaoGongGuanLiDao.insertSelective(paiGongGuanLiBaoGongGuanLiEntity);
    }

    @Override
    public List<PaiGongGuanLiBaoGongGuanLiEntity> findAllBaoGong(Integer page, Integer size) {
        List<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityList = paiGongGuanLiBaoGongGuanLiDao.findAllBaoGong();
        for (PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity : baoGongGuanLiEntityList) {
            Integer uid = paiGongGuanLiBaoGongGuanLiEntity.getUid();
            UserEntity userEntity = userDao.selectByPrimaryKey((long) uid);
            String phone = userEntity.getPhone();
            String trueName = userEntity.getTrueName();
            String bumen = userEntity.getDepartmentEntities().get(0).getName();
            paiGongGuanLiBaoGongGuanLiEntity.setUsername(trueName);
            paiGongGuanLiBaoGongGuanLiEntity.setPhone(phone);
            paiGongGuanLiBaoGongGuanLiEntity.setBumen(bumen);
        }
        return baoGongGuanLiEntityList;
    }
}
