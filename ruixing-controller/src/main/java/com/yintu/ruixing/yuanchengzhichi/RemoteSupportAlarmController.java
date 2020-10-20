package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
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
public class RemoteSupportAlarmController extends SessionController {
    @Autowired
    private RemoteSupportAlarmService remoteSupportAlarmService;
    @Autowired
    private RemoteSupportTicketService remoteSupportTicketService;
    @Autowired
    private CheZhanService cheZhanService;


    @DeleteMapping
    public Map<String, Object> remove(@RequestParam Integer[] czIds, @RequestParam Integer[] createTimes, @RequestParam Integer[] ids) {
        remoteSupportAlarmService.remove(czIds, createTimes, ids);
        return ResponseDataUtil.ok("删除报警/预警信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable String id) {
        String[] idArr = id.split("_");
        RemoteSupportAlarmEntity remoteSupportAlarmEntity = null;
        if (idArr.length == 3) {
            remoteSupportAlarmEntity = remoteSupportAlarmService.findById("alarm_" + idArr[0] + "_" + idArr[1], Integer.parseInt(idArr[2]));
        }
        return ResponseDataUtil.ok("查询报警/预警信息成功", remoteSupportAlarmEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "cz_id", required = false) Integer czId,
                                       @RequestParam(value = "start_time", required = false) Date startTime,
                                       @RequestParam(value = "end_time", required = false) Date endTime) {
        List<RemoteSupportAlarmEntity> remoteSupportAlarmEntities = remoteSupportAlarmService.findByCondition(pageNumber, pageSize, czId, startTime, endTime);
        PageInfo<RemoteSupportAlarmEntity> pageInfo = new PageInfo<>(remoteSupportAlarmEntities);
        return ResponseDataUtil.ok("查询报警/预警信息列表成功", pageInfo);
    }

    @GetMapping("/stations")
    public Map<String, Object> findStations() {
        List<CheZhanEntity> cheZhanEntities = cheZhanService.findAll();
        return ResponseDataUtil.ok("查询车站列表成功", cheZhanEntities);
    }

    @PostMapping("/remote/support/alarm/tickets")
    public Map<String, Object> addTickets(@RequestParam Integer aCzId,
                                          @RequestParam Integer aCreateTime,
                                          @RequestParam Integer aId, @Validated RemoteSupportTicketEntity remoteSupportTicketEntity) {
        remoteSupportTicketEntity.setCreateBy(this.getLoginUserName());
        remoteSupportTicketEntity.setCreateTime(new Date());
        remoteSupportTicketEntity.setModifiedBy(this.getLoginUserName());
        remoteSupportTicketEntity.setModifiedTime(new Date());
        remoteSupportTicketEntity.setAlarmId(StringUtil.getAssemblyId(aCzId, aCreateTime, aId));
        remoteSupportTicketService.add(remoteSupportTicketEntity);
        return ResponseDataUtil.ok("添加报警/预警处置单信息列表成功");
    }

    @GetMapping("/remote/support/alarm/tickets")
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


}
