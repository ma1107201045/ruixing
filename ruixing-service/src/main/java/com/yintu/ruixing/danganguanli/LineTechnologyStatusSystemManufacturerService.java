package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/25 9:40
 */
public interface LineTechnologyStatusSystemManufacturerService extends BaseService<LineTechnologyStatusSystemManufacturerEntity, Integer> {

    void remove(Integer[] ids);

    List<LineTechnologyStatusSystemManufacturerEntity> findByExample(String name, Integer cid);

}
