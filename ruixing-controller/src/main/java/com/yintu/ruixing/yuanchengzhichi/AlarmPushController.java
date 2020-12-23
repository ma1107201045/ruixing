package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/23 11:51
 * @Version: 1.0
 */
@RestController
@RequestMapping("/alarm/push")
public class AlarmPushController {

    @Autowired
    private AlarmPushService alarmPushService;
    @Autowired
    private CheZhanService cheZhanService;


    @DeleteMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        alarmPushService.remove(id);
        return ResponseDataUtil.ok("删除报警、预警推送信息成功");
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "ap.id DESC") String orderBy,
                                       @RequestParam(value = "cid", required = false) Integer cid,
                                       @RequestParam(value = "push_date", required = false) Date pushDate) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<AlarmPushEntity> alarmPushEntities = alarmPushService.findByExample(cid, pushDate);
        PageInfo<AlarmPushEntity> pageInfo = new PageInfo<>(alarmPushEntities);
        return ResponseDataUtil.ok("查询报警、预警推送信息列表成功", pageInfo);
    }

    @GetMapping("/chezhans")
    public Map<String, Object> findChezhan() {
        List<CheZhanEntity> cheZhanEntities = cheZhanService.findAll();
        return ResponseDataUtil.ok("查询车站列表信息成功", cheZhanEntities);
    }


}
