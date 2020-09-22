package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileEntity;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/27 15:48
 * @Version 1.0
 * 需求: 报工管理
 */
@RestController
@RequestMapping("/BaoGongGuanLiAll")
public class PaiGongGuanLiBaoGongGuanLiController {
    @Autowired
    private PaiGongGuanLiBaoGongGuanLiService paiGongGuanLiBaoGongGuanLiService;

    @Autowired
    private UserService userService;

    //查询员工姓名  等信息
    @GetMapping("/findAllUserName")
    public Map<String, Object> findAllUserName(String truename) {
        List<UserEntity> userEntities = userService.findAllOrByUsername(truename);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //初始化页面
    @GetMapping("/findAllBaoGong")
    public Map<String, Object> findAllBaoGong(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityList = paiGongGuanLiBaoGongGuanLiService.findAllBaoGong(page, size);
        PageInfo<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityPageInfo = new PageInfo<>(baoGongGuanLiEntityList);
        return ResponseDataUtil.ok("查询报工数据成功", baoGongGuanLiEntityPageInfo);
    }

    //新增报工
    @PostMapping("/addBaoGong")
    public Map<String, Object> addBaoGong(PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity) {
        paiGongGuanLiBaoGongGuanLiService.addBaoGong(paiGongGuanLiBaoGongGuanLiEntity);
        return ResponseDataUtil.ok("新增报工成功");
    }

    //根据id  编辑对应的报工
    @PutMapping("/editBaoGongById/{id}")
    public Map<String, Object> editBaoGongById(@PathVariable Integer id, PaiGongGuanLiBaoGongGuanLiEntity paiGongGuanLiBaoGongGuanLiEntity) {
        paiGongGuanLiBaoGongGuanLiService.editBaoGongById(paiGongGuanLiBaoGongGuanLiEntity);
        return ResponseDataUtil.ok("编辑报工成功");
    }

    //根据日期 或者 姓名  或者姓名查询对应的报工
    @GetMapping("/findBaoGongBySome")
    public Map<String, Object> findBaoGongBySome(Date daytime, String xiangMuName, String userName, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityList = paiGongGuanLiBaoGongGuanLiService.findBaoGongBySome(daytime, xiangMuName, userName, page, size);
        PageInfo<PaiGongGuanLiBaoGongGuanLiEntity> baoGongGuanLiEntityPageInfo = new PageInfo<>(baoGongGuanLiEntityList);
        return ResponseDataUtil.ok("查询报工数据成功", baoGongGuanLiEntityPageInfo);
    }


    //上传文件
    @PostMapping("/uploads")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = FileUploadUtil.save(multipartFile.getInputStream(), fileName);
        JSONObject jo = new JSONObject();
        jo.put("filePath", filePath);
        jo.put("fileName", fileName);
        return ResponseDataUtil.ok("上传文件成功", jo);
    }

    //根据id 下载文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        PaiGongGuanLiBaoGongGuanLiEntity baoGongGuanLiEntity = paiGongGuanLiBaoGongGuanLiService.findById(id);
        if (baoGongGuanLiEntity != null) {
            String filePath = baoGongGuanLiEntity.getFilePath();
            String fileName = baoGongGuanLiEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }


    //根据id 单个 或者批量删除报工
    @DeleteMapping("/deleteBaoGongByIds/{ids}")
    public Map<String, Object> deleteBaoGongByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiBaoGongGuanLiService.deleteBaoGongByIds(ids);
        return ResponseDataUtil.ok("删除报工成功");
    }

}
