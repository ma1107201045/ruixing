package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.DesignLiaisonEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface DesignLiaisonDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignLiaisonEntity record);

    int insertSelective(DesignLiaisonEntity record);

    DesignLiaisonEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignLiaisonEntity record);

    int updateByPrimaryKey(DesignLiaisonEntity record);

    List<DesignLiaisonEntity> selectAll();

    List<DesignLiaisonEntity> selectByYear(Integer year);

    List<DesignLiaisonEntity> selectByExample(Integer[] ids, Integer year, String projectName, Integer projectId);

    List<Integer> selectByDistinctProjectDate();

    List<Map<String, Object>> selectProjectsByProjectStatus(Short projectStatus);
}