package com.yintu.ruixing.paigongguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiRiQinDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDaystateDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiRiQinService;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserDaystateEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserRiQinEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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
    @Autowired
    private PaiGongGuanLiUserDaystateDao paiGongGuanLiUserDaystateDao;

    @Override
    public JSONObject findAllPeopleRiQinDatas() {
        JSONObject js = new JSONObject();
        //表头数据

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String monthfirst = format.format(c.getTime());//本月第一天
        System.out.println("===============nowfirst:" + monthfirst);
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String monthlast = format.format(ca.getTime());//本月最后一天
        System.out.println("===============last:" + monthlast);
        //获取当月的天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<PaiGongGuanLiUserRiQinEntity> UserRiQin=new ArrayList<>();
        for (int i = 1; i <= actualMaximum; i++) {
            PaiGongGuanLiUserRiQinEntity userRiQinEntity=new PaiGongGuanLiUserRiQinEntity();
            userRiQinEntity.setId(i);
            userRiQinEntity.setRiqi(i+"日");
            UserRiQin.add(userRiQinEntity);
        }
        js.put("title",UserRiQin);
        System.out.println("title"+js);

        //获取数据
        List<PaiGongGuanLiUserDaystateEntity> AllUserDaystateEntityList=new ArrayList<>();
        List<PaiGongGuanLiUserDaystateEntity>userDaystateEntityList=paiGongGuanLiUserDaystateDao.findAllUser(monthfirst,monthlast);
        if (userDaystateEntityList.size()!=0){
            for (PaiGongGuanLiUserDaystateEntity daystateEntity : userDaystateEntityList) {
                PaiGongGuanLiUserDaystateEntity allpaiGongGuanLiUserDaystateEntity=new PaiGongGuanLiUserDaystateEntity();
                Integer userid = daystateEntity.getUserid();//员工id
                String username = daystateEntity.getUsername();//员工姓名
                List<PaiGongGuanLiUserDaystateEntity> oneUserDayState = paiGongGuanLiUserDaystateDao.findOneUser(userid, monthfirst, monthlast);
                List<PaiGongGuanLiUserDaystateEntity> oneuserList=new ArrayList<>();
                if (oneUserDayState.size()!=0){
                    Integer idd=1;
                    for (PaiGongGuanLiUserDaystateEntity userDaystateEntity : oneUserDayState) {
                        PaiGongGuanLiUserDaystateEntity paiGongGuanLiUserDaystateEntity=new PaiGongGuanLiUserDaystateEntity();
                        Integer daystate = userDaystateEntity.getDaystate();
                        paiGongGuanLiUserDaystateEntity.setDaystate(daystate);
                        paiGongGuanLiUserDaystateEntity.setId(idd++);
                        oneuserList.add(paiGongGuanLiUserDaystateEntity);
                    }
                }
                allpaiGongGuanLiUserDaystateEntity.setUserid(userid);
                allpaiGongGuanLiUserDaystateEntity.setUsername(username);
                allpaiGongGuanLiUserDaystateEntity.setUserlist(oneuserList);
                AllUserDaystateEntityList.add(allpaiGongGuanLiUserDaystateEntity);
            }
        }
        js.put("data",AllUserDaystateEntityList);
        return js;
    }

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
