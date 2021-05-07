package com.yintu.ruixing.weixiudaxiu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitEntity {
    private Integer id;

    private Integer renwubaseid;

    private String renwunumber;

    private String recordnumber;

    private Integer tljid;

    private Integer dwdid;

    private String xdid;

    private String tljname;

    private String dwdname;

    private String xdname;

    private Integer returnuserid;

    private String returnUsername;

    private String returnedusername;

    private String returnedusernamephone;

    private Integer renwustate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date returntime;

    private String returnwenti;

    private String replymessage;

    private Integer implementstate;

    private Integer wentistate;

    private Integer pushstate;

    private Integer returncycletype;//回访周期类型：1：每周，2：每月，3：每季度，4：每年',

    private String years;

    private Integer typenumber;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private Integer editState;

    private Date begintime;

    private List<EquipmentWenTiReturnVisitFileEntity> wenTiFileList;
    private List<EquipmentWenTiReturnVisitFileEntity> returnFileList;
    private String pushNumber;

}