package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 投招标文件技术支持
 *
 * @author:mlf
 * @date:2020/7/2 14:56
 */
@Controller
@RequestMapping("/biddings/files")
public class BiddingFileController extends SessionController {

    @Autowired
    private BiddingFileService biddingFileService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private UserService userService;


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated BiddingFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        entity.setUserId(this.getLoginUserId().intValue());
        biddingFileService.add(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加招投标技术支持文件信息成功");
    }


    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        biddingFileService.remove(ids);
        return ResponseDataUtil.ok("删除招投标技术支持文件信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated BiddingFileEntity entity, @RequestParam(value = "auditorIds", required = false) Integer[] auditorIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        biddingFileService.edit(entity, auditorIds, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改招投标技术支持文件信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        BiddingFileEntity biddingFileEntity = biddingFileService.findBiddingById(id);
        return ResponseDataUtil.ok("查询招投标技术支持文件信息成功", biddingFileEntity);
    }


    @GetMapping("/search")
    @ResponseBody
    public Map<String, Object> findBySearch(@RequestParam("page_number") Integer pageNumber,
                                            @RequestParam("page_size") Integer pageSize,
                                            @RequestParam(value = "order_by", required = false, defaultValue = "bf.id DESC") String orderBy,
                                            @RequestParam(value = "project_id", required = false) Integer biddingId,
                                            @RequestParam(value = "file_name", required = false) String name,
                                            @RequestParam(value = "type", required = false) String type) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<BiddingFileEntity> biddingFileEntities = biddingFileService.findByBiddingIdAndNameAndType(biddingId, name, type, this.getLoginUserId().intValue());
        PageInfo<BiddingFileEntity> pageInfo = new PageInfo<>(biddingFileEntities);
        return ResponseDataUtil.ok("查询招投标技术支持以及文件信息列表成功", pageInfo);
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "投招标技术支持列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        biddingFileService.exportFile(response.getOutputStream(), ids, this.getLoginUserId().intValue());
    }

    @GetMapping("/{id}/log")
    @ResponseBody
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 2, (short) 2, id, null));
        return ResponseDataUtil.ok("查询招投标技术支持文件日志信息列表成功", solutionLogEntities);
    }

    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String trueName) {
        List<UserEntity> userEntities = userService.findByTruename(trueName);
        userEntities = userEntities
                .stream()
                .filter(userEntity -> !userEntity.getId().equals(this.getLoginUserId()))
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询审核人列表信息成功", userEntities);
    }


    /**
     * 审核文件
     *
     * @param id     文件id
     * @param isPass 是否 审核状态 1.待审核 2.已审核未通过 3.已审核已通过
     * @param reason 已审核未通过
     * @return 已审核未通过理由
     */
    @PutMapping("/audit/{id}")
    @ResponseBody
    public Map<String, Object> audit(@PathVariable Integer id, @RequestParam("isPass") Short isPass, String reason) {
        biddingFileService.audit(id, isPass, reason, this.getLoginUserId().intValue(), this.getLoginUserName(), this.getLoginTrueName());
        return ResponseDataUtil.ok("审核招投标技术支持文件信息成功");
    }


}
