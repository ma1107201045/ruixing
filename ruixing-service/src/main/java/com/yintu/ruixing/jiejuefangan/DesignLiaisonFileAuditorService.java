package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileAuditorEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/6 18:41
 */
public interface DesignLiaisonFileAuditorService extends BaseService<DesignLiaisonFileAuditorEntity, Integer> {
    List<DesignLiaisonFileAuditorEntity> findByDesignLiaisonFileId(Integer designLiaisonFileId);

    List<DesignLiaisonFileAuditorEntity> findByExample(Integer designLiaisonFileId, Integer auditorId, Short isCheck);

    void addMuch(List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities);

    void removeByDesignLiaisonFileId(Integer designLiaisonFileId);
}
