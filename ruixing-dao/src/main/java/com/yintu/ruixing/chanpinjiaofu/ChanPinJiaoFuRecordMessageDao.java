package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
@Mapper
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