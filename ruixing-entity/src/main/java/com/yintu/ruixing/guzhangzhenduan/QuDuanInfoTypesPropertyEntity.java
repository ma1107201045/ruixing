package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuDuanInfoTypesPropertyEntity implements Serializable {
    private static final long serialVersionUID = 6028143256653580528L;
    private Integer id;

    private String types;

    private Integer propertyId;

    private QuDuanInfoPropertyEntity quDuanInfoPropertyEntity;

}