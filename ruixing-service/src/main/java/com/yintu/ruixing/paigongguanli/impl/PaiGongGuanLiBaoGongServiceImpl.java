package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiBaoGongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/10/9 11:07
 * @Version 1.0
 * 需求:报工
 */
@Service
@Transactional
public class PaiGongGuanLiBaoGongServiceImpl implements PaiGongGuanLiBaoGongService {
    @Autowired
    private PaiGongGuanLiBaoGongDao paiGongGuanLiBaoGongDao;

    @Override
    public void deleteBaoGongByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiBaoGongDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editBaoGongById(String username, Integer senderid, PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity) {
        paiGongGuanLiBaoGongEntity.setUpdatename(username);
        paiGongGuanLiBaoGongEntity.setUpdatetime(new Date());
        paiGongGuanLiBaoGongDao.updateByPrimaryKeySelective(paiGongGuanLiBaoGongEntity);
    }

    @Override
    public List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(Integer page, Integer size, Integer senderid, Date datetime) {
        return paiGongGuanLiBaoGongDao.findAllBaoGongByUid(senderid,datetime);
    }

    @Override
    public void addBaoGong(PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity, String username, Integer senderid) {
        Date nowDay=new Date();
        if (paiGongGuanLiBaoGongEntity.getUserid()==null){
            paiGongGuanLiBaoGongEntity.setUserid(senderid);
        }
        if (paiGongGuanLiBaoGongEntity.getDatetime()==null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date parse = null;
            try {
                parse = sdf.parse(sdf.format(nowDay));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            paiGongGuanLiBaoGongEntity.setDatetime(parse);
        }
        paiGongGuanLiBaoGongEntity.setUid(senderid);
        paiGongGuanLiBaoGongEntity.setCreatename(username);
        paiGongGuanLiBaoGongEntity.setCreatetime(nowDay);

        paiGongGuanLiBaoGongDao.insertSelective(paiGongGuanLiBaoGongEntity);
    }


}