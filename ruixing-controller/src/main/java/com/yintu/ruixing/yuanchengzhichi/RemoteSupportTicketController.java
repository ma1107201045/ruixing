package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/9 14:15
 */
@RestController
@RequestMapping("/remote/support/alarm/ticktes")
public class RemoteSupportTicketController extends SessionController {
    @Autowired
    private RemoteSupportTicketService remoteSupportTicketService;

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        remoteSupportTicketService.remove(ids);
        return ResponseDataUtil.ok("删除报警/预警处置单信息成功");
    }
}
