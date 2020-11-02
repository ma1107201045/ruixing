package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseOperatingRecordEntity implements Serializable {
    private static final long serialVersionUID = 5397749690103966235L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String backupFileName;

    private String backupFilePath;

}