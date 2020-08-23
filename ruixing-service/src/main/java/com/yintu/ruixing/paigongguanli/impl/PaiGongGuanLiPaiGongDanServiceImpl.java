package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.paigongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:32
 * @Version 1.0
 * 需求: 派工单
 */
@Service
@Transactional
public class PaiGongGuanLiPaiGongDanServiceImpl implements PaiGongGuanLiPaiGongDanService {
    @Autowired
    private PaiGongGuanLiPaiGongDanDao paiGongGuanLiPaiGongDanDao;

    @Autowired
    private PaiGongGuanLiTaskDao paiGongGuanLiTaskDao;

    @Autowired
    private PaiGongGuanLiTaskUserDao paiGongGuanLiTaskUserDao;

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum) {
        return paiGongGuanLiPaiGongDanDao.findOnePaiGongDanByNum(paiGongDanNum);
    }

    @Override
    public void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity) {
        Integer maxTaskshuxingNum=null;
        Integer minTaskshuxingNum=null;
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要且紧急") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要不紧急")) {
             maxTaskshuxingNum=90;
             minTaskshuxingNum=80;
        }
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("紧急不重要") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("不重要不紧急")) {
             maxTaskshuxingNum=70;
             minTaskshuxingNum=60;
        }
        if (paiGongGuanLiPaiGongDanEntity.getPaigongmode() == 1) { //手动派工
            paiGongGuanLiPaiGongDanDao.insertSelective(paiGongGuanLiPaiGongDanEntity);
        } else { //自动派工
            Integer paigongpeoplenumber = paiGongGuanLiPaiGongDanEntity.getPaigongpeoplenumber();
            for (Integer i = 0; i < paigongpeoplenumber; i++) {
                String xiangmutype = paiGongGuanLiPaiGongDanEntity.getXiangmutype();
                String yewutype = paiGongGuanLiPaiGongDanEntity.getYewutype();
                String chuchaitype = paiGongGuanLiPaiGongDanEntity.getChuchaitype();
                String renwu = xiangmutype + yewutype + "——" + chuchaitype;
                Integer tid = paiGongGuanLiTaskDao.findTid(renwu);
                List<PaiGongGuanLiTaskUserEntity>userEntityList=paiGongGuanLiTaskUserDao.findUser(tid,maxTaskshuxingNum,minTaskshuxingNum);
                for (PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity : userEntityList) {
                    String truename = paiGongGuanLiTaskUserEntity.getTruename();
                    List<PaiGongGuanLiTaskUserEntity>userList=paiGongGuanLiPaiGongDanDao.findUserByName(truename);
                    if (userList.size()==0){//说明这个人咩有出过差

                    }else {

                    }
                }
                long time = paiGongGuanLiPaiGongDanEntity.getChuchaistarttime().getTime();
            }
        }
    }

    @Override
    public String findPaiGongDanNum(String suoxie) {
        return paiGongGuanLiPaiGongDanDao.findPaiGongDanNum(suoxie);
    }
}
