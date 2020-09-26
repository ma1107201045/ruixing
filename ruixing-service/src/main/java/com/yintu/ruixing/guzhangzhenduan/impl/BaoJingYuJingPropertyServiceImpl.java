package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-17 10
 */
@Service
@Transactional
public class BaoJingYuJingPropertyServiceImpl implements BaoJingYuJingPropertyService {
    @Autowired
    private BaoJingYuJingPropertyDao baoJingYuJingPropertyDao;
    @Autowired
    private BaoJingYuJingDao baoJingYuJingDao;
    @Autowired
    private QuDuanBaseDao quDuanBaseDao;
    @Autowired
    private AlarmTableDao alarmTableDao;
    @Autowired
    private CheZhanDao cheZhanDao;
    @Autowired
    private BaoJingYuJingBaseDao baoJingYuJingBaseDao;


    @Override
    public List<AlarmEntity> findAllNotReadAlarmDatas(Integer page, Integer size, String tableName) {
        List<AlarmEntity> alarmEntityList = new ArrayList<>();
        List<AlarmTableEntity> alarmTableEntityList = alarmTableDao.findAllNotReadAlarmDatas(tableName);
        for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);
            if (czNumber > 0) {//通信编码电路
                //查询区段名
                String quduanNmae=quDuanBaseDao.findQuduanName(stationId,sectionId);
                if (alarmlevel==1){//报警信息
                    Integer bjyjType=2;
                    String alarmContext=baoJingYuJingBaseDao.findAlarmContext(alarmcode,bjyjType);//报警内容
                }else if (alarmlevel==2){//预警信息
                    Integer bjyjType=3;
                    String alarmContext=baoJingYuJingBaseDao.findAlarmContext(alarmcode,bjyjType);//预警内容
                }

            } else if (czNumber == 0) {//继电编码电路
                //查询区段名
                String quduanNmae=quDuanBaseDao.findQuduanName(stationId,sectionId);
                if (alarmlevel==1){//报警信息
                    Integer bjyjType=1;
                    String alarmContext=baoJingYuJingBaseDao.findAlarmContext(alarmcode,bjyjType);//报警内容
                }else if (alarmlevel==2){//预警信息
                    Integer bjyjType=3;
                    String alarmContext=baoJingYuJingBaseDao.findAlarmContext(alarmcode,bjyjType);//预警内容
                }
            }

        }
        return null;
    }

    @Override
    public void editAlarmState(AlarmTableEntity alarmTableEntity, String tableName) {
        alarmTableEntity.setStatus(1);
        alarmTableDao.editAlarmState(alarmTableEntity, tableName);
    }

    @Override
    public Integer findAlarmNumber(String tableName) {
        return alarmTableDao.findAlarmNumber(tableName);
    }


    @Override
    public List<TreeNodeUtil> findBaoJingYuJingTree(Integer parentId) {
        List<BaoJingYuJingPropertyEntity> baoJingYuJingPropertyEntities = baoJingYuJingPropertyDao.findBaoJingYuJingTree(parentId);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (BaoJingYuJingPropertyEntity baoJingYuJingPropertyEntity : baoJingYuJingPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId((long) baoJingYuJingPropertyEntity.getId());
            treeNodeUtil.setLabel(baoJingYuJingPropertyEntity.getName());
            treeNodeUtil.setChildren(this.findBaoJingYuJingTree(baoJingYuJingPropertyEntity.getId()));
            treeNodeUtils.add(treeNodeUtil);

        }
        return treeNodeUtils;
    }

    @Override
    public List<BaoJingYuJingEntity> findAllYuJingBaoJing(Integer page, Integer size) {
        return baoJingYuJingDao.findAllYuJingBaoJing();
    }

    @Override
    public List<BaoJingYuJingEntity> findYuJingBaoJingBySouSuo(Integer[] ids, Integer sid, Integer qid,
                                                               Date startTime, Date huifuTime, Integer tianChuang,
                                                               Integer lvChuHuiFu, Integer lvChuKaiTong, Integer page, Integer size) {
        return baoJingYuJingDao.findYuJingBaoJingBySouSuo(ids, sid, qid, startTime, huifuTime, tianChuang, lvChuHuiFu, lvChuKaiTong);
    }

    @Override
    public List<QuDuanBaseEntity> findAllQuDuan() {
        // List<QuDuanBaseEntity> list1=null;
        List<QuDuanBaseEntity> list = quDuanBaseDao.findAllQuDuanName();

        return list;
    }
}
