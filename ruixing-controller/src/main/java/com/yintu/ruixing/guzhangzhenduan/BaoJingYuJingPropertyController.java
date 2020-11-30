package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.guzhangzhenduan.CheZhanDao;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-06-17 10
 */
@RestController
@RequestMapping("/BaoJingYuJing")
public class BaoJingYuJingPropertyController {
    @Autowired
    private BaoJingYuJingPropertyService baoJingYuJingPropertyService;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;

    @Autowired
    private CheZhanDao cheZhanDao;

    //查询报警预警树形结构
    @RequestMapping
    public Map<String, Object> findBaoJingYuJingTree() {
        List<TreeNodeUtil> treeNodeUtils = baoJingYuJingPropertyService.findBaoJingYuJingTree(-1);
        return ResponseDataUtil.ok("查询报警预警树结构成功", treeNodeUtils);
    }

    //查询所有的预警报警信息
    @GetMapping("/findAllYuJingBaoJing")
    public Map<String, Object> findAllYuJingBaoJing(@RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "size", required = false) Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<BaoJingYuJingEntity> baoJingYuJingEntities = baoJingYuJingPropertyService.findAllYuJingBaoJing(page, size);
        js.put("baoJingYuJingEntities", baoJingYuJingEntities);
        PageInfo<BaoJingYuJingEntity> pageInfo = new PageInfo<>(baoJingYuJingEntities);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询所有数据成功", js);
    }

    //查询所有的区段
    @GetMapping("/findAllQuDuan")
    public Map<String, Object> findAllQuDuan() {
        List<QuDuanBaseEntity> quduan = baoJingYuJingPropertyService.findAllQuDuan();
        /*List<QuDuanBaseEntity> list=null;
        for (QuDuanBaseEntity quDuanBaseEntity : quduan) {
            if (quDuanBaseEntity.getQuduanyunyingName()!=null){
                list.add(quDuanBaseEntity);
            }
        }*/
        return ResponseDataUtil.ok("查询区段成功", quduan);
    }

    //根据搜索  查询对应的预警报警信息
    @GetMapping("/findYuJingBaoJingBySouSuo")
    public Map<String, Object> findYuJingBaoJingBySouSuo(Integer[] ids, Integer sid, Integer qid,
                                                         Date startTime, Date huifuTime, Integer tianChuang,
                                                         Integer lvChuHuiFu, Integer lvChuKaiTong,
                                                         @RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "size", required = false) Integer size) {
        try {
            JSONObject js = new JSONObject();
            PageHelper.startPage(page, size);
            List<BaoJingYuJingEntity> baoJingYuJingEntities = baoJingYuJingPropertyService.findYuJingBaoJingBySouSuo(ids,
                    sid, qid, startTime, huifuTime, tianChuang, lvChuHuiFu, lvChuKaiTong, page, size);
            js.put("baoJingYuJingEntities", baoJingYuJingEntities);
            PageInfo<BaoJingYuJingEntity> pageInfo = new PageInfo<>(baoJingYuJingEntities);
            js.put("pageInfo", pageInfo);
            return ResponseDataUtil.ok("搜索数据成功", js);
        } catch (Exception e) {
            return ResponseDataUtil.error("请选择要查询的报警或者预警名称");
        }
    }

    //根据车站czid 统计未读报警预警的数量
    @GetMapping("/findAlarmNumber")
    public Map<String, Object> findAlarmNumber(Integer dwdid, Integer xdid, Integer czid) {
        Date dayTime = new Date();
        Integer AlarmNumber=null;
        if (dwdid == null && xdid == null && czid == null){
             AlarmNumber = baoJingYuJingPropertyService.findAllAlarmNumber();
        }
        if (dwdid != null && xdid == null && czid == null){
            AlarmNumber = baoJingYuJingPropertyService.findAllAlarmNumberByDWDid(dwdid);
        }
        if (dwdid != null && xdid != null && czid == null){
            AlarmNumber = baoJingYuJingPropertyService.findAllAlarmNumberByXDid(dwdid,xdid);
        }
        if (czid !=null){
            String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
             AlarmNumber = baoJingYuJingPropertyService.findAlarmNumber(tableName);
        }
        return ResponseDataUtil.ok("查询成功", AlarmNumber);
    }


    //更改未读状态
    @PutMapping("/editAlarmState")
    public Map<String, Object> editAlarmState(AlarmTableEntity alarmTableEntity) {
        baoJingYuJingPropertyService.editAlarmState(alarmTableEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //查询所有未读的报警预警数据
    @GetMapping("/findAllNotReadAlarmDatas")
    public Map<String, Object> findAllNotReadAlarmDatas(Integer page, Integer size,Integer czid) {
        if (czid==null) {
            List<AlarmEntity> alarmEntityList = baoJingYuJingPropertyService.findAllNotReadAlarmDatas(page, size);
            PageInfo<AlarmEntity> alarmEntityPageInfo = new PageInfo<>(alarmEntityList);
            return ResponseDataUtil.ok("查询数据成功", alarmEntityPageInfo);
        }else {
            List<AlarmEntity> alarmEntityList = baoJingYuJingPropertyService.findAllNotReadAlarmDatasByCZid(page, size,czid);
            PageInfo<AlarmEntity> alarmEntityPageInfo = new PageInfo<>(alarmEntityList);
            return ResponseDataUtil.ok("查询数据成功", alarmEntityPageInfo);
        }
    }

    //查询历史记录的预警报警数据
    @GetMapping("/findAllHistoryAlarmDatas")
    public Map<String, Object> findAllHistoryAlarmDatas(Integer page, Integer size, Integer czid) {
        Date dayTime = new Date();
        String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            PageHelper.startPage(page, size);
            List<AlarmEntity> alarmEntityList = baoJingYuJingPropertyService.findAllHistoryAlarmDatas(page, size, tableName);
            PageInfo<AlarmEntity> alarmEntityPageInfo = new PageInfo<>(alarmEntityList);
            return ResponseDataUtil.ok("查询数据成功", alarmEntityPageInfo);
        }
        return ResponseDataUtil.ok("查询数据成功", new ArrayList<>());
    }


    //根据选择的条件 查询对应的历史报警预警数据
    @GetMapping("/findSomeAlarmDatasByChoose")
    public Map<String, Object> findSomeAlarmDatasByChoose(Date starTime, Date endTime, Integer dwdid, Integer xdid, Integer czid, Integer page, Integer size) {
        List<AlarmEntity> alarmEntityList = baoJingYuJingPropertyService.findSomeAlarmDatasByChoose(starTime, endTime, dwdid, xdid, czid, page, size);
        PageInfo<AlarmEntity> alarmEntityPageInfo = new PageInfo<>(alarmEntityList);
        return ResponseDataUtil.ok("查询数据成功", alarmEntityPageInfo);
    }


}
