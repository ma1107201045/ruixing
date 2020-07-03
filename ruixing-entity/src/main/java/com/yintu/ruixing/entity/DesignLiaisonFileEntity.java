package com.yintu.ruixing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 设计联络以及后续技术交流 项目文件实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignLiaisonFileEntity implements Serializable {

    private static final long serialVersionUID = 2040050047670489042L;

    private Integer id;

    private String name;

    private String path;

    private Date uploadDatetime;

    private Short type;

    private Short releaseStatus;

    private Integer auditorId;

    private Integer designLiaisonId;

    private DesignLiaisonEntity designLiaisonEntity;
}