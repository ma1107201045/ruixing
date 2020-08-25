package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.paigongguanli.*;
import com.yintu.ruixing.xitongguanli.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private PaiGongGuanLiRiQinDao paiGongGuanLiRiQinDao;

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum) {
        return paiGongGuanLiPaiGongDanDao.findOnePaiGongDanByNum(paiGongDanNum);
    }

    @Override
    public void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity) {
        Date today = new Date();//获取今天日期
        List<String> userlist = new ArrayList<>();//人员名集合
        Integer maxTaskshuxingNum = null;
        Integer minTaskshuxingNum = null;
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要且紧急") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要不紧急")) {
            maxTaskshuxingNum = 90;
            minTaskshuxingNum = 80;
        }
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("紧急不重要") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("不重要不紧急")) {
            maxTaskshuxingNum = 70;
            minTaskshuxingNum = 60;
        }
        if (paiGongGuanLiPaiGongDanEntity.getPaigongmode() == 1) { //手动派工
            paiGongGuanLiPaiGongDanDao.insertSelective(paiGongGuanLiPaiGongDanEntity);

        } else { //自动派工
            Integer paigongpeoplenumber = paiGongGuanLiPaiGongDanEntity.getPaigongpeoplenumber();//自动派工人数

            String xiangmutype = paiGongGuanLiPaiGongDanEntity.getXiangmutype();
            String yewutype = paiGongGuanLiPaiGongDanEntity.getYewutype();
            String chuchaitype = paiGongGuanLiPaiGongDanEntity.getChuchaitype();
            String renwu = xiangmutype + yewutype + "——" + chuchaitype;
            Integer tid = paiGongGuanLiTaskDao.findTid(renwu);
            //根据任务id  和分值范围  查询对应的人员  并按照分值大小排列
            List<PaiGongGuanLiTaskUserEntity> userEntityList = paiGongGuanLiTaskUserDao.findUser(tid, maxTaskshuxingNum, minTaskshuxingNum);
            for (PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity : userEntityList) {//遍历每个人员
                String truename = paiGongGuanLiTaskUserEntity.getTruename();//获取人员名
                Integer uid = userDao.findid(truename);
                String userdongtai = paiGongGuanLiRiQinDao.findUserDongTai(uid);//查询这个人的出差  请假情况
                if (!userdongtai.equals("请假") || !userdongtai.equals("出差")) {
                    List<PaiGongGuanLiPaiGongDanEntity> userList = paiGongGuanLiPaiGongDanDao.findUserByName(truename);//根据人员名  查询是否派单 或者是否是改派人员
                    if (userList.size() == 0) { //说明这个人咩有出过差
                        userlist.add(truename); //把这个人放到list集合里
                    } else {//这个人出过差
                        for (PaiGongGuanLiPaiGongDanEntity gongGuanLiPaiGongDanEntity : userList) {//遍历这个人的出差情况  由时间从前往后排序
                            long Chuchaiendtime = gongGuanLiPaiGongDanEntity.getChuchaiendtime().getTime();//获得本次出差的结束时间
                            String paigongpeople = gongGuanLiPaiGongDanEntity.getPaigongpeople();//得到此人的名称
                            if (Chuchaiendtime < paiGongGuanLiPaiGongDanEntity.getChuchaistarttime().getTime()) {//说明此人 能派遣
                                userlist.add(paigongpeople);
                            }
                        }
                    }
                }
            }

            //遍历符合条件的人员userlist
            if (userlist.size() != 0) {
                double lianxutime = 0;
                for (int i = 0; i < userlist.size(); i++) {
                    String username = userlist.get(i);//得到人员名
                    List<PaiGongGuanLiPaiGongDanEntity> userList = paiGongGuanLiPaiGongDanDao.findUserByName(username);//根据人员名  查询此人的派工经历
                    if (userList.size() == 1) {
                        long endtime = userList.get(0).getChuchaiendtime().getTime();
                        long starttime = userList.get(0).getChuchaistarttime().getTime();
                        lianxutime = endtime - starttime;
                    }
                    if (userList.size() > 1) {
                        long endtime1 = userList.get(0).getChuchaiendtime().getTime();
                        long starttime1 = userList.get(0).getChuchaistarttime().getTime();

                        long endtime2 = userList.get(1).getChuchaiendtime().getTime();
                        long starttime2 = userList.get(1).getChuchaistarttime().getTime();

                        long lianxutime1 = starttime1 - endtime2;
                        long dayNum = lianxutime1 / 86400000;//间隔天数
                        if (dayNum < 3) {
                            lianxutime = (endtime1 - starttime1) + (endtime2 - starttime2);
                        }
                        if (3 <= dayNum && dayNum < 6) {
                            lianxutime = (endtime1 - starttime1) + (endtime2 - starttime2) * 0.6;
                        }
                        if (6 <= dayNum && dayNum < 10) {
                            lianxutime = (endtime1 - starttime1) + (endtime2 - starttime2) * 0.3;
                        }
                        if (10 <= dayNum) {
                            lianxutime = (endtime1 - starttime1);
                        }
                    }
                }

            }
        }
    }

    @Override
    public String findPaiGongDanNum(String suoxie) {
        return paiGongGuanLiPaiGongDanDao.findPaiGongDanNum(suoxie);
    }


    public static void main(String[] args) {
        int[] a={1,2,9,6,5};
        for (int i = 0; i < a.length; i++) {
            for (int i1 = i+1; i1 < a.length; i1++) {
                if (a[i]>a[i1]){
                    int temp = a[i];

                    a[i] = a[i1];

                    a[i1] = temp;
                }
            }
        }
    }

}
