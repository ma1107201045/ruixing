package com.yintu.ruixing.paigongguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiRiQinDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinService;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:11
 * @Version 1.0
 * 需求: 派工管理  日勤
 */
@Service
@Transactional
public class PaiGongGuanLiRiQinServiceImpl implements PaiGongGuanLiRiQinService {
    @Autowired
    private PaiGongGuanLiRiQinDao paiGongGuanLiRiQinDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<PaiGongGuanLiRiQinEntity> findAllRiQinDatas(Integer page, Integer size) {
        List<PaiGongGuanLiRiQinEntity> riQinEntityList = paiGongGuanLiRiQinDao.findAllRiQinDatas();
        for (PaiGongGuanLiRiQinEntity riQinEntity : riQinEntityList) {
            JSONObject js = new JSONObject();
            Integer uid = riQinEntity.getUid();
            List<PaiGongGuanLiRiQinEntity> riQinEntities = paiGongGuanLiRiQinDao.findRiQinByUid(uid);
            List<Integer> list = new ArrayList();
            for (PaiGongGuanLiRiQinEntity qinEntity : riQinEntities) {
                Date starttime = qinEntity.getStarttime();
                int date = starttime.getDate();
                //对存在日期的数据 添加
                for (int i = 1; i <= 31; i++) {
                    if (date == i) {
                        PaiGongGuanLiRiQinEntity riQinEntity1 = new PaiGongGuanLiRiQinEntity();
                        Integer userdongtai = qinEntity.getUserdongtai();
                        riQinEntity1.setUserdongtai(userdongtai);
                        js.put("a" + i, riQinEntity1);
                    }
                }
                list.add(date);
            }

            //对日期值去重
            List<Integer> daylist = new ArrayList();
            for (int i = 1; i <= 31; i++) {
                daylist.add(i);
            }
            List<Integer> datalist = new ArrayList();
            datalist.addAll(daylist);
            datalist.removeAll(list);

            //获得不存在数据的 日期值
            for (Integer date : datalist) {
                PaiGongGuanLiRiQinEntity riQinEntity1 = new PaiGongGuanLiRiQinEntity();
                riQinEntity1.setUserdongtai(0);
                js.put("a" + date, riQinEntity1);
            }
            riQinEntity.setStatus(js);
        }
        return riQinEntityList;
    }

    @Override
    public List<PaiGongGuanLiRiQinEntity> findAllRiQinByUid(Integer uid) {
        return paiGongGuanLiRiQinDao.findAllRiQinByUid(uid);
    }

    @Override
    public void editRiQinById(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity) {
        paiGongGuanLiRiQinEntity.setUpdatetime(new Date());
        paiGongGuanLiRiQinDao.updateByPrimaryKeySelective(paiGongGuanLiRiQinEntity);
    }

    @Override
    public void addRiQin(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity, Integer senderid) {
        paiGongGuanLiRiQinEntity.setCreattime(new Date());
        paiGongGuanLiRiQinEntity.setUid(senderid);
        Date starttime = paiGongGuanLiRiQinEntity.getStarttime();
        PaiGongGuanLiRiQinEntity riQinEntity = paiGongGuanLiRiQinDao.findRiQin(senderid, starttime);
        if (riQinEntity == null) {
            paiGongGuanLiRiQinDao.insertSelective(paiGongGuanLiRiQinEntity);
        } else {
            paiGongGuanLiRiQinDao.deleteRiQin(starttime);
            paiGongGuanLiRiQinDao.insertSelective(paiGongGuanLiRiQinEntity);
        }
    }

    @Override
    public List<PaiGongGuanLiRiQinEntity> findAllRiQinByUserName(Integer page, Integer size, String username) {
        List<PaiGongGuanLiRiQinEntity> riQinList = new ArrayList<>();
        List<Long> uid = userDao.findId(username);
        if (uid.size() == 0) {
            return null;
        } else {
            for (Long aLong : uid) {
                PaiGongGuanLiRiQinEntity riQinEntityList = paiGongGuanLiRiQinDao.selectByPrimaryKey(aLong.intValue());
                UserEntity userEntity = userDao.selectByPrimaryKey(aLong);
                String trueName = userEntity.getTrueName();
                String danwei = "";//userEntity.getCustomerUnitsEntity().getName();
                String bumen = "";//userEntity.getDepartmentEntities().get(0).getName();
                String zhiwei = "";//userEntity.getCustomerDutyEntity().getName();
                riQinEntityList.setUsername(trueName);
                riQinEntityList.setDanwei(danwei);
                riQinEntityList.setBumen(bumen);
                riQinEntityList.setZhiwei(zhiwei);
                riQinList.add(riQinEntityList);
            }
        }
        return riQinList;
    }

    @Override
    public List<PaiGongGuanLiRiQinEntity> findAllRiQin(Integer page, Integer size) {
        List<PaiGongGuanLiRiQinEntity> riQinEntityList = paiGongGuanLiRiQinDao.findAllRiQin();
        for (PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity : riQinEntityList) {
            Integer uid = paiGongGuanLiRiQinEntity.getUid();
            UserEntity userEntity = userDao.selectByPrimaryKey((long) uid);
            String trueName = userEntity.getTrueName();
            String danwei = "";//userEntity.getCustomerUnitsEntity().getName();
            // departmentEntities  customerDutyEntity  roleEntities
            String bumen = userEntity.getDepartmentEntities().get(0).getName();
            String zhiwei = "";//userEntity.getCustomerDutyEntity().getName();
            paiGongGuanLiRiQinEntity.setUsername(trueName);
            paiGongGuanLiRiQinEntity.setDanwei(danwei);
            paiGongGuanLiRiQinEntity.setBumen(bumen);
            paiGongGuanLiRiQinEntity.setZhiwei(zhiwei);
        }
        return riQinEntityList;
    }
}
