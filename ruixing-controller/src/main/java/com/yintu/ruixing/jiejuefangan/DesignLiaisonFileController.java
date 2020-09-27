package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设计联络以及后续文件技术交流
 *
 * @author:mlf
 * @date:2020/7/3 14:01
 */
@Controller
@RequestMapping("/design/liaisons/files")
public class DesignLiaisonFileController extends SessionController {

    @Autowired
    private DesignLiaisonFileService designLiaisonFileService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated DesignLiaisonFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        entity.setUserId(this.getLoginUserId().intValue());
        designLiaisonFileService.add(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加设计联络及后续技术交流文件信息成功");
    }


    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        designLiaisonFileService.remove(ids);
        return ResponseDataUtil.ok("删除设计联络及后续技术交流文件信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated DesignLiaisonFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        designLiaisonFileService.edit(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改设计联络及后续技术交流文件信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        DesignLiaisonFileEntity designLiaisonFileEntity = designLiaisonFileService.findDesignLiaisonById(id);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流文件信息成功", designLiaisonFileEntity);
    }

    @GetMapping("/search")
    @ResponseBody
    public Map<String, Object> findBySearch(@RequestParam("page_number") Integer pageNumber,
                                            @RequestParam("page_size") Integer pageSize,
                                            @RequestParam(value = "order_by", required = false, defaultValue = "dlf.id DESC") String orderBy,
                                            @RequestParam(value = "project_id", required = false) Integer designLiaisonId,
                                            @RequestParam(value = "file_name", required = false) String name,
                                            @RequestParam(value = "type", required = false) String type) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<DesignLiaisonFileEntity> designLiaisonEntities = designLiaisonFileService.findByDesignLiaisonIdIdAndNameAndType(designLiaisonId, name, type, this.getLoginUserId().intValue());
        PageInfo<DesignLiaisonFileEntity> pageInfo = new PageInfo<>(designLiaisonEntities);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息以及文件信息列表成功", pageInfo);
    }


    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "设计联络及后续技术交流列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        designLiaisonFileService.exportFile(response.getOutputStream(), ids, this.getLoginUserId().intValue());
    }

    @GetMapping("/{id}/log")
    @ResponseBody
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 3, (short) 2, id, null));
        return ResponseDataUtil.ok("查询设计联络及后续技术交流文件日志信息列表成功", solutionLogEntities);
    }

    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String trueName) {
        List<UserEntity> userEntities = userService.findByTruename(trueName);
        userEntities = userEntities
                .stream()
                .filter(userEntity -> !userEntity.getId().equals(this.getLoginUserId()))
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询审核人列表信息成功", userEntities);
    }

    /**
     * 查询配置的用户
     *
     * @return 用户列表
     */
    @GetMapping("/audit/configurations")
    @ResponseBody
    public Map<String, Object> findAuditConfigurations() {
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationService.findByExample(null, null, (short) 1);
        auditConfigurationEntities.forEach(auditConfigurationEntity -> auditConfigurationEntity.setUserEntities(auditConfigurationEntity.getUserEntities().stream()
                .filter(userEntity -> !userEntity.getId().equals(this.getLoginUserId()))
                .sorted(Comparator.comparing(UserEntity::getId).reversed())
                .collect(Collectors.toList()))
        );
        return ResponseDataUtil.ok("查询审批流配置信息列表成功", auditConfigurationEntities);
    }

    /**
     * 审核文件
     *
     * @param id     文件id
     * @param isPass 是否 审核状态 1.待审核 2.已审核未通过 3.已审核已通过
     * @param reason 已审核未通过
     * @return 已审核未通过理由
     */
    @PutMapping("/audit/{id}")
    @ResponseBody
    public Map<String, Object> audit(@PathVariable Integer id, @RequestParam("isPass") Short isPass, String reason) {
        designLiaisonFileService.audit(id, isPass, reason, this.getLoginUserId().intValue(), this.getLoginUserName(), this.getLoginTrueName());
        return ResponseDataUtil.ok("审核设计联络及后续技术交流文件信息成功");
    }


}
