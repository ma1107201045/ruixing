package com.yintu.ruixing.master.guzhangzhenduan;


import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingPropertyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-17 10
 */

public interface BaoJingYuJingPropertyDao {
    List<BaoJingYuJingPropertyEntity> findBaoJingYuJingTree(Integer parentId);
}
