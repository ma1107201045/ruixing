package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 线段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XianDuanEntity {
    private Long xid;

    private Long tid;

    private Long did;

    private Long xdId;

    private Long dwdId;

    private String xdName;

    private String xdZgName;

    private Long dwdXdId;

    private  String xdMiaoShu;

    private String yuliu1;

    private String yuliu2;

    private Long xdType;

    private String dwdName;

    private Integer xdState;//线段设置状态  0：未设置  1：已设置

    private String xdJson;//储存的json

    private Long label;

    List<CheZhanEntity> cheZhanEntities;

    private DianWuDuanEntity dianWuDuanEntity;

    private TieLuJuEntity tieLuJuEntity;

}
