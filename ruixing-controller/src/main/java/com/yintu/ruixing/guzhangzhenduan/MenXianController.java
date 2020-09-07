package com.yintu.ruixing.guzhangzhenduan;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/12 11:56
 */
@RestController
@RequestMapping("/menxians")
public class MenXianController extends SessionController {
    @Autowired
    private MenXianService menXianService;
    @Autowired
    private QuDuanInfoService quDuanInfoService;


    @PostMapping
    public Map<String, Object> add(@Validated MenXianEntity menXianEntity) {
        menXianService.add(menXianEntity);
        return ResponseDataUtil.ok("添加门限参数信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        menXianService.remove(id);
        return ResponseDataUtil.ok("删除门限参数信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated MenXianEntity menXianEntity) {
        menXianService.edit(menXianEntity);
        return ResponseDataUtil.ok("修改门限参数信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        MenXianEntity menXianEntity = menXianService.findById(id);
        return ResponseDataUtil.ok("查询门限参数信息成功", menXianEntity);
    }

    /**
     * 查询门限参数属性树成功
     */
    @RequestMapping("/tree")
    public Map<String, Object> findTreeByCzId(@RequestParam("czId") Integer czId) {
        List<TreeNodeUtil> treeNodeUtils = quDuanInfoService.findPropertiesTree(czId);
        return ResponseDataUtil.ok("查询门限参数属性树成功", treeNodeUtils);
    }

    @GetMapping("/list")
    public Map<String, Object> findByCzIdAndProperties(@RequestParam("czId") Integer czId, @RequestParam("properties") Integer[] properties) {
        JSONObject jo = menXianService.findByCzIdAndProperties(czId, properties);
        return ResponseDataUtil.ok("查询门限列表成功", jo);
    }

    @PostMapping("/import")
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = menXianService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("导入门限参数信息列表成功", context);
    }

    @PostMapping("/import/data")
    public Map<String, Object> importData(@RequestBody String[][] context) {
        JSONArray ja = (JSONArray) JSONArray.toJSON(context);
        menXianService.importData(ja, this.getLoginUserName());
        return ResponseDataUtil.ok("导入门限参数信息列表成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "门限参数列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        menXianService.templateFile(response.getOutputStream());
    }

}
