package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.QuDuanDownloadDao;
import com.yintu.ruixing.guzhangzhenduan.QuDuanDownloadEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanDownloadService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/8 15:33
 */
@Service
@Transactional
public class QuDuanDownloadServiceImpl implements QuDuanDownloadService {

    @Autowired
    private QuDuanDownloadDao quDuanDownloadDao;

    //**************************数据下载******************************
    @Override
    public void add(QuDuanDownloadEntity quDuanDownloadEntity) {
        quDuanDownloadDao.insertSelective(quDuanDownloadEntity);
    }

    @Override
    public void remove(Integer id) {
        quDuanDownloadDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(QuDuanDownloadEntity quDuanDownloadEntity) {
        quDuanDownloadDao.updateByPrimaryKeySelective(quDuanDownloadEntity);
    }

    @Override
    public QuDuanDownloadEntity findById(Integer id) {
        return quDuanDownloadDao.selectByPrimaryKey(id);
    }


    @Override
    public List<QuDuanDownloadEntity> findByDateTime(Integer czId, Date startDateTime, Date endDateTime) {
        return quDuanDownloadDao.selectByDateTime(czId, startDateTime, endDateTime);
    }


    @Override
    public Integer add(Integer czId, Short type, Date startDateTime, Date endDateTime) {
        QuDuanDownloadEntity quDuanDownloadEntity = new QuDuanDownloadEntity();
        quDuanDownloadEntity.setCid(czId);
        quDuanDownloadEntity.setStartTime(startDateTime);
        quDuanDownloadEntity.setEndTime(endDateTime);
        quDuanDownloadEntity.setStatus((short) 0);
        quDuanDownloadEntity.setType(type);
        quDuanDownloadEntity.setDataType((short) 1);
        quDuanDownloadEntity.setCreateTime(new Date());
        this.add(quDuanDownloadEntity);
        return quDuanDownloadEntity.getId();
    }


    //**************************数据接收或者关闭******************************

    public void insertData(Integer czId, Integer userId) {
        QuDuanDownloadEntity quDuanDownloadEntity = new QuDuanDownloadEntity();
        quDuanDownloadEntity.setCreateTime(new Date());
        quDuanDownloadEntity.setCid(czId);
        quDuanDownloadEntity.setUserId(userId);
        quDuanDownloadEntity.setDataType((short) 2);

        quDuanDownloadEntity.setDataStatus((short) 0);
        quDuanDownloadEntity.setSwitchStatus((short) 1);

        quDuanDownloadEntity.setUpdateTime(new Date());
        this.add(quDuanDownloadEntity);
    }

    @Override
    public Integer changeDataStatus(Integer czId, Integer userId, Short dataStatus) {
        QuDuanDownloadEntity quDuanDownloadEntity = this.findByCzIdAndUserId(czId, userId);
        quDuanDownloadEntity.setDataStatus(dataStatus);
        if (dataStatus.equals((short) 1)) {
            quDuanDownloadEntity.setCreateTime(new Date());
        } else {
            if (quDuanDownloadEntity.getSwitchStatus().equals((short) 0)) {
                quDuanDownloadEntity.setSwitchStatus((short) 1);
            }
        }
        this.edit(quDuanDownloadEntity);
        return quDuanDownloadEntity.getId();
    }

    @Override
    public Integer changeSwitchStatus(Integer czId, Integer userId, Short switchStatus) {
        QuDuanDownloadEntity quDuanDownloadEntity = this.findByCzIdAndUserId(czId, userId);
        quDuanDownloadEntity.setSwitchStatus(switchStatus);
        this.edit(quDuanDownloadEntity);
        return quDuanDownloadEntity.getId();
    }

    @Override
    public Short changeUpdateTime(Integer czId, Integer userId) {
        QuDuanDownloadEntity quDuanDownloadEntity = this.findByCzIdAndUserId(czId, userId);
        quDuanDownloadEntity.setUpdateTime(new Date());
        this.edit(quDuanDownloadEntity);
        return quDuanDownloadEntity.getSwitchStatus();
    }

    @Override
    public QuDuanDownloadEntity findByCzIdAndUserId(Integer czId, Integer userId) {
        QuDuanDownloadEntity quDuanDownloadEntity = quDuanDownloadDao.selectByCidAndDataType(czId, userId, (short) 2);
        if (quDuanDownloadEntity == null)
            insertData(czId, userId);
        return quDuanDownloadEntity;
    }

}

