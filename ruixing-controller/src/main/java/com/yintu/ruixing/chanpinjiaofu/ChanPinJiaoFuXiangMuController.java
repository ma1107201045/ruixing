package com.yintu.ruixing.chanpinjiaofu;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/1 13:43
 * @Version 1.0
 * 需求:产品需求所有
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Controller
@RequestMapping("/ChanPinJiaoFuXiangMuAll")
public class ChanPinJiaoFuXiangMuController extends SessionController {
    @Autowired
    private ChanPinJiaoFuXiangMuService chanPinJiaoFuXiangMuService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    //创建三级树
    @GetMapping
    @ResponseBody
    public Map<String, Object> findSanJiShu() {
        List<TreeNodeUtil> treeNodeUtils = chanPinJiaoFuXiangMuService.findSanJiShu();
        return ResponseDataUtil.ok("查询成功", treeNodeUtils);
    }

    //查询消息提醒
    @ResponseBody
    @GetMapping("/findXiaoXi")
    public Map<String, Object> findXiaoXi() {
        Integer senderid = this.getLoginUser().getId().intValue();
        List<MessageEntity> contextlist = chanPinJiaoFuXiangMuService.findXiaoXi(senderid);
        return ResponseDataUtil.ok("查询消息成功", contextlist);
    }


    //根据消息id   更改信息状态
    @ResponseBody
    @PutMapping("/editXiaoXiById/{id}")
    public Map<String, Object> editXiaoXiById(@PathVariable Integer id, MessageEntity messageEntity) {
        chanPinJiaoFuXiangMuService.editXiaoXiById(messageEntity);
        return ResponseDataUtil.ok("更改信息状态成功");
    }

    //添加新消息提醒
    //添加定时任务
    @Scheduled(cron = "0 0 06 * * ?")
    @ResponseBody
    @GetMapping("/addXiaoXi")
    public void addXiaoXi() throws Exception {
        //获取当前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd ");
        String currentDate = dateFormat.format(new Date());
        Date date1 = dateFormat.parse(currentDate);
        List<Integer> uids = new ArrayList<>();
//        String username = this.getLoginUser().getTrueName();
//        Integer senderid = this.getLoginUser().getId().intValue();
        String username = "系统";
        Integer senderid = null;
        Date nowTime = new Date();
        String truename = null;
        List<UserEntity> userEntitiess = userService.findByTruename(truename);
        for (UserEntity entitiess : userEntitiess) {
            Integer id = entitiess.getId().intValue();
            uids.add(id);
        }
        //获取 项目的发货提醒日期
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = chanPinJiaoFuXiangMuService.findAllXiangMu();
        for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
            Integer id = chanPinJiaoFuXiangMuEntity.getId();
            String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
            if (fahuoTixingTime != null) {
                String currentDate1 = dateFormat.format(fahuoTixingTime);
                Date date2 = dateFormat.parse(currentDate1);
                int compareTo = date1.compareTo(date2);
                if (compareTo == 0) {
                    for (Integer uid : uids) {
                        //添加一条消息到消息表
                        MessageEntity messageEntity = new MessageEntity();
                        messageEntity.setContext("“" + xiangmuName + "”项目待发货，请及时联系顾客确认供货计划！");
                        messageEntity.setType((short) 2);
                        messageEntity.setStatus((short) 1);
                        messageEntity.setCreateBy(username);//创建人
                        messageEntity.setCreateTime(nowTime);//创建时间
                        messageEntity.setMessageType((short) 2);
                        messageEntity.setProjectId(id);
                        messageEntity.setSenderId(senderid);
                        messageEntity.setReceiverId(uid);
                        messageService.sendMessage(messageEntity);
                    }
                }
            }
        }
    }

    //根据id 进行单个或者批量下载到Excel
    @GetMapping("/downLoadByIds/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "交付情况问题反馈列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        chanPinJiaoFuXiangMuService.exportFile(response.getOutputStream(), ids);
    }

    //查询项目审核角色
    @ResponseBody
    @GetMapping("/XMAuditors")
    public Map<String, Object> XMAuditors() {
        List<AuditConfigurationEntity> auditConfigurationEntities = chanPinJiaoFuXiangMuService.findXMAudit((short) 5, (short) 1, (short) 1);
        if (auditConfigurationEntities.isEmpty()) {
            return ResponseDataUtil.ok("查询审核角色成功", chanPinJiaoFuXiangMuService.findTree());
        }
        return ResponseDataUtil.ok("查询审核角色成功", new ArrayList<>());
    }

    //查询文件审核角色
    @ResponseBody
    @GetMapping("/FileAuditors")
    public Map<String, Object> FileAuditors() {
        List<AuditConfigurationEntity> auditConfigurationEntities = chanPinJiaoFuXiangMuService.findFileAudit((short) 6, (short) 1, (short) 1);
        if (auditConfigurationEntities.isEmpty()) {
            return ResponseDataUtil.ok("查询审核角色成功", chanPinJiaoFuXiangMuService.findTree());
        }
        return ResponseDataUtil.ok("查询审核角色成功", new ArrayList<>());
    }


    //查询转交审核人姓名
    @GetMapping("/findZhuanJiaoAuditorName")
    @ResponseBody
    public Map<String, Object> findAllAuditorNamre(Integer objectid, Integer objectType) {
        List<UserEntity> userEntities = chanPinJiaoFuXiangMuService.findZhuanJiaoAuditorName(objectid, objectType);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }


    //根据项目id 查看更新历史记录
    @ResponseBody
    @GetMapping("/findReordById/{id}")
    public Map<String, Object> findReordById(@PathVariable Integer id) {
        List<ChanPinJiaoFuRecordMessageEntity> recordMessageEntityList = chanPinJiaoFuXiangMuService.findReordById(id);
        return ResponseDataUtil.ok("查询项目记录成功", recordMessageEntityList);
    }

    //根据文件id 查看更新历史记录
    @ResponseBody
    @GetMapping("/findFileReordById/{id}")
    public Map<String, Object> findFileReordById(@PathVariable Integer id) {
        List<ChanPinJiaoFuRecordMessageEntity> recordMessageEntityList = chanPinJiaoFuXiangMuService.findFileReordById(id);
        return ResponseDataUtil.ok("查询文件记录成功", recordMessageEntityList);
    }

    //新增项目
    @ResponseBody
    @PostMapping("/addXiangMu")
    public Map<String, Object> addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.addXiangMu(chanPinJiaoFuXiangMuEntity, username, senderid);
        return ResponseDataUtil.ok("添加项目成功");
    }

    //根据选择的id  修改对应的项目内容
    @ResponseBody
    @PutMapping("/editXiangMuById/{id}")
    public Map<String, Object> editXiangMuById(@PathVariable Integer id, ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, Integer[] auditorid, Integer[] sort) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.editXiangMuById(chanPinJiaoFuXiangMuEntity, username, id, senderid, auditorid, sort);
        return ResponseDataUtil.ok("修改项目数据成功");
    }

    //根据id  单个或者批量删除对应的项目
    @ResponseBody
    @DeleteMapping("/deletXiagMuByIds/{ids}")
    public Map<String, Object> deletXiagMuById(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findFile(ids[i]);
            if (fileEntityList.size() == 0) {
                chanPinJiaoFuXiangMuService.deletXiagMuById(ids[i]);
            } else {
                return ResponseDataUtil.error("项目存在文件，不能删除项目");
            }
        }
        return ResponseDataUtil.ok("删除数据成功");
    }

    //根据项目id 编辑审核过程
    @ResponseBody
    @PutMapping("/editAuditorByXMId/{id}")
    public Map<String, Object> editAuditorByXMId(@PathVariable Integer id, Short isPass, Integer passUserId,
                                                 String accessoryName, String accessoryPath, String context) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.editAuditorByXMId(id, isPass, passUserId, username, receiverid, accessoryName, accessoryPath, context);
        return ResponseDataUtil.ok("项目审核成功");
    }

    //根据文件id 编辑审核过程
    @ResponseBody
    @PutMapping("/editAuditorByWJId/{id}")
    public Map<String, Object> editAuditorByWJId(@PathVariable Integer id, Short isPass, Integer passUserId,
                                                 String accessoryName, String accessoryPath, String context) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.editAuditorByWJId(id, isPass, passUserId, username, receiverid, accessoryName, accessoryPath, context);
        return ResponseDataUtil.ok("文件审核成功");
    }


    //查询所有的项目数据
    @ResponseBody
    @GetMapping("/findAll")
    public Map<String, Object> findAll(Integer page, Integer size) {
        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findAll(page, size, uid);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        return ResponseDataUtil.ok("查询所有数据成功", pageInfo);
    }

    //根据项目编号 和项目名称  进行模糊查询
    @ResponseBody
    @GetMapping("/findXiangMuData")
    public Map<String, Object> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size) {
        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findXiangMuData(xiangMuBianHao, xiangMuName, page, size, uid);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        return ResponseDataUtil.ok("查询数据成功", pageInfo);
    }

    //根据树的id  查询不同项目状态的数据
    @ResponseBody
    @GetMapping("/findXiangMuByIds")
    public Map<String, Object> findXiangMuByIds(Integer stateid, Integer page, Integer size) {
        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findXiangMuByIds(stateid, page, size, uid);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        return ResponseDataUtil.ok("查询数据成功", pageInfo);
    }


    //根据项目的id  查询对应的数据
    @ResponseBody
    @GetMapping("/findXiangMuById/{id}")
    public Map<String, Object> findXiangMuById(@PathVariable Integer id) {
        Integer uid = this.getLoginUser().getId().intValue();
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findXiangMuById(id, uid);
        return ResponseDataUtil.ok("查询数据成功", chanPinJiaoFuXiangMuEntities);
    }


    ///////////////////////////文件////////////////////////////////////

    //新增文件列表
    @ResponseBody
    @PostMapping("/addXiangMuFile")
    public Map<String, Object> addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer[] auditorids, Integer[] sort) {
        String username = this.getLoginUser().getTrueName();
        Integer uid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.addXiangMuFile(chanPinJiaoFuXiangMuFileEntity, username, uid, auditorids, sort);
        return ResponseDataUtil.ok("新增文件列表成功");
    }

    //根据id  修改文件列表
    @ResponseBody
    @PutMapping("/editXiangMuFileById/{id}")
    public Map<String, Object> editXiangMuFileById(@PathVariable Integer id, ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer[] auditorids, Integer[] sort) {
        String username = this.getLoginUser().getTrueName();
        Integer uid = this.getLoginUser().getId().intValue();
        chanPinJiaoFuXiangMuService.editXiangMuFileById(chanPinJiaoFuXiangMuFileEntity, id, uid, username, auditorids, sort);
        return ResponseDataUtil.ok("修改数据成功");
    }

    //根据文件类型或者文件名 进行模糊查询
    @ResponseBody
    @GetMapping("/findFileBySomething")
    public Map<String, Object> findFileBySomething(Integer xmid, Integer page, Integer size, Integer filetype, String filename) {
        //List<ChanPinJiaoFuFileAuditorEntity> fuFileAuditorEntityList = chanPinJiaoFuXiangMuService.findXMByXmid(xmid);

        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findFileBySomething(xmid, page, size, filetype, filename, uid);
        PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileEntityPageInfo);

            /*Integer uid = this.getLoginUser().getId().intValue();
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findFileBySomethingg(xmid, page, size, filetype, filename, uid);
            System.out.println("asdadasdadasd+" + fileEntityList);
            PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询文件成功", fileEntityPageInfo);*/

    }




    //根据项目id 初始化输入文件
    @ResponseBody
    @GetMapping("/findShuRuFile")
    public Map<String, Object> findShuRuFile(Integer xmid, Integer page, Integer size) {
        // List<ChanPinJiaoFuFileAuditorEntity> fuFileAuditorEntityList = chanPinJiaoFuXiangMuService.findXMByXmid(xmid);

        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findShuRuFile(xmid, page, size, uid);
        PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询输入文件成功", fileEntityPageInfo);

           /* PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findShuRuFilee(xmid, page, size, uid);
            PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输入文件成功", fileEntityPageInfo);
       */
    }

    //根据项目id 初始化输出文件
    @ResponseBody
    @GetMapping("/findShuChuFile")
    public Map<String, Object> findShuChuFile(Integer xmid, Integer page, Integer size) {
        //List<ChanPinJiaoFuFileAuditorEntity> fuFileAuditorEntityList = chanPinJiaoFuXiangMuService.findXMByXmid(xmid);

        Integer uid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findShuChuFile(xmid, page, size, uid);
            PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);

           /* PageHelper.startPage(page, size);
            Integer uid = this.getLoginUser().getId().intValue();
            List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findShuChuFilee(xmid, page, size, uid);
            PageInfo<ChanPinJiaoFuXiangMuFileEntity> fileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询输出文件成功", fileEntityPageInfo);
        */
    }


    //根据文件id  查询对应的文件数据
    @ResponseBody
    @GetMapping("/findFileById/{id}")
    public Map<String, Object> findFileById(@PathVariable Integer id) {
        Integer uid = this.getLoginUser().getId().intValue();
        List<ChanPinJiaoFuXiangMuFileEntity> fileEntityList = chanPinJiaoFuXiangMuService.findFileById(id, uid);
        return ResponseDataUtil.ok("查询文件成功", fileEntityList);
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
        ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity = chanPinJiaoFuXiangMuService.findById(id);
        if (chanPinJiaoFuXiangMuFileEntity != null) {
            String filePath = chanPinJiaoFuXiangMuFileEntity.getFilePath();
            String fileName = chanPinJiaoFuXiangMuFileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    //根据id  删除对应的文件
    @ResponseBody
    @DeleteMapping("/deletXiangMuFileById/{id}")
    public Map<String, Object> deletXiangMuFileById(@PathVariable Integer id) {
        chanPinJiaoFuXiangMuService.deletXiangMuFileById(id);
        return ResponseDataUtil.ok("删除文件成功");
    }

    //根据id  单个或者批量删除文件
    @ResponseBody
    @DeleteMapping("/deletXiangMuFileByIds/{ids}")
    public Map<String, Object> deletXiangMuFileByIds(@PathVariable Integer[] ids) {
        chanPinJiaoFuXiangMuService.deletXiangMuFileByIds(ids);
        return ResponseDataUtil.ok("单个或者批量删除文件成功");
    }

    //查询所有审核人姓名
    @ResponseBody
    @GetMapping("/findAllAuditorName")
    public Map<String, Object> findAllAuditorNamre(String truename) {
        List<UserEntity> userEntities = new ArrayList<>();
        String username = this.getLoginUser().getTrueName();
        List<UserEntity> userEntitiess = userService.findByTruename(truename);
        for (UserEntity userEntity : userEntitiess) {
            if (!userEntity.getTrueName().equals(username)) {
                userEntities.add(userEntity);
            }
        }
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //根据文件id  查询对应的审核人名字
    @ResponseBody
    @GetMapping("/findAllAuditorNameById/{id}")
    public Map<String, Object> findAllAuditorNameById(@PathVariable Integer id) {
        List<UserEntity> userEntities = chanPinJiaoFuXiangMuService.findAllAuditorNameById(id);
        return ResponseDataUtil.ok("查询审核人名成功", userEntities);

    }
    //////////////////////////////交付情况统计/////////////////////////////////////

    //统计各个状态的项目数量
    @ResponseBody
    @GetMapping("/findJiaoFuQingKuangNumberAll")
    public Map<String, Object> findJiaoFuQingKuangNumberAll() {
        // Map<String,Object> map=new HashMap<>();
        Map<String, Object> map = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangNumberAll();
        return ResponseDataUtil.ok("查询统计数量成功", map);
    }

    //查询各个状态的列表
    @ResponseBody
    @GetMapping("/findJiaoFuQingKuangList")
    public Map<String, Object> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size) {
        if (choiceTing.equals("daiQianShu") || choiceTing.equals("daiYanGong")) {
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangList(choiceTing, page, size);
            PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
            return ResponseDataUtil.ok("查询数据成功", pageInfo);
        } else {
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangLists(choiceTing, page, size);
            PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
            return ResponseDataUtil.ok("查询数据成功", pageInfo);
        }
    }
}
