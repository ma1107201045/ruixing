package com.yintu.ruixing.yuanchengzhichi;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTicketEntityWithBLOBs extends AlarmTicketEntity {
    private static final long serialVersionUID = -7889900814571145488L;
    private String reasonAnalysis;

    private String treatmentMeasure;

    private String dataFeature;

    private String faultEquipment;


}