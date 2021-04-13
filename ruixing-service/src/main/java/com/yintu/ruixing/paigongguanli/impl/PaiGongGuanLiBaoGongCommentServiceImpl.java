package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiBaoGongCommentDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongCommentEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/13 14:40
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class PaiGongGuanLiBaoGongCommentServiceImpl implements PaiGongGuanLiBaoGongCommentService {
    @Autowired
    private PaiGongGuanLiBaoGongCommentDao paiGongGuanLiBaoGongCommentDao;


    @Override
    public List<PaiGongGuanLiBaoGongCommentEntity> findComment(Integer baoGongId,Integer baogongtype) {
        return paiGongGuanLiBaoGongCommentDao.findComment(baoGongId,baogongtype);
    }

    @Override
    public void addComment(PaiGongGuanLiBaoGongCommentEntity paiGongGuanLiBaoGongCommentEntity) {
        paiGongGuanLiBaoGongCommentDao.insertSelective(paiGongGuanLiBaoGongCommentEntity);
    }
}
