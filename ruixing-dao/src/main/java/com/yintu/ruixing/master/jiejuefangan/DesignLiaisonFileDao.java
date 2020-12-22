package com.yintu.ruixing.master.jiejuefangan;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface DesignLiaisonFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignLiaisonFileEntity record);

    int insertSelective(DesignLiaisonFileEntity record);

    DesignLiaisonFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignLiaisonFileEntity record);

    int updateByPrimaryKey(DesignLiaisonFileEntity record);

    List<DesignLiaisonFileEntity> selectByCondition(Integer designLiaisonId, Integer[] ids, String name, Short type, Integer userId, Short releaseStatus);

    List<AuditTotalVo> selectByExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);
}