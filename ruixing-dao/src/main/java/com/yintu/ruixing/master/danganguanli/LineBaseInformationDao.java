package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface LineBaseInformationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationEntity record);

    int insertSelective(LineBaseInformationEntity record);

    LineBaseInformationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationEntity record);

    int updateByPrimaryKeyWithBLOBs(LineBaseInformationEntity record);

    int updateByPrimaryKey(LineBaseInformationEntity record);

    @Select("select t.tid,t.tlj_name from mro_tieluju t order by t.tid desc")
    List<Map<String, Object>> selectRailwaysBureauTid();

    @Select("select id,name from mro_line_base_information where tid=#{tid} order by modified_time desc")
    List<Map<String, Object>> selectByTid(Integer tid);

    @Select("select id,name from mro_station_base_information where line_base_information_id=#{id} order by modified_time desc")
    List<Map<String, Object>> selectStationById(Integer id);


    List<LineBaseInformationEntity> selectByExample(Integer[] ids, Integer tid);

    List<DianWuDuanEntity> selectDianWuDuanEntityById(Integer id);
}