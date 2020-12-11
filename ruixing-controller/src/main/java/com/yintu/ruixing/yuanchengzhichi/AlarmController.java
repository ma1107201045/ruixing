package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/10 10:02
 * @Version: 1.0
 */
@RestController
@RequestMapping("/alarm")
public class AlarmController extends SessionController {
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private DataStatsService dataStatsService;

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @RequestParam Integer idea, String remark) {
        alarmService.edit(id, idea, remark);
        return ResponseDataUtil.ok("修改报警、预警处置意见信息");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        AlarmEntity alarmEntity = new AlarmEntity();
        alarmEntity.setId(id);
        List<AlarmEntity> alarmEntities = alarmService.findByExample(null, null, null, null, null, alarmEntity, null);
        PageInfo<AlarmEntity> pageInfo = new PageInfo<>(alarmEntities);
        return ResponseDataUtil.ok("查询报警、预警处置意见信息", pageInfo);
    }

    @GetMapping
    public Map<String, Object> finAll(@RequestParam("page_number") Integer pageNumber,
                                      @RequestParam("page_size") Integer pageSize,
                                      @RequestParam(value = "order_by", required = false, defaultValue = "a.id DESC") String orderBy,
                                      @RequestParam(value = "tid", required = false) Integer tid,
                                      @RequestParam(value = "did", required = false) Integer did,
                                      @RequestParam(value = "xid", required = false) Integer xid,
                                      @RequestParam(value = "begin_time", required = false) Date beginTime,
                                      @RequestParam(value = "end_time", required = false) Date endTime,
                                      AlarmEntity alarmEntityQuery,
                                      @RequestParam(value = "xt_type", required = false) Integer xtType) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<AlarmEntity> alarmEntities = alarmService.findByExample(tid, did, xid, beginTime, endTime, alarmEntityQuery, xtType);
        PageInfo<AlarmEntity> pageInfo = new PageInfo<>(alarmEntities);
        return ResponseDataUtil.ok("查询报警、预警处置意见信息", pageInfo);
    }

    @GetMapping("/railways/bureau")
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/signal/depot")
    public Map<String, Object> findDianWuDuanByTid(@RequestParam Integer tid) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(tid);
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/special/railway/line")
    public Map<String, Object> findXianDuanByDid(@RequestParam Integer did) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(did);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/station")
    public Map<String, Object> findCheZhanByXid(@RequestParam Integer xid) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(xid);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }

    //根据车站的id  查找区段的名字和id
    @GetMapping("/section")
    public Map<String, Object> findQuDuanByCid(@RequestParam Integer stationId) {
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findQuDuanByCid(stationId);
        return ResponseDataUtil.ok("查询区段成功", quDuanBaseEntities);
    }
}
