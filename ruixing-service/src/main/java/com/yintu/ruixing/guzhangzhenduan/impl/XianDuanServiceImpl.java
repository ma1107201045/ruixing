package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.guzhangzhenduan.AlarmTableDao;
import com.yintu.ruixing.master.guzhangzhenduan.CheZhanDao;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import com.yintu.ruixing.master.guzhangzhenduan.XianDuanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 线段的serviceimpl
 */
@Service
@Transactional
public class XianDuanServiceImpl implements XianDuanService {
    @Autowired
    private XianDuanDao xianDuanDao;

    @Autowired
    private CheZhanDao cheZhanDao;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;

    @Autowired
    private AlarmTableDao alarmTableDao;

    @Override
    public void addXianDuan(XianDuanEntity xianDuanEntity,Long[] dwdids,Long[] dids) {
        for (int i = 0; i < dids.length; i++) {
            for (int i1 = 0; i1 < dids.length; i1++) {
                xianDuanEntity.setXdState(0);
                xianDuanEntity.setDwdId(dwdids[i]);
                xianDuanEntity.setDwdXdId(dids[i]);
                if (i1==i){
                    xianDuanDao.addXianDuan(xianDuanEntity);
                }
            }
        }
    }

    @Override
    public void delXianDuan(Long xid) {
        xianDuanDao.delXianDuan(xid);
    }

    @Override
    public void editXianDuan(XianDuanEntity xianDuanEntity) {
        xianDuanDao.editXianDuan(xianDuanEntity);
    }

    @Override
    public XianDuanEntity findXianDuanById(Long xid) {
        return xianDuanDao.findXianDuanById(xid);
    }

    @Override
    public List<Integer> findId(Long xid) {
        return xianDuanDao.findId(xid);
    }

    @Override
    public List<XianDuanEntity> findAllJsonByDid(Integer did) {
        Date dayTime=new Date();
        List<XianDuanEntity> xianDuanEntityList=xianDuanDao.findAllJsonByDid(did);
        for (XianDuanEntity xianDuanEntity : xianDuanEntityList) {
            long xid = xianDuanEntity.getXid();
            int Xid=(int)xid;
            List<CheZhanEntity> cheZhanEntityList=cheZhanDao.findCheZhanDatasByXid(Xid);
            for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
                int czid = (int ) cheZhanEntity.getCzId();
                String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
                if ( quDuanInfoDaoV2.isTableExist(tableName)==1){
                    Integer alarmNumber = alarmTableDao.findAlarmNumber(czid);
                    cheZhanEntity.setAlarmNumber(alarmNumber);
                }else {
                    cheZhanEntity.setAlarmNumber(0);
                }
            }
            xianDuanEntity.setCheZhanEntities(cheZhanEntityList);
        }
        return xianDuanEntityList;
    }
}
