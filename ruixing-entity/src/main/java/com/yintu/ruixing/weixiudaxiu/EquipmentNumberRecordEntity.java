package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentNumberRecordEntity implements Serializable {
    private static final long serialVersionUID = -9136498218286625735L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String equipmentNumber;

    private String name;

    private String configuration;
    @NotNull
    private Integer equipmentNumberId;
    

}