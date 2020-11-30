package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiBusinessTypeDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBusinessTypeEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/14 15:17
 * @Version 1.0
 * 需求:派工管理  任务类型
 */
@Service
@Transactional
public class PaiGongGuanLiBusinessTypeServiceImpl implements PaiGongGuanLiBusinessTypeService {
    @Autowired
    private PaiGongGuanLiBusinessTypeDao paiGongGuanLiBusinessTypeDao;

    @Override
    public void deleteBusinessTypeByIds(Integer id) {
        paiGongGuanLiBusinessTypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findChuChaRenWu(Integer id) {
        return paiGongGuanLiBusinessTypeDao.findChuChaRenWu(id);
    }

    @Override
    public void deleteChuChaRenWuByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            paiGongGuanLiBusinessTypeDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public void editChuChaRenWuById(PaiGongGuanLiBusinessTypeEntity paiGongGuanLiBusinessTypeEntity) {
        paiGongGuanLiBusinessTypeDao.updateByPrimaryKeySelective(paiGongGuanLiBusinessTypeEntity);
    }

    @Override
    public void addChuChaRenWu(Integer pid, PaiGongGuanLiBusinessTypeEntity paiGongGuanLiBusinessTypeEntity) {
        paiGongGuanLiBusinessTypeEntity.setParentid(pid);
        paiGongGuanLiBusinessTypeDao.insertSelective(paiGongGuanLiBusinessTypeEntity);
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findSomeChuChaRenWu(Integer id, Integer page, Integer size, String businessTypeaName) {
        return paiGongGuanLiBusinessTypeDao.findSomeChuChaRenWu(id,businessTypeaName);
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findSomeBusinessTypea(Integer page, Integer size, String businessTypeaName) {
        return paiGongGuanLiBusinessTypeDao.findSomeBusinessTypea(businessTypeaName);
    }

    @Override
    public void editBusinessTypeaById(PaiGongGuanLiBusinessTypeEntity paiGongGuanLiBusinessTypeEntity) {
        paiGongGuanLiBusinessTypeEntity.setParentid(0);
        paiGongGuanLiBusinessTypeDao.updateByPrimaryKeySelective(paiGongGuanLiBusinessTypeEntity);
    }

    @Override
    public void addBusinessTypea(PaiGongGuanLiBusinessTypeEntity paiGongGuanLiBusinessTypeEntity) {
        paiGongGuanLiBusinessTypeEntity.setParentid(0);
        paiGongGuanLiBusinessTypeDao.insertSelective(paiGongGuanLiBusinessTypeEntity);
    }
}
