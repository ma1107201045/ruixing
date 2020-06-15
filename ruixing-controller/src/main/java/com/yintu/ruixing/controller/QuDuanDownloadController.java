package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.QuDuanBaseEntity;
import com.yintu.ruixing.entity.QuDuanDownloadEntity;
import com.yintu.ruixing.entity.QuDuanInfoEntity;
import com.yintu.ruixing.service.QuDuanDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/8 15:36
 */
@RestController
@RequestMapping("/downloads")
public class QuDuanDownloadController extends BaseController {
    @Autowired
    private QuDuanDownloadService quDuanDownloadService;

    @PostMapping
    public Map<String, Object> add(@RequestParam("xid") Integer xid,
                                   @RequestParam("cid") Integer cid,
                                   @RequestParam("type") Short type,
                                   @RequestParam("startDateTime") Date startDateTime,
                                   @RequestParam("minute") Integer minute) {
        quDuanDownloadService.add(xid, cid, type, startDateTime, minute);
        return ResponseDataUtil.ok("添加下载记录成功");
    }


    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        quDuanDownloadService.remove(id);
        return ResponseDataUtil.ok("删除下载记录成功");

    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanDownloadService.findDataById(id);
        return ResponseDataUtil.ok("查询下载记录成功", quDuanBaseEntities);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam(value = "page_number") Integer pageNumber,
                                       @RequestParam(value = "page_size") Integer pageSize,
                                       @RequestParam(value = "sortby", required = false) String sortby,
                                       @RequestParam(value = "order", required = false) String order,
                                       @RequestParam(value = "startDateTime", required = false) Date startDateTime,
                                       @RequestParam(value = "endDateTime", required = false) Date endDateTime) {

        if (startDateTime != null && endDateTime != null && startDateTime.after(endDateTime) && !startDateTime.equals(endDateTime)) {
            throw new BaseRuntimeException("开始时间不能大于结束时间");
        }
        String orderBy = "id DESC";
        if (sortby != null && !"".equals(sortby) && order != null && !"".equals(order))
            orderBy = sortby + " " + order;
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<QuDuanDownloadEntity> quDuanDownloadEntities = quDuanDownloadService.findByDateTime(startDateTime, endDateTime);
        PageInfo<QuDuanDownloadEntity> pageInfo = new PageInfo<>(quDuanDownloadEntities);
        return ResponseDataUtil.ok("查询下载记录列表成功", pageInfo);
    }


}
