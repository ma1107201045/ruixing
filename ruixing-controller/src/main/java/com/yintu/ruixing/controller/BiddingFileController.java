package com.yintu.ruixing.controller;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.BiddingEntity;
import com.yintu.ruixing.entity.BiddingFileEntity;
import com.yintu.ruixing.service.BiddingFileService;
import com.yintu.ruixing.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/2 14:56
 */
@Controller
@RequestMapping("/biddings/files")
public class BiddingFileController extends SessionController implements BaseController<BiddingFileEntity, Integer> {
    @Autowired
    private BiddingService biddingService;
    @Autowired
    private BiddingFileService biddingFileService;


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(BiddingFileEntity entity) {
        if (entity.getBiddingId() == null)
            throw new BaseRuntimeException("投招标技术支持id不能为空");
        biddingFileService.add(entity);
        return ResponseDataUtil.ok("添加招投标技术支持文件信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        biddingFileService.remove(ids);
        return ResponseDataUtil.ok("删除招投标技术支持文件信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, BiddingFileEntity entity) {
        if (entity.getBiddingId() == null)
            throw new BaseRuntimeException("投招标技术支持id不能为空");
        biddingFileService.edit(entity);
        return ResponseDataUtil.ok("修改招投标技术支持文件信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        BiddingFileEntity biddingFileEntity = biddingFileService.findById(id);
        Integer biddingId = biddingFileEntity.getBiddingId();
        if (biddingId != null) {
            BiddingEntity biddingEntity = biddingService.findById(biddingId);
            biddingFileEntity.setBiddingEntity(biddingEntity);
        }
        return ResponseDataUtil.ok("查询招投标技术支持文件信息成功", biddingFileEntity);
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = FileUploadUtil.save(multipartFile.getInputStream(), fileName);
        JSONObject jo = new JSONObject();
        jo.put("filePath", filePath);
        jo.put("fileName", fileName);
        return ResponseDataUtil.ok("上传招投标技术支持文件信息成功", jo);
    }

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        BiddingFileEntity biddingFileEntity = biddingFileService.findById(id);
        if (biddingFileEntity != null) {
            String filePath = biddingFileEntity.getPath();
            String fileName = biddingFileEntity.getName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "投招标技术支持列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        biddingFileService.exportFile(response.getOutputStream(), ids);
    }
}