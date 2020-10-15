package com.yintu.ruixing.guzhangzhenduan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.POIUtils;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/26 16:37
 * @Version 1.0
 * 需求: 报警预警的基础信息表
 */
@RestController
@RequestMapping("/BaoJingYuJingBaseAll")
public class BaoJingYuJingBaseController extends SessionController {
    @Autowired
    private BaoJingYuJingBaseService baoJingYuJingBaseService;

    //新增报警或预警信息
    @PostMapping("/addBaoJing")
    public Map<String, Object> addBaoJing(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity) {
        Integer senderid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        baoJingYuJingBaseService.addBaoJing(baoJingYuJingBaseEntity, username);
        return ResponseDataUtil.ok("新增报警数据成功");
    }

    //初始化页面
    @GetMapping("/findAllBaoJing")
    public Map<String, Object> findAllBaoJing(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findAllBaoJing(page, size);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }

    //根据id  编辑对应的报警预警数据
    @PutMapping("/editBJYJDataByid/{id}")
    public Map<String, Object> editBJYJDataByid(@PathVariable Integer id, BaoJingYuJingBaseEntity baoJingYuJingBaseEntity) {
        String username = this.getLoginUser().getTrueName();
        baoJingYuJingBaseService.editBJYJDataByid(baoJingYuJingBaseEntity, username);
        return ResponseDataUtil.ok("编辑数据成功");
    }

    //根据id  单个或者批量删除数据
    @DeleteMapping("/deleteBJYJdDataByids/{ids}")
    public Map<String, Object> deleteBJYJdDataByids(@PathVariable Integer[] ids) {
        baoJingYuJingBaseService.deleteBJYJdDataByids(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //查询报警预警数据
    @GetMapping("/findBJYJData")
    public Map<String, Object> findBJYJData(Integer page, Integer size, String context, Integer bjyjType) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findBJYJData(page, size, context, bjyjType);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }

    //根据输入内容 查询报警的数据
    @GetMapping("/findBJDataBySomething")
    public Map<String, Object> findBJDataBySomething(Integer page, Integer size, String context) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findBJDataBySomething(page, size, context);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }

    //模板下载
    @GetMapping("/downloads")
    public void downloadFile(HttpServletResponse response) throws IOException {
        //String filePath ="C:\\data\\ruixing\\templates";
        String fileName = "车站信息配置模板.xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        FileUploadUtil.getTemplateFile(response.getOutputStream(), fileName);
    }

    //预警报警的Excel数据预览
    @PostMapping("/YuJingBaoJingYuLan")
    @ResponseBody
    public Map<String, Object> QuDuanYuLan(@RequestParam("file") MultipartFile excelFile) {
        List<String[]> datas = new ArrayList<>();
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list.size() > 0) {
                for (String[] strings : list) {
                    if (strings[0] != null && strings[1] != null && strings[2] != null && strings[3] != null) {
                        if (!strings[0].equals("") || !strings[1].equals("") || !strings[2].equals("") || !strings[3].equals("") ) {
                            String[] strings1 = strings;
                            datas.add(strings1);
                        } else {
                            return ResponseDataUtil.error("请选择正确的Excel数据");
                        }

                    }
                }
            }
        } catch (IOException e) {
            return ResponseDataUtil.error("文件上传失败");
        }
        return ResponseDataUtil.ok("文件上传成功", datas);
    }

    //


}
