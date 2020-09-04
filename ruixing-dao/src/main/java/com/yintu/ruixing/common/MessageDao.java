package com.yintu.ruixing.common;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageEntity record);

    int insertSelective(MessageEntity record);

    MessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageEntity record);

    int updateByPrimaryKeyWithBLOBs(MessageEntity record);

    int updateByPrimaryKey(MessageEntity record);

    List<MessageEntity> selectByTypeAndStatus(Short type, Integer loginUserId, Short status);

    List<MessageEntity> selectByExample(MessageEntity messageEntity);

    List<MessageEntity> findXiaoXi(Integer senderid);


}