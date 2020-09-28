package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
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
 * 售前文件技术支持
 *
 * @author:mlf
 * @date:2020/7/1 10:55
 */
@Controller
@RequestMapping("/pre/sales/files")
public class PreSaleFileController extends SessionController {

    @Autowired
    private PreSaleFileService preSaleFileService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditConfigurationService auditConfigurationService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated PreSaleFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        entity.setUserId(this.getLoginUserId().intValue());
        preSaleFileService.add(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加售前技术支持文件信息成功");
    }


    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        preSaleFileService.remove(ids);
        return ResponseDataUtil.ok("删除售前技术支持文件信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(Integer id, @Validated PreSaleFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        preSaleFileService.edit(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("更新售前技术支持文件信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        PreSaleFileEntity preSaleFileEntity = preSaleFileService.findPreSaleById(id);
        return ResponseDataUtil.ok("查询售前技术支持文件信息成功", preSaleFileEntity);
    }


    @GetMapping("/search")
    @ResponseBody
    public Map<String, Object> findPreSaleIdAndNameAndType(@RequestParam("page_number") Integer pageNumber,
                                                           @RequestParam("page_size") Integer pageSize,
                                                           @RequestParam(value = "order_by", required = false, defaultValue = "psf.id DESC") String orderBy,
                                                           @RequestParam(value = "project_id", required = false) Integer preSaleId,
                                                           @RequestParam(value = "file_name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) String type) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<PreSaleFileEntity> preSaleFileEntities = preSaleFileService.findByPreSaleIdAndNameAndType(preSaleId, name, type, this.getLoginUserId().intValue());
        PageInfo<PreSaleFileEntity> pageInfo = new PageInfo<>(preSaleFileEntities);
        return ResponseDataUtil.ok("查询售前技术支持文件信息列表成功", pageInfo);
    }


    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "售前技术支持列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        preSaleFileService.exportFile(response.getOutputStream(), ids, this.getLoginUserId().intValue());
    }

    /**
     * 查询所有用户
     *
     * @param trueName 真实姓名模糊查询
     * @return 用户列表
     */
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
        preSaleFileService.audit(id, isPass, reason, this.getLoginUserId().intValue(), this.getLoginUserName(), this.getLoginTrueName());
        return ResponseDataUtil.ok("审核售前技术支持文件信息成功");
    }

}
