package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanDownloadEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

public interface QuDuanDownloadDao {
    int deleteByPrimaryKey(Integer id);

    int insert(QuDuanDownloadEntity record);

    int insertSelective(QuDuanDownloadEntity record);

    QuDuanDownloadEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuDuanDownloadEntity record);

    int updateByPrimaryKey(QuDuanDownloadEntity record);


    List<QuDuanDownloadEntity> selectByDateTime(Integer czId, Date startDateTime, Date endDateTime);

    QuDuanDownloadEntity selectByCidAndDataType(Integer cid, Integer userId, Short dataType);
}