package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.PreSaleFileAuditorEntity;
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

    List<DesignLiaisonFileAuditorEntity> selectByExample(Integer designLiaisonFileId, Integer auditorId, Integer sort, Short activate);

    void insertMuch(List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities);

    void deleteByDesignLiaisonFileId(Integer designLiaisonFileId);
}