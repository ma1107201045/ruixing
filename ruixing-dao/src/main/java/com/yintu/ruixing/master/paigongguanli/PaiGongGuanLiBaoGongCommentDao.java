package com.yintu.ruixing.master.paigongguanli;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongCommentEntity;

import java.util.List;

public interface PaiGongGuanLiBaoGongCommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PaiGongGuanLiBaoGongCommentEntity record);

    PaiGongGuanLiBaoGongCommentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaiGongGuanLiBaoGongCommentEntity record);

    int updateByPrimaryKey(PaiGongGuanLiBaoGongCommentEntity record);
//////////////////////////////////////////////////////////////////////
    int insertSelective(PaiGongGuanLiBaoGongCommentEntity record);

    List<PaiGongGuanLiBaoGongCommentEntity> findComment(Integer baoGongId,Integer baogongtype);
}