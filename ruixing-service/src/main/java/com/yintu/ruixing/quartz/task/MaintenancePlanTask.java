package com.yintu.ruixing.quartz.task;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.yunxingweihu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional
public class MaintenancePlanTask {

    @Autowired
    private MaintenancePlanService maintenancePlanService;
    @Autowired
    private MaintenancePlanInfoService maintenancePlanInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ScheduleJobService scheduleJobService;

    public void execute(Integer maintenancePlanId) {
        MaintenancePlanEntity maintenancePlanEntity = maintenancePlanService.findById(maintenancePlanId);
        if (maintenancePlanEntity != null) {
            List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = maintenancePlanInfoService.findByCondition(null, null, null, new Date());
            if (maintenancePlanInfoEntities.isEmpty()) {
                List<UserEntity> userEntities = userService.findByTruename(null);
                for (UserEntity userEntity : userEntities) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy("系统");
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy("系统");
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle(TaskEnum.MAINTENANCEPLAN.getName());
                    messageEntity.setContext("“" + maintenancePlanEntity.getName() + "”维护计划项目中，没有进行维护工作！");
                    messageEntity.setType((short) 5);
                    messageEntity.setSmallType((short) 1);
                    messageEntity.setMessageType((short) 1);
                    messageEntity.setProjectId(null);
                    messageEntity.setFileId(null);
                    messageEntity.setSenderId(null);
                    messageEntity.setReceiverId(userEntity.getId().intValue());
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
        }
    }
}
