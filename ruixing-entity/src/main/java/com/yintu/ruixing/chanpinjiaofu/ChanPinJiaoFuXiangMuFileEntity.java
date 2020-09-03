package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanPinJiaoFuXiangMuFileEntity {
    private Integer id;

    private Integer uid;

    private Integer xmId;

    private Integer fileType;

    private String fileName;

    private String filePath;

    private Integer fabuType;

    private Integer auditorState;

    //private Integer auditorId;

    private Date createtime;

    private Date updatetime;

    private String createname;

    private String updatename;

    private String remarks;

    private Integer auditorid;

}