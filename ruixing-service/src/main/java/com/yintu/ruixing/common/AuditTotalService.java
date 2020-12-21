package com.yintu.ruixing.common;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/19 18:07
 * @Version: 1.0
 */
public interface AuditTotalService {


    /**
     * 按照不同模块类型进行审批
     *
     * @param auditDto 前台传递参数
     */
    void audit(AuditDto auditDto);

    /**
     * 转交时获取审批列表
     *
     * @param auditDto 前台传递参数
     * @return 审批人员列表
     */
    List<UserEntity> findByOtherAuditors(AuditDto auditDto);


    /**
     * 综合查询审批列表
     *
     * @param auditTotalDto 前台传递参数
     * @return 列表信息
     */
    PageInfo<AuditTotalVo> findPage(int pageNum, int pageSize, AuditTotalDto auditTotalDto);

    /**
     * @return 解决方案的审批列表
     */
    List<AuditTotalVo> findPreSale(AuditTotalDto auditTotalDto);

    /**
     * @return 投标招标技术支持的审批列表
     */
    List<AuditTotalVo> findBidding(AuditTotalDto auditTotalDto);

    /**
     * @return 设计联络及后续交流的审批列表
     */
    List<AuditTotalVo> findDesignLiaison(AuditTotalDto auditTotalDto);
}
