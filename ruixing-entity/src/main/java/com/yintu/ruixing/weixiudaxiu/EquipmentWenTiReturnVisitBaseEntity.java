package com.yintu.ruixing.weixiudaxiu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitBaseEntity {
    private Integer id;

    private Integer adduserid;
    private Integer tljid;
    private Integer dwdid;
    private String xdid;

    private String renwunumber;

    private String tljname;

    private String dwdname;

    private String xdname;

    private String returnUserName;

    private Integer returnuserid;

    private String returnedusername;

    private String returnedusernamephone;

    private Integer returncycletype;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date renwustarttime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date rewuendtime;

    private Integer implementstate;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}