package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import org.omg.CORBA.INTERNAL;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:50
 * @Version: 1.0
 */
public interface LineBaseInformationUnitService extends BaseService<LineBaseInformationUnitEntity, Integer> {

    void removeByLineBaseInformationId(Integer lineBaseInformationId);

}
