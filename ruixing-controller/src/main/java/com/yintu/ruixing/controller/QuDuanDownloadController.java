package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.QuDuanBaseEntity;
import com.yintu.ruixing.entity.QuDuanDownloadEntity;
import com.yintu.ruixing.service.CheZhanService;
import com.yintu.ruixing.service.QuDuanDownloadService;
import com.yintu.ruixing.service.QuDuanInfoService;
import com.yintu.ruixing.websocket.SessionInfo;
import com.yintu.ruixing.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/8 15:36
 */
@RestController
@RequestMapping("/quduan/info/downloads")
public class QuDuanDownloadController extends SessionController {
    @Autowired
    private QuDuanDownloadService quDuanDownloadService;

    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping
    public Map<String, Object> add(
            @RequestParam("czId") Integer czId,
            @RequestParam("type") Short type,
            @RequestParam("startDateTime") Date startDateTime,
            @RequestParam("minute") Integer minute) {
        Calendar time = Calendar.getInstance();
        time.setTime(startDateTime);
        time.add(Calendar.MINUTE, minute);
        Date endDateTime = time.getTime();
        Integer id = quDuanDownloadService.add(czId, type, startDateTime, endDateTime);
        webSocketServer.sendMessage(czId, id);
        return ResponseDataUtil.ok("添加下载记录成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        quDuanDownloadService.remove(id);
        return ResponseDataUtil.ok("删除下载记录成功");

    }

    /**
     * 回放接口
     *
     * @param id
     * @return
     */
    @GetMapping("/backdata")
    public Map<String, Object> findPlayBackDataById(@RequestParam("id") Integer id) {
        Map<String, Object> map = quDuanDownloadService.findPlayBackDataById(id);
        return ResponseDataUtil.ok("查询下载记录成功", map);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam(value = "page_number") Integer pageNumber,
                                       @RequestParam(value = "page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false,defaultValue = "qd.id DESC") String orderBy,
                                       @RequestParam(value = "startDateTime", required = false) Date startDateTime,
                                       @RequestParam(value = "endDateTime", required = false) Date endDateTime) {

        if (startDateTime != null && endDateTime != null && startDateTime.after(endDateTime) && !startDateTime.equals(endDateTime)) {
            throw new BaseRuntimeException("开始时间不能大于结束时间");
        }
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<QuDuanDownloadEntity> quDuanDownloadEntities = quDuanDownloadService.findByDateTime(startDateTime, endDateTime);
        PageInfo<QuDuanDownloadEntity> pageInfo = new PageInfo<>(quDuanDownloadEntities);
        return ResponseDataUtil.ok("查询下载记录列表成功", pageInfo);
    }


}
