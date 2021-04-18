package com.yintu.ruixing.paigongguanli;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/18 10:46
 * @Version 1.0
 * 需求:派工管理  任务
 */
public interface PaiGongGuanLiTaskService {

    void addTask(PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity);

    void editTaskById(PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity);

    List<PaiGongGuanLiTaskEntity> findSomeTask(Integer page, Integer size, String taskname);

    void addUser(Integer[] uid);

    List<PaiGongGuanLiTaskUserEntity> findSomeUserPowerScore(Integer page, Integer size, String userName);

    List<PaiGongGuanLiTaskUserEntity> findUserPowerScoreById(  Integer id, String taskTotalName);

    void editUserPowerScoreById(PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity);

    List<PaiGongGuanLiBusinessTypeEntity> findAllBusinessType();

    List<PaiGongGuanLiBusinessTypeEntity> findChuChaiById(Integer id);

    void deleteTaskByIds(Integer[] ids);

    void addTaskScore(PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity);

    List<PaiGongGuanLiTaskEntity> findAllTasks();

    void deleteUserTaskByIds(Integer[] ids);

}
