package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuRecordMessageEntity;

import java.util.Date;
import java.util.List;

public interface ChanPinJiaoFuRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuRecordMessageEntity record);

    int insertSelective(ChanPinJiaoFuRecordMessageEntity record);

    ChanPinJiaoFuRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuRecordMessageEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuRecordMessageEntity record);


    //////////////////////////////////////////////////////////
    void addRecordMessage(Integer mid, Integer typenum, Date nowTime, String username, String context);

    List<ChanPinJiaoFuRecordMessageEntity> findReordById(Integer id);
}