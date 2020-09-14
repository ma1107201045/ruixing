package com.yintu.ruixing.yunxingweihu;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/spare/parts/infos")
public class SparePartsInfoController extends SessionController {
    @Autowired
    private SparePartsInfoService sparePartsInfoService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated SparePartsInfoEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        sparePartsInfoService.add(entity);
        return ResponseDataUtil.ok("添加备品试验记录信息成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        sparePartsInfoService.remove(ids);
        return ResponseDataUtil.ok("删除备品试验记录信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        SparePartsInfoEntity sparePartsInfoEntity = sparePartsInfoService.findById(id);
        return ResponseDataUtil.ok("查询备品试验记录信息成功", sparePartsInfoEntity);
    }


    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = sparePartsInfoService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("获取导入备品试验记录信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importData(@RequestBody JSONObject jo) {
        Integer sparePartsId = jo.getInt("sparePartsId");
        String[][] context = jo.get("data", String[][].class);
        sparePartsInfoService.importData(sparePartsId, context, this.getLoginUserName());
        return ResponseDataUtil.ok("导入备品试验记录信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "备品试验记录信息列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsInfoService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "备品试验记录信息列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsInfoService.exportFile(response.getOutputStream(), ids);
    }
}
