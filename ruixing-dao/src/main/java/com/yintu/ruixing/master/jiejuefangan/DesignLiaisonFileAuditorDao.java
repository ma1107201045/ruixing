package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileAuditorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface DesignLiaisonFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignLiaisonFileAuditorEntity record);

    int insertSelective(DesignLiaisonFileAuditorEntity record);

    DesignLiaisonFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignLiaisonFileAuditorEntity record);

    int updateByPrimaryKey(DesignLiaisonFileAuditorEntity record);

    List<DesignLiaisonFileAuditorEntity> selectByDesignLiaisonFileId(Integer designLiaisonFileId);

    void insertMuch(List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities);

    void deleteByDesignLiaisonFileId(Integer designLiaisonFileId);
}