package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/8/3 20:25
 */
@RestController
@RequestMapping("/quduan/infos")
public class QuDuanInfoController extends SessionController {
    @Autowired
    private QuDuanDownloadService quDuanDownloadService;
    @Autowired
    private QuDuanInfoService quDuanInfoService;
    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping("/receives")
    public Map<String, Object> changeDataStatus(@RequestParam("czId") Integer czId, @RequestParam("dataStatus") Short dataStatus) {
        Integer id = quDuanDownloadService.changeDataStatus(czId, this.getLoginUserId().intValue(), dataStatus);
        webSocketServer.sendMessage(czId, id);
        return ResponseDataUtil.ok("改变数据接收状态成功");
    }

    @PostMapping("/switchs")
    public Map<String, Object> changeSwitchStatus(@RequestParam("czId") Integer czId, @RequestParam("switchStatus") Short switchStatus) {
        quDuanDownloadService.changeSwitchStatus(czId, this.getLoginUserId().intValue(), switchStatus);
        return ResponseDataUtil.ok("改变防呆开关状态成功");
    }

    @PostMapping("/update/time")
    public Map<String, Object> changeUpdateTime(@RequestParam("czId") Integer czId) {
        Short switchStatus = quDuanDownloadService.changeUpdateTime(czId, this.getLoginUserId().intValue());
        return ResponseDataUtil.ok("改变更新时间成功", switchStatus);
    }

    @GetMapping
    public Map<String, Object> findByCzIdAndUserId(@RequestParam("czId") Integer czId) {
        QuDuanDownloadEntity quDuanDownloadEntity = quDuanDownloadService.findByCzIdAndUserId(czId, this.getLoginUserId().intValue());
        return ResponseDataUtil.ok("查询数据状态成功", quDuanDownloadEntity);
    }


    /**
     * @param czId 车站id
     * @return
     */
    @GetMapping("/properties")
    public Map<String, Object> findNullProperties(@RequestParam("czId") Integer czId) {
        JSONObject jsonObjects = quDuanInfoService.findNullProperties(czId);
        return ResponseDataUtil.ok("查询区段属性成功", jsonObjects);
    }

    /**
     *
     * @param czId 车站id
     * @return
     */
    @GetMapping("/data")
    public Map<String, Object> findByCondition(@RequestParam("czId") Integer czId) {
        List<JSONObject> jsonObjects = quDuanInfoService.findByCondition(czId, null, null);
        return ResponseDataUtil.ok("查询区段详情成功", jsonObjects);
    }

    /**
     * 按照车站随机取出一条区段详情
     *
     * @param czId 区段id
     * @return
     */
    @GetMapping("/random")
    public Map<String, Object> findLastBycZId(@RequestParam("czId") Integer czId) {
        QuDuanInfoEntityV2 quDuanInfoEntity = quDuanInfoService.findLastBycZId(czId);
        return ResponseDataUtil.ok("查询区段详情成功", quDuanInfoEntity);
    }

}
