package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuFileAuditorEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuRecordMessageEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/14 18:03
 * @Version 1.0
 * 需求:安装调试的项目文件
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiFileAll")
public class AnZhuangTiaoShiFileController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiFileService anZhuangTiaoShiFileService;

    //根据 线段id  查询对应的所有的输入文件
    @GetMapping("/findShuRuFileByXid/{id}")
    public Map<String,Object>findShuRuFileByXid(@PathVariable Integer id,Integer page,Integer size){
        List<AnZhuangTiaoShiObjectAuditorEntity> fuFileAuditorEntityList=anZhuangTiaoShiFileService.findXMByid(id);
        if (fuFileAuditorEntityList.size()==0){
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findShuRuFile(id, page, size, uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输入文件成功", fileEntityPageInfo);
        }else {
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findShuRuFilee(id, page, size, uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输入文件成功", fileEntityPageInfo);
        }
    }

    //根据 线段id  查询对应的所有的输出文件
    @GetMapping("/findShuChuFileByXid/{id}")
    public Map<String,Object>findShuChuFileByXid(@PathVariable Integer id,Integer page,Integer size){
        List<AnZhuangTiaoShiObjectAuditorEntity> fuFileAuditorEntityList=anZhuangTiaoShiFileService.findXMByid(id);
        if (fuFileAuditorEntityList.size()==0){
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findShuChuFile(id, page, size, uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
        }else {
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findShuChuFilee(id, page, size, uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
        }
    }


    //根据文件名 查询对应的文件
    @GetMapping("/findFileByNmae")
    public Map<String,Object>findFileByNmae(Integer page,Integer size,Integer xdid,Integer filetype,String filename){
        List<AnZhuangTiaoShiObjectAuditorEntity> fuFileAuditorEntityList=anZhuangTiaoShiFileService.findXMByid(xdid);
        if (fuFileAuditorEntityList.size()==0){
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findFileByNmae(page,size,xdid,filetype,filename,uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
        }else {
            PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<AnZhuangTiaoShiFileEntity> fileEntityList = anZhuangTiaoShiFileService.findFileByNmaee(page,size,xdid,filetype,filename,uid);
            PageInfo<AnZhuangTiaoShiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
        }

       /* PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiFileEntity>fileEntities=anZhuangTiaoShiFileService.findFileByNmae(page,size,xdid,filetype,filename);
        PageInfo<AnZhuangTiaoShiFileEntity>fileEntityPageInfo=new PageInfo<>(fileEntities);
        return ResponseDataUtil.ok("查询成功",fileEntityPageInfo);*/
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

    //添加线段的文件
    @PostMapping("/addFile")
    public Map<String,Object>addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity,Integer[] uids){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiFileService.addFile(anZhuangTiaoShiFileEntity,username,senderid,uids);
        return ResponseDataUtil.ok("新增文件成功");
    }
    //根据文件id  编辑对应的文件
    @PutMapping("/editFileById/{id}")
    public Map<String,Object>editFileById(@PathVariable Integer id,Integer[] uids,AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity){
        String username = this.getLoginUser().getTrueName();
        Integer userid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiFileService.editFileById(anZhuangTiaoShiFileEntity,id,uids,userid,username);
        return ResponseDataUtil.ok("编辑文件成功");
    }


    //根据文件id 编辑审核过程
    @PutMapping("/editAuditorByWJId/{id}")
    public Map<String, Object> editAuditorByWJId(@PathVariable Integer id, AnZhuangTiaoShiObjectAuditorEntity anZhuangTiaoShiObjectAuditorEntity, Integer senderId) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiFileService.editAuditorByWJId(anZhuangTiaoShiObjectAuditorEntity, id, username, receiverid,senderId);
        return ResponseDataUtil.ok("文件审核成功");
    }

    //根据文件id  批量删除 或者单个删除文件
    @DeleteMapping("/deletFileByIds/{ids}")
    public Map<String,Object>deletFileByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiFileService.deletFileByIds(ids);
        return ResponseDataUtil.ok("删除文件成功");
    }


    //根据id 下载文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity=anZhuangTiaoShiFileService.findById(id);
        if (anZhuangTiaoShiFileEntity != null) {
            String filePath = anZhuangTiaoShiFileEntity.getFilePath();
            String fileName = anZhuangTiaoShiFileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }
    //根据文件id  查询备注信息
    @GetMapping("/findReordById/{id}")
    public Map<String, Object> findReordById(@PathVariable Integer id) {
        List<AnZhuangTiaoShiRecordMessageEntity> recordMessageEntityList = anZhuangTiaoShiFileService.findReordById(id);
        return ResponseDataUtil.ok("查询文件记录成功", recordMessageEntityList);
    }


    //根据文件id 查询对应的文件数据
    @GetMapping("/findFileById/{id}")
    public Map<String,Object>findFileById(@PathVariable Integer id){
        List<AnZhuangTiaoShiFileEntity> fileEntityList=anZhuangTiaoShiFileService.findFileById(id);
        return ResponseDataUtil.ok("查询文件成功",fileEntityList);
    }


}
