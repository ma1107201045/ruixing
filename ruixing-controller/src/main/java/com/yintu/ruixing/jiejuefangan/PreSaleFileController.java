package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated PreSaleFileEntity preSaleFileEntity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        preSaleFileService.add(preSaleFileEntity, auditorIds);
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
    public Map<String, Object> edit(Integer id, @Validated PreSaleFileEntity preSaleFileEntity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        preSaleFileService.edit(preSaleFileEntity, auditorIds);
        return ResponseDataUtil.ok("更新售前技术支持文件信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        PreSaleFileEntity preSaleFileEntity = preSaleFileService.findPreSaleById(id);
        return ResponseDataUtil.ok("查询售前技术支持文件信息成功", preSaleFileEntity);
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "售前技术支持列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        preSaleFileService.exportFile(response.getOutputStream(), ids);
    }

    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String truename) {
        List<UserEntity> userEntities = preSaleFileService.findUserEntitiesByTruename(truename);
        return ResponseDataUtil.ok("查询审核人列表信息成功", userEntities);
    }


    @GetMapping("/log")
    public Map<String, Object> findLogByExample(@RequestParam("recordTypeId") Integer recordTypeId) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 1, (short) 1, recordTypeId, null));
        return ResponseDataUtil.ok("查询售前技术支持项目日志信息列表成功", solutionLogEntities);
    }

}
