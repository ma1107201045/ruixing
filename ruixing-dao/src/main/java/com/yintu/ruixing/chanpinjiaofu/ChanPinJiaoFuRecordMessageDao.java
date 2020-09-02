package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    void addRecordMessage(@Param("mid") Integer mid,@Param("typenum") Integer typenum,
                          @Param("nowTime") Date nowTime,@Param("username") String username,@Param("context") String context);

    List<ChanPinJiaoFuRecordMessageEntity> findReordById(Integer id);

    List<ChanPinJiaoFuRecordMessageEntity> findFileReordById(Integer id);
}