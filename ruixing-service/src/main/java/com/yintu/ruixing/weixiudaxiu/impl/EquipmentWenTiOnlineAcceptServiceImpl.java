package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.master.guzhangzhenduan.DianWuDuanDao;
import com.yintu.ruixing.master.guzhangzhenduan.XianDuanDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiCustomerWenTiTypeDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiCustomerWenTiTypeEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptFeedbackEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiOnlineAcceptService;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/27 15:51
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiOnlineAcceptServiceImpl implements EquipmentWenTiOnlineAcceptService {
    @Autowired
    private EquipmentWenTiCustomerWenTiTypeDao equipmentWenTiCustomerWenTiTypeDao;
    @Autowired
    private EquipmentWenTiOnlineAcceptFeedbackDao equipmentWenTiOnlineAcceptFeedbackDao;
    @Autowired
    private DianWuDuanDao dianWuDuanDao;
    @Autowired
    private XianDuanDao xianDuanDao;
    @Autowired
    private EquipmentWenTiReturnVisitRecordmessageDao equipmentWenTiReturnVisitRecordmessageDao;



    @Override
    public List<XianDuanEntity> findAllXianDuan() {
        return xianDuanDao.findAllXianDuan();
    }

    @Override
    public List<DianWuDuanEntity> findAllDianWuDuan() {
        return dianWuDuanDao.findDianWuDuan();
    }

    @Override
    public void addAcceptFeedback(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity) {
        String tljname = equipmentWenTiOnlineAcceptFeedbackEntity.getTljname();

        equipmentWenTiOnlineAcceptFeedbackDao.insertSelective(equipmentWenTiOnlineAcceptFeedbackEntity);
        Integer id = equipmentWenTiOnlineAcceptFeedbackEntity.getId();
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(2);
        recordmessageEntity.setContext(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename() + "新增了一条回访任务");
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public void deleteWenTiTypeByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiCustomerWenTiTypeDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<EquipmentWenTiCustomerWenTiTypeEntity> findAllWenTiType() {
        return equipmentWenTiCustomerWenTiTypeDao.findAllWenTiType();
    }

    @Override
    public void editWenTiTypeById(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity) {
        equipmentWenTiCustomerWenTiTypeDao.updateByPrimaryKeySelective(equipmentWenTiCustomerWenTiTypeEntity);
    }

    @Override
    public void addWenTiType(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity) {
        equipmentWenTiCustomerWenTiTypeDao.insertSelective(equipmentWenTiCustomerWenTiTypeEntity);
    }
}
