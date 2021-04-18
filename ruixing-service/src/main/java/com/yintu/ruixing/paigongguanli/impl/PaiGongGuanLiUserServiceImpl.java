package com.yintu.ruixing.paigongguanli.impl;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.VacationDayCalculate;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskUserDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDaystateDao;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiTaskUserEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserDaystateEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class PaiGongGuanLiUserServiceImpl implements PaiGongGuanLiUserService {
    @Autowired
    private PaiGongGuanLiUserDao paiGongGuanLiUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PaiGongGuanLiTaskDao paiGongGuanLiTaskDao;
    @Autowired
    private PaiGongGuanLiTaskUserDao paiGongGuanLiTaskUserDao;
    @Autowired
    private PaiGongGuanLiUserDaystateDao paiGongGuanLiUserDaystateDao;

    @Override
    public List<PaiGongGuanLiUserEntity> finaAlreadyPaiGongPeople() {
        return paiGongGuanLiUserDao.finaAlreadyPaiGongPeople();
    }

    @Override
    public List<PaiGongGuanLiUserEntity> findAllUser(String name) {
        return paiGongGuanLiUserDao.findAllUser(name);
    }

    @Override
    public void deleteById(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity) {
        Long userid = paiGongGuanLiUserEntity.getId();//人员id
        paiGongGuanLiUserDao.deleteByUserid(userid);
        //删除人员状态
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userid);
        userEntity.setPaiGongGuanLiState(0);
        userDao.updateByPrimaryKeySelective(userEntity);
        //刪除分數
        paiGongGuanLiTaskUserDao.deleteByuid(userid.intValue());
        //删除人员的日勤数据
        paiGongGuanLiUserDaystateDao.deleteByUserid(userid.intValue());
    }

    @Override
    public void autoAddPeopleState() {
        Integer dayState = 0;
        Integer baogongState = 0;
        //查询已经选择的所有人员的userid
        List<PaiGongGuanLiUserEntity> userDaystateEntityList = paiGongGuanLiUserDao.findUserID();
        if (userDaystateEntityList.size() != 0) {
            for (PaiGongGuanLiUserEntity userDaystateEntity : userDaystateEntityList) {
                //添加下一年的日勤
                Integer thirdyear = new Date().getYear() + 2;//获取当前年份
                HashMap<String, Boolean> nextmap = new VacationDayCalculate().yearVacationDay(thirdyear + 1900);
                for (String s : nextmap.keySet()) {
                    Boolean aBoolean = nextmap.get(s);
                    if (aBoolean) {
                        dayState = 2;
                        baogongState = 0;
                    } else {
                        dayState = 1;
                        baogongState = 1;
                    }
                    PaiGongGuanLiUserDaystateEntity daystateEntity = new PaiGongGuanLiUserDaystateEntity();
                    daystateEntity.setUserid(userDaystateEntity.getUserid());
                    daystateEntity.setUsername(userDaystateEntity.getName());
                    daystateEntity.setRiqi(s);
                    daystateEntity.setDaystate(dayState);
                    daystateEntity.setOtherState(0);
                    daystateEntity.setBaogongState(baogongState);
                    daystateEntity.setCreatename("系统");
                    daystateEntity.setCreatetime(new Date());
                    daystateEntity.setUpdatename("系统");
                    daystateEntity.setUpdatetime(new Date());
                    paiGongGuanLiUserDaystateDao.insertSelective(daystateEntity);
                }
            }
        }
    }

    @Override
    public void addPGGLuser(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity, String username) {
        Integer userid = paiGongGuanLiUserEntity.getUserid();
        paiGongGuanLiUserDao.insertSelective(paiGongGuanLiUserEntity);
        Long id = paiGongGuanLiUserEntity.getId();
        UserEntity userEntity = new UserEntity();
        userEntity.setId((long) userid);
        userEntity.setPaiGongGuanLiState(1);
        userDao.updateByPrimaryKeySelective(userEntity);
        List<Integer> taskid = paiGongGuanLiTaskDao.findId();
        String name = paiGongGuanLiUserEntity.getName();
        //添加分值配置
        if (taskid.size() != 0) {
            for (Integer taskId : taskid) {
                PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity = new PaiGongGuanLiTaskUserEntity();
                paiGongGuanLiTaskUserEntity.setTid(taskId);
                paiGongGuanLiTaskUserEntity.setUid(id.intValue());
                paiGongGuanLiTaskUserEntity.setScore(-1);
                paiGongGuanLiTaskUserDao.insertSelective(paiGongGuanLiTaskUserEntity);
            }
        }
        //添加当年日勤状态
        Integer dayState = 0;
        Integer baogongState = 0;
        Integer year = new Date().getYear();//获取当前年份
        HashMap<String, Boolean> map = new VacationDayCalculate().yearVacationDay(year + 1900);
        for (String s : map.keySet()) {
            Boolean aBoolean = map.get(s);
            if (aBoolean) {
                dayState = 2;
                baogongState = 0;
            } else {
                dayState = 1;
                baogongState = 1;
            }
            PaiGongGuanLiUserDaystateEntity daystateEntity = new PaiGongGuanLiUserDaystateEntity();
            daystateEntity.setUserid(userid);
            daystateEntity.setUsername(name);
            daystateEntity.setRiqi(s);
            daystateEntity.setDaystate(dayState);
            daystateEntity.setOtherState(0);
            daystateEntity.setBaogongState(baogongState);
            daystateEntity.setCreatename(username);
            daystateEntity.setCreatetime(new Date());
            daystateEntity.setUpdatename(username);
            daystateEntity.setUpdatetime(new Date());
            paiGongGuanLiUserDaystateDao.insertSelective(daystateEntity);
        }

        //添加下一年的日勤
        Integer nextyear = new Date().getYear() + 1;//获取当前年份
        HashMap<String, Boolean> nextmap = new VacationDayCalculate().yearVacationDay(nextyear + 1900);
        for (String s : nextmap.keySet()) {
            Boolean aBoolean = nextmap.get(s);
            if (aBoolean) {
                dayState = 2;
                baogongState = 0;
            } else {
                dayState = 1;
                baogongState = 1;
            }
            PaiGongGuanLiUserDaystateEntity daystateEntity = new PaiGongGuanLiUserDaystateEntity();
            daystateEntity.setUserid(userid);
            daystateEntity.setUsername(name);
            daystateEntity.setRiqi(s);
            daystateEntity.setDaystate(dayState);
            daystateEntity.setOtherState(0);
            daystateEntity.setBaogongState(baogongState);
            daystateEntity.setCreatename(username);
            daystateEntity.setCreatetime(new Date());
            daystateEntity.setUpdatename(username);
            daystateEntity.setUpdatetime(new Date());
            paiGongGuanLiUserDaystateDao.insertSelective(daystateEntity);
        }

    }
}
