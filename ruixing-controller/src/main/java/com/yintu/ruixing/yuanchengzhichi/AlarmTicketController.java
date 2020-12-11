package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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


    @GetMapping("/alarms")
    public Map<String, Object> findByDisposeStatus(@RequestParam("page_number") Integer pageNumber,
                                                   @RequestParam("page_size") Integer pageSize,
                                                   @RequestParam(value = "order_by", required = false, defaultValue = "a.id DESC") String orderBy) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<AlarmEntity> alarmEntities = alarmService.findByDisposeStatus();
        PageInfo<AlarmEntity> pageInfo = new PageInfo<>(alarmEntities);
        return ResponseDataUtil.ok("查询报警、预警处置意见信息列表成功", pageInfo);
    }
}
