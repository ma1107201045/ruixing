package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusEntityWithBLOBs extends LineTechnologyStatusEntity {
    private static final long serialVersionUID = 897458342150784193L;

    private String guaranteeMessage;

    private String reorganize;

}