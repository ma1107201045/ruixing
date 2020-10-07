package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportVideoMeetingDao;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportVideoMeetingEntityWithBLOBs;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportVideoMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/7 15:16
 */
@Service
@Transactional
public class RemoteSupportVideoMeetingServiceImpl implements RemoteSupportVideoMeetingService {
    @Autowired
    private RemoteSupportVideoMeetingDao remoteSupportVideoMeetingDao;

    @Override
    public void add(RemoteSupportVideoMeetingEntityWithBLOBs entity) {
        remoteSupportVideoMeetingDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        remoteSupportVideoMeetingDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(RemoteSupportVideoMeetingEntityWithBLOBs entity) {
        remoteSupportVideoMeetingDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public RemoteSupportVideoMeetingEntityWithBLOBs findById(Integer id) {
        return remoteSupportVideoMeetingDao.selectByPrimaryKey(id);
    }

    @Override
    public void addJoinPerson(Integer id, String joinPerson) {
        if (id == null || joinPerson == null || joinPerson.equals(""))
            throw new BaseRuntimeException("id或者参与人姓名不能为空");
        RemoteSupportVideoMeetingEntityWithBLOBs remoteSupportVideoMeetingEntityWithBLOBs = this.findById(id);
        if (remoteSupportVideoMeetingEntityWithBLOBs != null) {
            String joinPersonStr = remoteSupportVideoMeetingEntityWithBLOBs.getJoinPerson();
            remoteSupportVideoMeetingEntityWithBLOBs = new RemoteSupportVideoMeetingEntityWithBLOBs();
            remoteSupportVideoMeetingEntityWithBLOBs.setId(id);
            remoteSupportVideoMeetingEntityWithBLOBs.setJoinPerson((joinPersonStr == null ? "" : joinPersonStr) + joinPerson + ",");
            this.edit(remoteSupportVideoMeetingEntityWithBLOBs);
        }

    }

    @Override
    public void addDuration(Integer id, Integer duration, String loginUserName) {
        if (id == null || duration == null)
            throw new BaseRuntimeException("id或者会议时长不能为空");
        RemoteSupportVideoMeetingEntityWithBLOBs remoteSupportVideoMeetingEntityWithBLOBs = this.findById(id);
        if (remoteSupportVideoMeetingEntityWithBLOBs != null) {
            remoteSupportVideoMeetingEntityWithBLOBs = new RemoteSupportVideoMeetingEntityWithBLOBs();
            remoteSupportVideoMeetingEntityWithBLOBs.setId(id);
            remoteSupportVideoMeetingEntityWithBLOBs.setModifiedBy(loginUserName);
            remoteSupportVideoMeetingEntityWithBLOBs.setModifiedTime(new Date());
            remoteSupportVideoMeetingEntityWithBLOBs.setDuration(duration);
            this.edit(remoteSupportVideoMeetingEntityWithBLOBs);
        }
    }

    @Override
    public void addOpinion(Integer id, String opinion, String loginUserName) {
        if (id == null || opinion == null || opinion.equals(""))
            throw new BaseRuntimeException("id或者处置意见不能为空");
        RemoteSupportVideoMeetingEntityWithBLOBs remoteSupportVideoMeetingEntityWithBLOBs = this.findById(id);
        if (remoteSupportVideoMeetingEntityWithBLOBs != null) {
            remoteSupportVideoMeetingEntityWithBLOBs = new RemoteSupportVideoMeetingEntityWithBLOBs();
            remoteSupportVideoMeetingEntityWithBLOBs.setId(id);
            remoteSupportVideoMeetingEntityWithBLOBs.setModifiedBy(loginUserName);
            remoteSupportVideoMeetingEntityWithBLOBs.setModifiedTime(new Date());
            remoteSupportVideoMeetingEntityWithBLOBs.setOpinion(opinion);
            this.edit(remoteSupportVideoMeetingEntityWithBLOBs);
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public RemoteSupportVideoMeetingEntityWithBLOBs findByCondition(Integer id) {
        List<RemoteSupportVideoMeetingEntityWithBLOBs> remoteSupportVideoMeetingEntityWithBLOBs = remoteSupportVideoMeetingDao.selectByCondition(new Integer[]{id}, null);
        return remoteSupportVideoMeetingEntityWithBLOBs.isEmpty() ? null : remoteSupportVideoMeetingEntityWithBLOBs.get(0);
    }

    @Override
    public List<RemoteSupportVideoMeetingEntityWithBLOBs> findByCondition(Integer[] ids, String context) {
        return remoteSupportVideoMeetingDao.selectByCondition(ids, context);
    }
}
