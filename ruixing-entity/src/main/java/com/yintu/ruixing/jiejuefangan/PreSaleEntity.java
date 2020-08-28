package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSaleEntity implements Serializable {

    private static final long serialVersionUID = 2236622084285772960L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotEmpty
    private String projectName;

    private Short projectStatus;

    private Date projectDate;

    private Short taskStatus;

    private Date taskFinishDate;

    private String remark;

}