package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/10 18:25
 * @Version: 1.0
 */
@RestController
@RequestMapping("/alarm/ticket")
public class AlarmTicketController extends SessionController {
    @Autowired
    private AlarmTicketService alarmTicketService;

    @PostMapping
    public Map<String, Object> add(@Validated AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs) {
        alarmTicketService.add(alarmTicketEntityWithBLOBs);
        return ResponseDataUtil.ok("添加报警、预警处置意见信息");
    }

}
