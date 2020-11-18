package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface UserDataService extends BaseService<UserDataEntity, Long> {

    void removeByUserId(Long userId);

    List<UserDataEntity> findByUserId(Long userId);
}
