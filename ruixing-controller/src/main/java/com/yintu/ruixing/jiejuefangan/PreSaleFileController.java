package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private SolutionLogService solutionLogService;
    @Autowired
    private UserService userService;


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

    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String trueName) {
        List<UserEntity> userEntities = userService.findByTruename(trueName);
        return ResponseDataUtil.ok("查询审核人列表信息成功", userEntities);
    }


    @GetMapping("/{id}/log")
    @ResponseBody
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 1, (short) 2, id, null));
        return ResponseDataUtil.ok("查询售前技术支持文件日志信息列表成功", solutionLogEntities);
    }

}
