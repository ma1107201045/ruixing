package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 19:43
 */
public interface LineTechnologyStatusProductService extends BaseService<LineTechnologyStatusProductEntityWithBLOBs, Integer> {

    void remove(Integer[] ids);

    List<LineTechnologyStatusProductEntityWithBLOBs> findByExample(String name, Integer cid);
}
