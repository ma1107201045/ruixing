package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineBaseInformationUnitEntity;
import org.apache.ibatis.annotations.Delete;

public interface LineBaseInformationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineBaseInformationUnitEntity record);

    int insertSelective(LineBaseInformationUnitEntity record);

    LineBaseInformationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineBaseInformationUnitEntity record);

    int updateByPrimaryKey(LineBaseInformationUnitEntity record);

    @Delete("delete from mro_line_base_information_unit where line_base_information_id=#{lineBaseInformationId}")
    void deleteByLineBaseInformationId(Integer lineBaseInformationId);
}