package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 17:26
 */
@Service
@Transactional
public interface LineTechnologyStatusProductModelNumberService extends BaseService<LineTechnologyStatusProductModelNumberEntity, Integer> {

    List<LineTechnologyStatusProductModelNumberEntity> findAll();
}
