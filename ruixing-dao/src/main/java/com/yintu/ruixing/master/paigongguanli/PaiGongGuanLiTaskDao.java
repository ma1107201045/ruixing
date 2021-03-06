package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiTaskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface PaiGongGuanLiTaskDao {
    int insert(PaiGongGuanLiTaskEntity record);

    PaiGongGuanLiTaskEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PaiGongGuanLiTaskEntity record);



    ////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiTaskEntity record);

    int insertSelective(PaiGongGuanLiTaskEntity record);

    List<PaiGongGuanLiTaskEntity> findSomeTask(String taskname);

    List<Integer> findId();

    Integer findTid(String renwu);

    List<PaiGongGuanLiTaskEntity> findAllTasks();
}