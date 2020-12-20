package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 产品交付的项目实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanPinJiaoFuXiangMuEntity {

    private Integer id;

    private Integer xiangmuState;//项目状态 1：正在执行 ， 2：仅剩尾款，3：项目关闭

    private String xiangmuBianhao;//项目编号

    private String xiangmuName;//项目名

    private Integer xiaoshouState;//销售需求状态  1：前续未办理、2：全部品类/数量已下需求、3：部分品类/数量已下需求',

    private Integer fahuoState;//发货状态 1：项目停滞暂不发货、2：暂未开始发货、3：陆续发货中、4：工程结束不需发货、5：当前订单是否完成',

    private Integer qianshouState;//签收状态 1：前续未办理、2：公司暂存不签收、3：已转储待签收、4：完成签收',

    private Integer yangongState;//验工状态 1：是否需要验工、2：前续未办理、3：待验工、4：顾客要求暂缓验工、5：完成部分验工、6：完成验工',

    private Integer xianchangfuwu;//是否需要现场服务   0：否 ，1：是',

    private Integer isnotYanGong;//'是否需要验工 0：否，1：是',

    private Date fahuoTixingTime;//发货提醒日期

    private Double xiangMuMoney;//项目钱

    private Double sheBeiMoney;//设备钱

    private String sbName;//设备名

    private Integer auditorid;//审核人id
    private Integer auditorstate;//审核状态 1：待审核 ，2：已审核，3：审核未通过'
    private Date createTime;
    private Date updateTime;
    private String operatorName;
    private String remarks;

    //private ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity;
}