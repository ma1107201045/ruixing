package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Mr.liu
 * @Date 2020/9/15 16:18
 * @Version 1.0
 * 需求:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AZTSXiangMuServiceChooseEntity {

    //项目
    private Integer id;

    private Integer worksid; //作业项配置id

    private Date xianduantime;//线段创建的时间

    private String tljName;//铁路局名

    private String dwdName;//电务段名

    private String xdName;//线段名

    private String xdType;//项目类型

    private Integer xdFenlei;//线段状态 1：正在进行 ，2：已完成，3：长期停滞

    private String guanlianxiangmu;//关联项目名和编号

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    //中间表
    private Integer xdid;

    private Integer serid;

    private Integer choid;

    private Integer czid;

    private String chezhanname;

    private Integer typetime;

    private Integer isnot;

    private Date planStartTime;

    private Date planEndTime;

    private Date actualStartTime;

    private Date actualEndTime;

    private Date planOpenTime;

    private Date actualOpenTime;

    //小choose
    private Integer sid;

    private Object name;

    private Integer isNotDaoHuo;

    //choose类型
    private String servicename;

    private String choose;

    private Integer timetype;

    private Integer isNotOver;
}
