package com.yintu.ruixing.entity;

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

    List<XianDuanEntity> xianDuanEntities;

    private TieLuJuEntity tieLuJuEntity;

}
