package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationStationUnitEntity;
import org.apache.ibatis.annotations.Delete;

public interface LineBaseInformationStationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationStationUnitEntity record);

    int insertSelective(LineBaseInformationStationUnitEntity record);

    LineBaseInformationStationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationStationUnitEntity record);

    int updateByPrimaryKey(LineBaseInformationStationUnitEntity record);

    @Delete("delete from mro_line_base_information_station_unit where line_base_information_station_id=#{lineBaseInformationStationId}")
    void deleteByLineBaseInformationStationId(Integer lineBaseInformationStationId);
}