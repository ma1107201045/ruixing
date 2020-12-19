package com.yintu.ruixing.guzhangzhenduan;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserDataEntity;
import com.yintu.ruixing.xitongguanli.UserDataService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketEntityWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/8/18 19:29
 */
@Controller
@RequestMapping("/skylight/times")
public class SkylightTimeController extends SessionController {
    @Autowired
    private SkylightTimeService skylightTimeService;
    @Autowired
    private DataStatsService dataStatsService;
    @Autowired
    private UserDataService userDataService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated SkylightTimeEntity entity, Integer[] qdIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        skylightTimeService.add(entity, qdIds);
        return ResponseDataUtil.ok("添加天窗时间信息成功");
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        skylightTimeService.remove(ids);
        return ResponseDataUtil.ok("删除天窗时间信息成功");
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "st.id DESC") String orderBy,
                                       @RequestParam(value = "cz_id", required = false) Integer czId,
                                       @RequestParam(value = "qd_id", required = false) Integer qdId,
                                       @RequestParam(value = "start_time", required = false) Date startTime,
                                       @RequestParam(value = "end_time", required = false) Date endTime) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SkylightTimeEntity> skylightTimeEntities = skylightTimeService.findByCondition(null, startTime, endTime, czId, qdId);
        PageInfo<SkylightTimeEntity> pageInfo = new PageInfo<>(skylightTimeEntities);
        return ResponseDataUtil.ok("查询天窗时间列表信息成功", pageInfo);
    }


    @GetMapping("/railways/bureau")
    @ResponseBody
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        UserEntity userEntity = this.getLoginUser();//判断是否是客户用户
        if (userEntity.getIsCustomer() == 1) {
            List<UserDataEntity> userDataEntities = userDataService.findByUserId(userEntity.getId());
            tieLuJuEntities = tieLuJuEntities.stream().filter(tieLuJuEntity -> {
                boolean result = false;
                for (UserDataEntity userDataEntity : userDataEntities) {
                    if (tieLuJuEntity.getTid() == userDataEntity.getTId()) {
                        result = true;
                        break;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/signal/depot")
    @ResponseBody
    public Map<String, Object> findDianWuDuanByTid(@RequestParam Integer tid) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(tid);
        UserEntity userEntity = this.getLoginUser();//判断是否是客户用户
        if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
            List<UserDataEntity> userDataEntities = userDataService.findByUserId(userEntity.getId());
            dianWuDuanEntities = dianWuDuanEntities.stream().filter(dianWuDuanEntity -> {
                boolean result = false;
                for (UserDataEntity userDataEntity : userDataEntities) {
                    if (dianWuDuanEntity.getDid() == userDataEntity.getDId()) {
                        result = true;
                        break;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/special/railway/line")
    @ResponseBody
    public Map<String, Object> findXianDuanByDid(@RequestParam Integer did) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(did);
        UserEntity userEntity = this.getLoginUser();//判断是否是客户用户
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(userEntity.getId());
        if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
            xianDuanEntities = xianDuanEntities.stream().filter(xianDuanEntity -> {
                boolean result = false;
                for (UserDataEntity userDataEntity : userDataEntities) {
                    if (xianDuanEntity.getXid().equals(userDataEntity.getXId())) {
                        result = true;
                        break;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/station")
    @ResponseBody
    public Map<String, Object> findCheZhanByXid(@RequestParam Integer xid) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(xid);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }

    //根据车站的id  查找区段的名字和id
    @GetMapping("/section")
    @ResponseBody
    public Map<String, Object> findQuDuanByCid(@RequestParam Integer stationId) {
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findQuDuanByCid(stationId);
        return ResponseDataUtil.ok("查询区段成功", quDuanBaseEntities);
    }


    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = skylightTimeService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("导入天窗时间列表信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importData(@RequestBody String[][] context) {
        skylightTimeService.importData(context, this.getLoginUserName());
        return ResponseDataUtil.ok("导入天窗时间列表信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "天窗时间列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        skylightTimeService.templateFile(response.getOutputStream());
    }
}
