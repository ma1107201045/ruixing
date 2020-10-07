package com.yintu.ruixing.yuanchengzhichi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteSupportVideoMeetingEntityWithBLOBs extends RemoteSupportVideoMeetingEntity {
    private static final long serialVersionUID = 7244870237005804509L;
    @NotBlank
    private String context;

    private String opinion;


}