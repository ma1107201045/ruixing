package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.master.xitongguanli.DepartmentUserDao;
import com.yintu.ruixing.xitongguanli.DepartmentUserEntity;
import com.yintu.ruixing.xitongguanli.DepartmentUserEntityExample;
import com.yintu.ruixing.xitongguanli.DepartmentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/10 17:25
 */
@Service
@Transactional
public class DepartmentUserServiceImpl implements DepartmentUserService {
    @Autowired
    private DepartmentUserDao departmentUserDao;

    @Override
    public void add(DepartmentUserEntity entity) {
        entity.setCreateTime(new Date());
        entity.setModifiedTime(new Date());
        departmentUserDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        departmentUserDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(DepartmentUserEntity entity) {
        entity.setModifiedTime(new Date());
        departmentUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public DepartmentUserEntity findById(Long id) {
        return departmentUserDao.selectByPrimaryKey(id);
    }

    @Override
    public List<DepartmentUserEntity> findByExample(DepartmentUserEntityExample departmentUserEntityExample) {
        return departmentUserDao.selectByExample(departmentUserEntityExample);
    }

    @Override
    public void removeByExample(DepartmentUserEntityExample departmentUserEntityExample) {
        departmentUserDao.deleteByExample(departmentUserEntityExample);
    }
}
