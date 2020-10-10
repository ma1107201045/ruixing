package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private RemoteSupportTicketPushService remoteSupportTicketPushService;
    @Autowired
    private UserService userService;

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        remoteSupportTicketService.remove(ids);
        return ResponseDataUtil.ok("删除报警/预警处置单信息成功");
    }

    @PostMapping("/remote/support/alarm/ticket/pushs")
    public Map<String, Object> push(@Validated RemoteSupportTicketPushEntity remoteSupportTicketPushEntity, @RequestParam Integer[] userIds) {
        remoteSupportTicketPushEntity.setCreateBy(this.getLoginUserName());
        remoteSupportTicketPushEntity.setCreateTime(new Date());
        remoteSupportTicketPushEntity.setModifiedBy(this.getLoginUserName());
        remoteSupportTicketPushEntity.setModifiedTime(new Date());
        remoteSupportTicketPushEntity.setOperator(this.getLoginTrueName());
        remoteSupportTicketPushService.add(remoteSupportTicketPushEntity, userIds);
        return ResponseDataUtil.ok("推送报警/预警处置单成功");
    }

    @GetMapping("/persons")
    public Map<String, Object> findUserEntities(@RequestParam(value = "true_name", required = false, defaultValue = "") String trueName) {
        List<UserEntity> userEntities = userService.findByTruename(trueName);
        userEntities = userEntities
                .stream()
                .filter(userEntity -> !userEntity.getId().equals(this.getLoginUserId()))
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询推送人列表信息成功", userEntities);
    }
}
