package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:35
 * @Version 1.0
 * 需求:安装调试 项目
 */
@RestController
@RequestMapping("/XiangMuServiceChooseAll")
public class AnZhuangTiaoShiXiangMuServiceChooseController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseService anZhuangTiaoShiXiangMuServiceChooseService;

    //新增车站数据
    @PostMapping("/addXiangMuServiceChoose")
    public Map<String, Object> addXiangMuServiceChoose(@RequestBody JSONArray cheZhanDatas){
        anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChoose(cheZhanDatas, this.getLoginTrueName(), this.getLoginUserId().intValue());
        return ResponseDataUtil.ok("新增成功");
    }


    //根据线段id  查询对应的车站数据
    @GetMapping("/findAllByXDidddd/{xdid}")
    public Map<String, Object> findAllByXDidddd(@RequestParam("page_number") Integer pageNumber, @RequestParam("page_size") Integer pageSize, @PathVariable Integer xdid) {
        JSONObject jsonObject = anZhuangTiaoShiXiangMuServiceChooseService.findAllByXdId(pageNumber, pageSize, xdid);
        return ResponseDataUtil.ok("查询车站数据成功", jsonObject);
    }

}
