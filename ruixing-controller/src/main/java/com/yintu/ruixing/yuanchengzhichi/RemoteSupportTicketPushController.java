package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/10 14:15
 */

@RestController
@RequestMapping("/remote/support/alarm/ticket/pushs")
public class RemoteSupportTicketPushController {
    @Autowired
    private RemoteSupportTicketPushService remoteSupportTicketPushService;

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        remoteSupportTicketPushService.remove(ids);
        return ResponseDataUtil.ok("删除报警/预警处置单推送信息成功");
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "rstp.id DESC") String orderBy,
                                       @RequestParam(value = "operator", required = false) String operator) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<RemoteSupportTicketPushEntity> remoteSupportTicketPushEntities = remoteSupportTicketPushService.findByCondition(null, "".equals(operator) ? null : operator);
        PageInfo<RemoteSupportTicketPushEntity> pageInfo = new PageInfo<>(remoteSupportTicketPushEntities);
        long count = remoteSupportTicketPushService.countByOperator("".equals(operator) ? null : operator);
        pageInfo.setTotal(count);
        return ResponseDataUtil.ok("查询报警/预警处置单推送信息列表成功", pageInfo);
    }

}
