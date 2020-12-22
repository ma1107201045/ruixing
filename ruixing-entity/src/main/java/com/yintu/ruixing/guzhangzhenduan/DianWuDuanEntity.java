package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 电务段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DianWuDuanEntity {
    private long did;

    private long tid;

    private long dwdId;

    private long tljId;

    private String dwdName;

    private long tljDwdId;

    private String dwdMiaoShu;

    private String yuliu1;

    private String yuliu2;

    private Long label;

    List<XianDuanEntity> xianDuanEntities;

    List<CheZhanEntity> cheZhanEntities;

    private TieLuJuEntity tieLuJuEntity;
    private Integer dwdState; //车站配置状态  0：未配置    1：已配置
    private String dwdJson; //车站储存json
}
