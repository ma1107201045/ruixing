package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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


    @DeleteMapping
    public Map<String, Object> remove(@RequestParam Integer czId, @RequestParam(name = "createTime") Integer createtime, @RequestParam Integer id) {
        remoteSupportAlarmService.remove(StringUtil.getBaoJingYuJingTableName(czId, new Date(createtime * 1000L)), id);
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
}
