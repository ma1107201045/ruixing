package com.yintu.ruixing.yuanchengzhichi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/7 15:29
 */
@RestController
@RequestMapping("/remote/support/video/meetings")
public class RemoteSupportVideoMeetingController extends SessionController implements BaseController<RemoteSupportVideoMeetingEntityWithBLOBs, Integer> {

    @Autowired
    private RemoteSupportVideoMeetingService remoteSupportVideoMeetingService;
    @Autowired
    private DataStatsService dataStatsService;

    @PostMapping
    public Map<String, Object> add(@Validated RemoteSupportVideoMeetingEntityWithBLOBs entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        remoteSupportVideoMeetingService.add(entity);
        return ResponseDataUtil.ok("添加视频会议记录信息成功", entity.getId());
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }


    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        remoteSupportVideoMeetingService.remove(ids);
        return ResponseDataUtil.ok("删除视频会议记录信息成功");
    }

    @Override
    public Map<String, Object> edit(Integer id, RemoteSupportVideoMeetingEntityWithBLOBs entity) {
        return null;
    }

    @PutMapping("/add/join/person/{id}")
    public Map<String, Object> addJoinPerson(@PathVariable Integer id, @RequestParam String joinPerson) {
        remoteSupportVideoMeetingService.addJoinPerson(id, joinPerson);
        return ResponseDataUtil.ok("添加视频会议参与人信息成功");
    }

    @PutMapping("/add/duration/{id}")
    public Map<String, Object> addDuration(@PathVariable Integer id, @RequestParam Integer duration) {
        remoteSupportVideoMeetingService.addDuration(id, duration, this.getLoginUserName());
        return ResponseDataUtil.ok("添加视频会议时长信息成功");
    }

    @PutMapping("/add/opinion/{id}")
    public Map<String, Object> addOpinion(@PathVariable Integer id, @RequestParam String opinion) {
        remoteSupportVideoMeetingService.addOpinion(id, opinion, this.getLoginUserName());
        return ResponseDataUtil.ok("添加视频会议处置意见信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        RemoteSupportVideoMeetingEntityWithBLOBs remoteSupportVideoMeetingEntityWithBLOBs = remoteSupportVideoMeetingService.findByCondition(id);
        return ResponseDataUtil.ok("查询视频会议记录信息成功", remoteSupportVideoMeetingEntityWithBLOBs);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "rsvm.id DESC") String orderBy,
                                       @RequestParam(value = "context", required = false) String context) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<RemoteSupportVideoMeetingEntityWithBLOBs> remoteSupportVideoMeetingEntityWithBLOBs = remoteSupportVideoMeetingService.findByCondition(null, context);
        PageInfo<RemoteSupportVideoMeetingEntityWithBLOBs> pageInfo = new PageInfo<>(remoteSupportVideoMeetingEntityWithBLOBs);
        return ResponseDataUtil.ok("查询视频会议记录信息列表成功", pageInfo);
    }


    @GetMapping("/railways/bureau")
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/signal/depot")
    public Map<String, Object> findDianWuDuanByTid(@RequestParam Integer railwaysBureauId) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(railwaysBureauId);
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/special/railway/line")
    public Map<String, Object> findXianDuanByDid(@RequestParam Integer signalDepotId) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(signalDepotId);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/station")
    public Map<String, Object> findCheZhanByXid(@RequestParam Integer specialRailwayLineId) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(specialRailwayLineId);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }

}
