package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoPropertyEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/7 10:08
 */
public interface QuDuanInfoPropertyService extends BaseService<QuDuanInfoPropertyEntity, Integer> {
    List<QuDuanInfoPropertyEntity> findByType(Short type);

    List<QuDuanInfoPropertyEntity> findByIds(Integer[] ids);

    List<QuDuanInfoPropertyEntity> finByName(String name);
}
