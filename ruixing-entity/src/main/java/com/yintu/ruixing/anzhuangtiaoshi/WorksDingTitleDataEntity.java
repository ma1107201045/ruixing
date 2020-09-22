package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorksDingTitleDataEntity {
    private Integer id;

    private Integer czid;

    private Integer wntid;

    private Integer wnlid;

    private String czName;//车站名
    private String aworkName;//作业项名字
    private String bworkDoerAndTime;//作业人和标记时间
    private String cworkBiaoShi;//作业标识
    private String dworkState;//作业状态
    private JSONObject list;













}