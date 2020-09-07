package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.MenXianPropertyService;
import com.yintu.ruixing.guzhangzhenduan.MenXianService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/12 11:54
 */
@Service
@Transactional
public class MenXianServiceImpl implements MenXianService {
    @Autowired
    private MenXianDao menXianDao;

    @Autowired
    private MenXianPropertyService menXianPropertyService;

    @Autowired
    private QuDuanInfoService quDuanInfoService;

    @Override
    public void add(MenXianEntity menXianEntity) {
        Integer quDuanId = menXianEntity.getQuduanId();
        QuDuanInfoEntityV2 quDuanInfoEntity = new QuDuanInfoEntityV2();
        if (quDuanInfoEntity != null) {
            Integer propertyId = menXianEntity.getPropertyId();
            MenXianPropertyEntity menXianPropertyEntity = menXianPropertyService.findById(propertyId);
            if (menXianPropertyEntity != null) {
                MenXianEntity menXianEntity1 = this.findByQuDuanIdAndPropertyId(quDuanId, propertyId);
                if (menXianEntity1 == null) {
                    String name = menXianPropertyEntity.getName();
                    String measuredValue;
                    String measuredValueUnit;
                    switch (name) {
                        case "功出电压V":
                            measuredValue = quDuanInfoEntity.getVOutZhu().toString();
                            measuredValueUnit = "V";
                            break;
                        case "功出电流mA":
                            measuredValue = quDuanInfoEntity.getMaOutZhu().toString();
                            measuredValueUnit = "mA";
                            break;
                        case "上边频Hz":
                            measuredValue = quDuanInfoEntity.getHzUpZhu().toString();
                            measuredValueUnit = "Hz";
                            break;
                        case "下边频Hz":
                            measuredValue = quDuanInfoEntity.getHzDownZhu().toString();
                            measuredValueUnit = "Hz";
                            break;
                        case "发送低频Hz":
                            measuredValue = quDuanInfoEntity.getHzLowZhu().toString();
                            measuredValueUnit = "Hz";
                            break;
                        case "送端电缆侧电压V":
                            measuredValue = quDuanInfoEntity.getVSongduanCable().toString();
                            measuredValueUnit = "V";
                            break;
                        case "送端电缆侧电流mA":
                            measuredValue = quDuanInfoEntity.getMaSongduanCable().toString();
                            measuredValueUnit = "mA";
                            break;
                        case "受端电缆侧主电压V":
                            measuredValue = quDuanInfoEntity.getVShouduanCableHost().toString();
                            measuredValueUnit = "V";
                            break;
                        case "受端电缆侧调电压V":
                            measuredValue = quDuanInfoEntity.getVShouduanCableSpare().toString();
                            measuredValueUnit = "V";
                            break;
                        case "轨入电压V":
                            measuredValue = quDuanInfoEntity.getVInAll().toString();
                            measuredValueUnit = "V";
                            break;
                        case "主接入电压mV":
                            measuredValue = quDuanInfoEntity.getMvInZhu().toString();
                            measuredValueUnit = "mV";
                            break;

                        case "调接入电压mV":
                            measuredValue = quDuanInfoEntity.getMvInDiaoZhu().toString();
                            measuredValueUnit = "mV";
                            break;
                        case "接收低频Hz":
                            measuredValue = quDuanInfoEntity.getHzInLowZhu().toString();
                            measuredValueUnit = "Hz";
                            break;
                        case "FBP电缆电流mA":
                            measuredValue = quDuanInfoEntity.getMaCableFbp().toString();
                            measuredValueUnit = "mA";
                            break;
                        case "FBP长内电流A":
                            measuredValue = quDuanInfoEntity.getALonginFbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBP长外电流A":
                            measuredValue = quDuanInfoEntity.getALongoutFbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBP短内电流A":
                            measuredValue = quDuanInfoEntity.getAShortinFbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBP短外电流A":
                            measuredValue = quDuanInfoEntity.getAShortoutFbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBP温度°C":
                            measuredValue = quDuanInfoEntity.getTFbp().toString();
                            measuredValueUnit = "°C";
                            break;
                        case "FBA长内电流A":
                            measuredValue = quDuanInfoEntity.getALonginFbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBA长外电流A":
                            measuredValue = quDuanInfoEntity.getALongoutFbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBA短内电流A":
                            measuredValue = quDuanInfoEntity.getAShortinFbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "FBA短外电流A":
                            measuredValue = quDuanInfoEntity.getAShortoutFbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBA长内电流A":
                            measuredValue = quDuanInfoEntity.getALonginJbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBA长外电流A":
                            measuredValue = quDuanInfoEntity.getALongoutJbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBA短内电流A":
                            measuredValue = quDuanInfoEntity.getAShortinJbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBA短外电流A":
                            measuredValue = quDuanInfoEntity.getAShortoutJbaZhu().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBP电缆电流mA":
                            measuredValue = quDuanInfoEntity.getMaCableJbp().toString();
                            measuredValueUnit = "mA";
                            break;
                        case "JBP长内电流A":
                            measuredValue = quDuanInfoEntity.getALonginJbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBP长外电流A":
                            measuredValue = quDuanInfoEntity.getALongoutJbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBP短内电流A":
                            measuredValue = quDuanInfoEntity.getAShortinJbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBP短外电流A":
                            measuredValue = quDuanInfoEntity.getAShortoutJbp().toString();
                            measuredValueUnit = "A";
                            break;
                        case "JBP温度°C":
                            measuredValue = quDuanInfoEntity.getTJbp().toString();
                            measuredValueUnit = "°C";
                            break;
                        default:
                            measuredValue = new BigDecimal(0.0).toString();
                            measuredValueUnit = "";
                    }
                    menXianEntity.setMeasuredValue(measuredValue);
                    menXianEntity.setMeasuredValueUnit(measuredValueUnit);
                    menXianDao.insertSelective(menXianEntity);
                }
            }
        }

    }

    @Override
    public void remove(Integer id) {
        menXianDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MenXianEntity menXianEntity) {
        Integer quDuanId = menXianEntity.getQuduanId();
        QuDuanInfoEntity quDuanInfoEntity = null;
        if (quDuanInfoEntity != null) {
            Integer propertyId = menXianEntity.getPropertyId();
            MenXianPropertyEntity menXianPropertyEntity = menXianPropertyService.findById(propertyId);
            if (menXianPropertyEntity != null) {
                MenXianEntity menXianEntity1 = this.findByQuDuanIdAndPropertyId(quDuanId, propertyId);
                if (menXianEntity1 != null && menXianEntity.getId().equals(menXianEntity1.getId())) {
                    menXianDao.updateByPrimaryKeySelective(menXianEntity);
                }
            }

        }

        menXianDao.updateByPrimaryKeySelective(menXianEntity);
    }

    @Override
    public MenXianEntity findById(Integer id) {
        return menXianDao.selectByPrimaryKey(id);
    }

    @Override
    public MenXianEntity findByQuDuanIdAndPropertyId(Integer quDuanId, Integer propertyId) {
        return menXianDao.selectByQuDuanIdAndPropertyId(quDuanId, propertyId);
    }

    @Override
    public List<MenXianEntity> findByPropertyIds(Integer[] propertyIds) {
        return menXianDao.selectByPropertyIds(propertyIds);
    }

}
