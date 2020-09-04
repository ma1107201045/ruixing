package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Mr.liu
 * @Date 2020/7/7 16:12
 * @Version 1.0
 * 需求:产品交付项目文件和审核人的中间表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanPinJiaoFuFileAuditorEntity {
    private Integer id;

    private Integer chanPinJiaoFuFileId;//文件id

    private Integer auditorId;//审核人id

    private Integer isPass;//是否通过 0.未通过 1.已通过
    private Integer objectType;//是否通过 0.未通过 1.已通过


}
