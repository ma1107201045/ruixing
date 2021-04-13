package com.yintu.ruixing.paigongguanli;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/13 14:39
 * @Version 1.0
 * 需求:
 */
public interface PaiGongGuanLiBaoGongCommentService {
    void addComment(PaiGongGuanLiBaoGongCommentEntity paiGongGuanLiBaoGongCommentEntity);

    List<PaiGongGuanLiBaoGongCommentEntity> findComment(Integer baoGongId,Integer baogongtype);

}
