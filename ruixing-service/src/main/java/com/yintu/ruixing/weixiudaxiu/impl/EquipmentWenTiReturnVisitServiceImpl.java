package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Mr.liu
 * @Date 2021/4/21 17:23
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiReturnVisitServiceImpl implements EquipmentWenTiReturnVisitService {
    @Autowired
    private EquipmentWenTiReturnVisitDao equipmentWenTiReturnVisitDao;


}
