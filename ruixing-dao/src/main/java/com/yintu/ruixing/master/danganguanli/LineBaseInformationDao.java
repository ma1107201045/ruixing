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

    @Select("SELECT lbi.id,lbi.name FROM ( SELECT * FROM mro_line_base_information WHERE tid=#{tid,jdbcType=INTEGER} ORDER BY id DESC ) lbi  GROUP BY lbi.name ORDER BY lbi.id DESC")
    List<Map<String, Object>> selectByTid(Integer tid);

    @Select("SELECT lbis.id,lbis.name FROM ( SELECT * FROM mro_line_base_information_station WHERE line_base_information_id=#{lineBaseInformationId,jdbcType=INTEGER} ORDER BY id DESC ) lbis  GROUP BY lbis.name ORDER BY lbis.id DESC")
    List<Map<String, Object>> selectStationById(Integer id);

    List<LineBaseInformationEntity> selectNewLineByTid(Integer tid);

    List<LineBaseInformationEntity> selectHistoryByExample(Integer tid, Integer id, String name, Integer[] ids);

    List<DianWuDuanEntity> selectDianWuDuanEntityById(Integer id);
}