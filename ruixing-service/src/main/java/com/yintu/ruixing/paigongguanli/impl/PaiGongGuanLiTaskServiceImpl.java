package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiBusinessTypeDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskUserDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDao;
import com.yintu.ruixing.paigongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/18 10:47
 * @Version 1.0
 * 需求:派工管理  任务
 */
@Service
@Transactional
public class PaiGongGuanLiTaskServiceImpl implements PaiGongGuanLiTaskService {
    @Autowired
    private PaiGongGuanLiTaskDao paiGongGuanLiTaskDao;
    @Autowired
    private PaiGongGuanLiTaskUserDao paiGongGuanLiTaskUserDao;
    @Autowired
    private PaiGongGuanLiBusinessTypeDao paiGongGuanLiBusinessTypeDao;
    @Autowired
    private PaiGongGuanLiUserDao paiGongGuanLiUserDao;

    @Override
    public void deleteUserTaskByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiTaskUserDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<PaiGongGuanLiTaskEntity> findAllTasks() {
        return paiGongGuanLiTaskDao.findAllTasks();
    }

    @Override
    public void addTaskScore(PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity) {
        paiGongGuanLiTaskUserDao.insertSelective(paiGongGuanLiTaskUserEntity);
    }

    @Override
    public void deleteTaskByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            paiGongGuanLiTaskDao.deleteByPrimaryKey(ids[i]);
            paiGongGuanLiTaskUserDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findChuChaiById(Integer id) {
        return paiGongGuanLiBusinessTypeDao.findChuChaiById(id);
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findAllBusinessType() {
        return paiGongGuanLiBusinessTypeDao.findAllBusinessType();
    }

    @Override
    public void editUserPowerScoreById(PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity) {
        paiGongGuanLiTaskUserDao.updateByPrimaryKeySelective(paiGongGuanLiTaskUserEntity);
    }

    @Override
    public List<PaiGongGuanLiTaskUserEntity> findUserPowerScoreById(Integer id, String taskTotalName) {
        return paiGongGuanLiTaskUserDao.findUserPowerScoreById(id, taskTotalName);
    }

    @Override
    public List<PaiGongGuanLiTaskUserEntity> findSomeUserPowerScore(Integer page, Integer size, String userName) {
        return paiGongGuanLiTaskUserDao.findSomeUserPowerScore(userName);
    }

    @Override
    public void addUser(Integer[] uid) {
        List<Integer> Tid = paiGongGuanLiTaskDao.findId();
        for (int i = 0; i < uid.length; i++) {
            for (Integer tid : Tid) {
                paiGongGuanLiTaskUserDao.addTask(uid[i], tid);
            }
        }
    }

    @Override
    public List<PaiGongGuanLiTaskEntity> findSomeTask(Integer page, Integer size, String taskname) {
        return paiGongGuanLiTaskDao.findSomeTask(taskname);
    }

    @Override
    public void editTaskById(PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity) {
        String taskname = paiGongGuanLiTaskEntity.getTaskname();
        String businesstask = paiGongGuanLiTaskEntity.getBusinesstask();
        String businesstype = paiGongGuanLiTaskEntity.getBusinesstype();
        String taskName = taskname + businesstype + "——" + businesstask;
        paiGongGuanLiTaskEntity.setTasktotalname(taskName);
        paiGongGuanLiTaskDao.updateByPrimaryKeySelective(paiGongGuanLiTaskEntity);
    }

    @Override
    public void addTask(PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity) {
        String taskname = paiGongGuanLiTaskEntity.getTaskname();
        String businesstask = paiGongGuanLiTaskEntity.getBusinesstask();
        String businesstype = paiGongGuanLiTaskEntity.getBusinesstype();
        String taskName = taskname + businesstype + "——" + businesstask;
        paiGongGuanLiTaskEntity.setTasktotalname(taskName);
        paiGongGuanLiTaskDao.insertSelective(paiGongGuanLiTaskEntity);
        Integer id = paiGongGuanLiTaskEntity.getId();
        List<Integer> Uid = paiGongGuanLiUserDao.findUid();
        if (Uid.size() != 0) {
            for (Integer uid : Uid) {
                paiGongGuanLiTaskUserDao.addTask(uid, id);
            }
        }
    }
}
