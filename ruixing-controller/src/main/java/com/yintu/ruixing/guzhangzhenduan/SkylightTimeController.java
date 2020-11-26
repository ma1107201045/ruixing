package com.yintu.ruixing.guzhangzhenduan;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/8/18 19:29
 */
@Controller
@RequestMapping("/skylight/times")
public class SkylightTimeController extends SessionController {
    @Autowired
    private SkylightTimeService skylightTimeService;
    @Autowired
    private CheZhanService cheZhanService;
    @Autowired
    private QuDuanBaseService quDuanBaseService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated SkylightTimeEntity entity, Integer[] qdIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        skylightTimeService.add(entity, qdIds);
        return ResponseDataUtil.ok("添加天窗时间信息成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        skylightTimeService.remove(ids);
        return ResponseDataUtil.ok("删除天窗时间信息成功");
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "st.id DESC") String orderBy,
                                       @RequestParam(value = "cz_id", required = false) Integer czId,
                                       @RequestParam(value = "start_time", required = false) Date startTime,
                                       @RequestParam(value = "end_time", required = false) Date endTime) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SkylightTimeEntity> skylightTimeEntities = skylightTimeService.findByCondition(null, startTime, endTime, czId);
        PageInfo<SkylightTimeEntity> pageInfo = new PageInfo<>(skylightTimeEntities);
        return ResponseDataUtil.ok("查询天窗时间列表信息成功", pageInfo);
    }

    @GetMapping("/chezhans")
    @ResponseBody
    public Map<String, Object> findChezhan() {
        List<CheZhanEntity> cheZhanEntities = cheZhanService.findAll();
        return ResponseDataUtil.ok("查询车站列表信息成功", cheZhanEntities);
    }

    @GetMapping("/quduans")
    @ResponseBody
    public Map<String, Object> findQuduanByczId(@RequestParam Integer czId) {
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, null);
        return ResponseDataUtil.ok("查询区段列表信息成功", quDuanBaseEntities);
    }

    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = skylightTimeService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("导入天窗时间列表信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importData(@RequestBody String[][] context) {
        skylightTimeService.importData(context, this.getLoginUserName());
        return ResponseDataUtil.ok("导入天窗时间列表信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "天窗时间列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        skylightTimeService.templateFile(response.getOutputStream());
    }
}
