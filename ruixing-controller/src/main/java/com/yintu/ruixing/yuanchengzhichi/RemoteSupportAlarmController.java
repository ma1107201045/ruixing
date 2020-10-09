package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/8 18:31
 */
@RestController
@RequestMapping("/remote/support/alarms")
public class RemoteSupportAlarmController {
    @Autowired
    private RemoteSupportAlarmService remoteSupportAlarmService;
    @Autowired
    private RemoteSupportTicketService remoteSupportTicketService;


    @DeleteMapping
    public Map<String, Object> remove(@RequestParam Integer[] czIds, @RequestParam Integer[] createTimes, @RequestParam Integer[] ids) {
        remoteSupportAlarmService.remove(czIds, createTimes, ids);
        return ResponseDataUtil.ok("删除报警/预警信息成功");
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam("cz_id") Integer czId,
                                       @RequestParam("start_time") Date startTime,
                                       @RequestParam("end_time") Date endTime) {
        List<RemoteSupportAlarmEntity> remoteSupportAlarmEntities = remoteSupportAlarmService.findByCondition(pageNumber, pageSize, czId, startTime, endTime);
        PageInfo<RemoteSupportAlarmEntity> pageInfo = new PageInfo<>(remoteSupportAlarmEntities);
        return ResponseDataUtil.ok("查询报警/预警信息列表成功", pageInfo);
    }



    @GetMapping("/tickets")
    public Map<String, Object> findTickets(@RequestParam("page_number") Integer pageNumber,
                                           @RequestParam("page_size") Integer pageSize,
                                           @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                           @RequestParam(value = "status", required = false) Short status,
                                           @RequestParam("cz_id") Integer czId,
                                           @RequestParam("create_time") Integer createTime,
                                           @RequestParam("id") Integer id) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<RemoteSupportTicketEntity> remoteSupportAlarmEntities = remoteSupportTicketService.findByCondition(null, status, StringUtil.getAssemblyId(czId, createTime, id));
        PageInfo<RemoteSupportTicketEntity> pageInfo = new PageInfo<>(remoteSupportAlarmEntities);
        return ResponseDataUtil.ok("查询报警/预警处置单信息列表成功", pageInfo);
    }

    @PostMapping("/add/tickets")
    public Map<String, Object> addTickets(@Validated RemoteSupportTicketEntity remoteSupportTicketEntity,
                                          @RequestParam Integer czId,
                                          @RequestParam Integer createTime,
                                          @RequestParam Integer id) {
        remoteSupportTicketEntity.setAlarmId(StringUtil.getAssemblyId(czId, createTime, id));
        remoteSupportTicketService.add(remoteSupportTicketEntity);
        return ResponseDataUtil.ok("查询报警/预警处置单信息列表成功");
    }
}
