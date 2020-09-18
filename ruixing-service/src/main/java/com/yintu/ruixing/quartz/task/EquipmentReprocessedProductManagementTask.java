package com.yintu.ruixing.quartz.task;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.yunxingweihu.TaskEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/18 20:51
 */
@Component
@Transactional
public class EquipmentReprocessedProductManagementTask {
    @Autowired
    EquipmentReprocessedProductManagementService equipmentReprocessedProductManagementService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    public void execute(Integer equipmentReprocessedProductManagementId) {
        EquipmentReprocessedProductManagementEntity equipmentReprocessedProductManagementEntity = equipmentReprocessedProductManagementService.findById(equipmentReprocessedProductManagementId);
        if (equipmentReprocessedProductManagementEntity != null) {
            if (equipmentReprocessedProductManagementEntity.getRecordTime() == null) {
                List<UserEntity> userEntities = userService.findByTruename(null);
                for (UserEntity userEntity : userEntities) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy("系统");
                    messageEntity.setCreateTime(new Date());
                    messageEntity.setModifiedBy("系统");
                    messageEntity.setModifiedTime(new Date());
                    messageEntity.setTitle(TaskEnum.MAINTENANCEPLAN.getName());
                    messageEntity.setContext("“" + "”");
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
