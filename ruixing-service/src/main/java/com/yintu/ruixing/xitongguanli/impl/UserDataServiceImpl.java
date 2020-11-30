package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.master.xitongguanli.UserDataDao;
import com.yintu.ruixing.xitongguanli.UserDataEntity;
import com.yintu.ruixing.xitongguanli.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {
    @Autowired
    private UserDataDao userDataDao;

    @Override
    public void add(UserDataEntity entity) {
        userDataDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        userDataDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(UserDataEntity entity) {
        userDataDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public UserDataEntity findById(Long id) {
        return userDataDao.selectByPrimaryKey(id);
    }

    @Override
    public void removeByUserId(Long userId) {
        userDataDao.deleteByUserId(userId);
    }

    @Override
    public List<UserDataEntity> findByUserId(Long userId) {
        return userDataDao.selectByUserId(userId);
    }
}
