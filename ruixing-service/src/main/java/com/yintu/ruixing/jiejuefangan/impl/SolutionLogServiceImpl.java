package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.jiejuefangan.SolutionLogDao;
import com.yintu.ruixing.jiejuefangan.SolutionLogEntity;
import com.yintu.ruixing.jiejuefangan.SolutionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/29 10:23
 */
@Service
@Transactional
public class SolutionLogServiceImpl implements SolutionLogService {
    @Autowired
    private SolutionLogDao solutionLogDao;

    @Override
    public void add(SolutionLogEntity entity) {
        solutionLogDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        solutionLogDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(SolutionLogEntity entity) {
        solutionLogDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public SolutionLogEntity findById(Integer id) {
        return solutionLogDao.selectByPrimaryKey(id);
    }

    @Override
    public List<SolutionLogEntity> findByExample(SolutionLogEntity entity) {
        return solutionLogDao.selectByExample(entity);
    }
}
