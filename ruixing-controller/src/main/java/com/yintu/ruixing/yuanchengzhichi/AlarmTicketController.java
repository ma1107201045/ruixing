package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/11 16:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/alarm/ticket")
public class AlarmTicketController extends SessionController {
    @Autowired
    private AlarmTicketService alarmTicketService;
    @Autowired
    private AlarmService alarmService;

    @PostMapping
    public Map<String, Object> add(@Validated AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, @RequestParam Integer[] alarmIds, @RequestParam Integer[] faultStatus, @RequestParam Integer[] disposeStatus) {
        alarmTicketEntityWithBLOBs.setCreateBy(this.getLoginUserName());
        alarmTicketEntityWithBLOBs.setCreateTime(new Date());
        alarmTicketEntityWithBLOBs.setModifiedBy(this.getLoginUserName());
        alarmTicketEntityWithBLOBs.setModifiedTime(new Date());
        alarmTicketService.add(alarmTicketEntityWithBLOBs, alarmIds, faultStatus, disposeStatus);
        return ResponseDataUtil.ok("添加报警、预警处置单信息成功");
    }

    @PutMapping
    public Map<String, Object> edit(@Validated AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, @RequestParam Integer alarmId, @RequestParam Integer faultStatus, @RequestParam Integer disposeStatus) {
        alarmTicketEntityWithBLOBs.setModifiedBy(this.getLoginUserName());
        alarmTicketEntityWithBLOBs.setModifiedTime(new Date());
        alarmTicketService.edit(alarmTicketEntityWithBLOBs, alarmId, faultStatus, disposeStatus);
        return ResponseDataUtil.ok("修改报警、预警处置单信息成功");
    }


    @GetMapping("/{alarmIds}/alarm")
    public Map<String, Object> findAlarmByAlarmIds(@PathVariable Integer[] alarmIds) {
        List<AlarmEntity> alarmEntities = alarmService.findByIds(alarmIds);
        return ResponseDataUtil.ok("修改报警、预警处置单信息列表成功", alarmEntities);
    }
}
