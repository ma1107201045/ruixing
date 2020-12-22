package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 铁路局
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TieLuJuEntity {
    private long tid;

    private long tljId;

    private String tljName;

    private String tljMiaoShu;

    private String dwdName;

    private Long label;

    List<DianWuDuanEntity> dianWuDuanEntities;

}
