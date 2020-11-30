package com.yintu.ruixing.master.common;

import com.yintu.ruixing.common.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageEntity record);

    int insertSelective(MessageEntity record);

    MessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageEntity record);

    int updateByPrimaryKeyWithBLOBs(MessageEntity record);

    int updateByPrimaryKey(MessageEntity record);

    List<MessageEntity> selectByTypeAndStatus(@Param("type")Short type,@Param("loginUserId") Integer loginUserId,@Param("status") Short status);

    List<MessageEntity> selectByExample(MessageEntity messageEntity);

    List<MessageEntity> findXiaoXi(@Param("senderid") Integer senderid,@Param("type") Integer type);


}