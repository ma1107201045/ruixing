package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksFileEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiService;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntityExample;
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
 * @Date 2020/8/9 10:29
 * @Version 1.0
 * 需求:
 */
@RestController
@RequestMapping("/wenTiAll")
public class AnZhuangTiaoShiWenTiController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiWenTiService anZhuangTiaoShiWenTiService;


    //查询所有的单位名称
    @GetMapping("findAllDepartment")
    public Map<String, Object> findAllDepartment() {
        List<DepartmentEntity> departmentEntities = anZhuangTiaoShiWenTiService.findAllDepartment(new DepartmentEntityExample());
        return ResponseDataUtil.ok("查询部门成功", departmentEntities);
    }

    //新增问题
    @PostMapping("/addWenTi")
    public Map<String, Object> addWenTi(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity, Integer[] uids) {
        Integer senderid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiWenTiService.addWenTi(anZhuangTiaoShiWenTiEntity, uids, username, senderid);
        return ResponseDataUtil.ok("新增问题成功");
    }

    //根据id 编辑问题
    @PutMapping("/editWenTiById/{id}")
    public Map<String, Object> editWenTiById(@PathVariable Integer id, AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity) {
        Integer senderid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiWenTiService.editWenTiById(anZhuangTiaoShiWenTiEntity, id, senderid, username);
        return ResponseDataUtil.ok("编辑问题成功");
    }

    //根据问题id  查看时间轴
    @GetMapping("/findRecordMessageById/{id}")
    public Map<String, Object> findRecordMessageById(@PathVariable Integer id) {
        List<AnZhuangTiaoShiRecordMessageEntity> recordMessageEntityList = anZhuangTiaoShiWenTiService.findRecordMessageById(id);
        return ResponseDataUtil.ok("查询时间轴数据成功", recordMessageEntityList);
    }

    //根据问题id 编辑审核过程
    @PutMapping("/editAuditorByWTId/{id}")
    public Map<String, Object> editAuditorByWTId(@PathVariable Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, Integer senderId) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiWenTiService.editAuditorByWTId(id, anZhuangTiaoShiWenTiAuditorEntity, username, receiverid, senderId);
        return ResponseDataUtil.ok("问题审核成功");
    }

    //根据问题id  查询对应的数据(审核)
    @GetMapping("/findWenTiById/{id}")
    public Map<String, Object> findWenTiById(@PathVariable Integer id) {
        Integer receiverid = this.getLoginUser().getId().intValue();
        AnZhuangTiaoShiWenTiEntity wenTiEntity = anZhuangTiaoShiWenTiService.findWenTiById(id,receiverid);
        return ResponseDataUtil.ok("查询问题数据成功", wenTiEntity);
    }

    //根据问题id  查询对应的数据(首页跳转)
    @GetMapping("/findWenTiXiangQingById/{id}")
    public Map<String, Object> findWenTiXiangQingById(@PathVariable Integer id) {
        AnZhuangTiaoShiWenTiEntity wenTiEntity = anZhuangTiaoShiWenTiService.findWenTiXiangQingById(id);
        return ResponseDataUtil.ok("查询问题详情数据成功", wenTiEntity);
    }


    //初始化页面   或者根据线段名 或时间段，类别，反馈方式，受理单位，问题是否关闭 查询数据
    @GetMapping("/findSomeWenTi")
    public Map<String, Object> findSomeWenTi(Integer page, Integer size, String xdname,
                                             String startTime, String endTime,String wenTiType,
                                             String fankuiMode, String shouliDanwei,Integer isNotOver) {
        Integer receiverid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiWenTiEntity> wenTiEntityList = anZhuangTiaoShiWenTiService.findSomeWenTi(page, size, xdname,receiverid,
                                                                        startTime,endTime,wenTiType,fankuiMode,shouliDanwei,isNotOver);
        PageInfo<AnZhuangTiaoShiWenTiEntity> wenTiEntityPageInfo = new PageInfo<>(wenTiEntityList);
        return ResponseDataUtil.ok("查询成功", wenTiEntityPageInfo);
    }

    //主页面显示  显示为未完成的问题
    @GetMapping("/findAllNotDoWellWenTi")
    public Map<String, Object> findAllNotDoWellWenTi(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiWenTiEntity> wenTiEntityList = anZhuangTiaoShiWenTiService.findAllNotDoWellWenTi(page, size);
        PageInfo<AnZhuangTiaoShiWenTiEntity> wenTiEntityPageInfo = new PageInfo<>(wenTiEntityList);
        return ResponseDataUtil.ok("查询成功", wenTiEntityPageInfo);
    }

    //批量删除  或者单个删除
    @DeleteMapping("/deleteWenTiByIds/{ids}")
    public Map<String, Object> deleteWenTiByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            List<AnZhuangTiaoShiWorksFileEntity> fileEntityList = anZhuangTiaoShiWenTiService.findFileById(ids[i]);
            if (fileEntityList.size() == 0) {
                anZhuangTiaoShiWenTiService.deleteWenTiByIds(ids);
            } else {
                return ResponseDataUtil.error("存在有文件,不能删除此项问题记录");
            }
        }
        return ResponseDataUtil.ok("删除成功");
    }




    ////////////////////////文件/////////////////////////////
    //根据文件id 编辑审核过程
    @PutMapping("/editAuditorByWTFileId/{id}")
    public Map<String, Object> editAuditorByWTFileId(@PathVariable Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, Integer senderId) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiWenTiService.editAuditorByWJId(id, anZhuangTiaoShiWenTiAuditorEntity, username, receiverid, senderId);
        return ResponseDataUtil.ok("文件审核成功");
    }

    //根据文件id  查看时间轴
    @GetMapping("/findFileRecordMessageById/{id}")
    public Map<String, Object> findFileRecordMessageById(@PathVariable Integer id) {
        List<AnZhuangTiaoShiRecordMessageEntity> recordMessageEntityList = anZhuangTiaoShiWenTiService.findFileRecordMessageById(id);
        return ResponseDataUtil.ok("查询时间轴数据成功", recordMessageEntityList);
    }



    //根据文件id  查看对应的文件数据
    @GetMapping("/findFileById/{id}")
    public Map<String, Object> findFileById(@PathVariable Integer id) {
        AnZhuangTiaoShiWenTiFileEntity wenTiFileEntity = anZhuangTiaoShiWenTiService.findWenTiFileById(id);
        return ResponseDataUtil.ok("查询文件数据成功", wenTiFileEntity);
    }


    //查看反馈文件 或者根据文件名查询
    @GetMapping("/findAllFanKuiFileById")
    public Map<String, Object> findAllFanKuiFileById(Integer wid, Integer page, Integer size, String fileName) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiWenTiFileEntity> fileEntityList = anZhuangTiaoShiWenTiService.findAllFanKuiFileById(wid, page, size, fileName);
        PageInfo<AnZhuangTiaoShiWenTiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询反馈文件成功", fileEntityPageInfo);
    }

    //查看输出文件  或者根据文件名查询
    @GetMapping("/findAllShuChuFileById")
    public Map<String, Object> findAllShuChuFileById(Integer wid, Integer page, Integer size, String fileName) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiWenTiFileEntity> fileEntityList = anZhuangTiaoShiWenTiService.findAllShuChuFileById(wid, page, size, fileName);
        PageInfo<AnZhuangTiaoShiWenTiFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
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

    //新增反馈文件
    @PostMapping("/addFanKuiFile")
    public Map<String, Object> addFanKuiFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiWenTiService.addFanKuiFile(anZhuangTiaoShiWenTiFileEntity, uids, username, receiverid);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //新增输出文件
    @PostMapping("/addShuRuFile")
    public Map<String, Object> addShuRuFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        anZhuangTiaoShiWenTiService.addShuRuFile(anZhuangTiaoShiWenTiFileEntity, uids, username, receiverid);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //根据id  下载对应的文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity = anZhuangTiaoShiWenTiService.findById(id);
        if (anZhuangTiaoShiWenTiFileEntity != null) {
            String filePath = anZhuangTiaoShiWenTiFileEntity.getFilePath();
            String fileName = anZhuangTiaoShiWenTiFileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    //根据id  单个删除或者批量删除
    @DeleteMapping("deleteFileByIds/{ids}")
    public Map<String, Object> deleteFileByIds(@PathVariable Integer[] ids) {
        anZhuangTiaoShiWenTiService.deleteFileByIds(ids);
        return ResponseDataUtil.ok("删除文件成功");
    }

/////////////////////////////手机推送//////////////////////////////////////////////////
    //根据id查询对应的问题(处置单查看)
    @GetMapping("/findOneWenTiById/{id}")
    public Map<String,Object>findOneWenTiById(@PathVariable Integer id){
        AnZhuangTiaoShiWenTiEntity wenTiEntity=anZhuangTiaoShiWenTiService.findOneWenTiById(id);
        return ResponseDataUtil.ok("查询成功",wenTiEntity);
    }

    //手机推送消息
    @PostMapping("/pushMessage")
    public Map<String,Object>pushMessage(AnZhuangTiaoShiWenTiPushRecordEntity anZhuangTiaoShiWenTiPushRecordEntity){
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiWenTiPushRecordEntity.setCreatename(username);
        anZhuangTiaoShiWenTiPushRecordEntity.setCreatetime(new Date());
        anZhuangTiaoShiWenTiPushRecordEntity.setUpdatename(username);
        anZhuangTiaoShiWenTiPushRecordEntity.setUpdatetime(new Date());
        anZhuangTiaoShiWenTiService.pushMessage(anZhuangTiaoShiWenTiPushRecordEntity);
        return ResponseDataUtil.ok("发送消息成功");
    }

    //根据问题id  查看推送记录
    @GetMapping("/findPushMessageRecordById/{wid}")
    public Map<String,Object>findPushMessageRecordById(@PathVariable Integer wid,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWenTiPushRecordEntity>pushRecordEntityList=anZhuangTiaoShiWenTiService.findPushMessageRecordById(wid);
        PageInfo<AnZhuangTiaoShiWenTiPushRecordEntity>pushRecordEntityPageInfo=new PageInfo<>(pushRecordEntityList);
        return ResponseDataUtil.ok("查询推送记录成功",pushRecordEntityPageInfo);
    }


}
