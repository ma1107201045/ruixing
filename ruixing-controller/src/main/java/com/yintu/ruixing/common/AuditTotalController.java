package com.yintu.ruixing.common;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/21 16:10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/audit/total")
public class AuditTotalController extends SessionController {

    @Autowired
    private AuditTotalService auditTotalService;

    @PutMapping
    public Map<String, Object> audit(@Validated AuditDto auditDto) {
        auditDto.setLoginUserId(this.getLoginUserId().intValue());
        auditDto.setUserName(this.getLoginUserName());
        auditDto.setTrueName(this.getLoginTrueName());
        auditTotalService.audit(auditDto);
        return ResponseDataUtil.ok("审核成功");
    }

    @GetMapping("/auditors")
    public Map<String, Object> getAuditors(@Validated AuditDto auditDto) {
        auditDto.setLoginUserId(this.getLoginUserId().intValue());
        auditTotalService.findByOtherAuditors(auditDto);
        return ResponseDataUtil.ok("查询审核人列表信息成功");
    }

    @GetMapping
    public Map<String, Object> getPage(@RequestParam("page_num") int pageNum, @RequestParam("page_size") int pageSize, @Validated AuditTotalDto auditTotalDto) {
        auditTotalDto.setLoginUserId(this.getLoginUserId().intValue());
        PageInfo<AuditTotalVo> pageInfo = auditTotalService.findPage(pageNum, pageSize, auditTotalDto);
        return ResponseDataUtil.ok("查询审批列表信息成功", pageInfo);
    }

}
