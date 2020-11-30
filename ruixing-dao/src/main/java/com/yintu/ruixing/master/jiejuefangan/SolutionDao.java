package com.yintu.ruixing.master.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/4 9:49
 */

public interface SolutionDao {

    List<Integer> countTaskStatusByGroupBy(Integer selectType, Date date, Short taskStatus);

    List<Map<String, Object>> selectByDateSectionAndProjectStatus(Date startDate, Date endDate, Short projectStatus);

    long selectProjectSum();
}
