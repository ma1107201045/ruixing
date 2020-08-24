package com.yintu.ruixing.paigongguanli;

import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:10
 * @Version 1.0
 * 需求:派工管理   日勤
 */
@RestController
@RequestMapping("/RiQinAll")
public class PaiGongGuanLiRiQinController {
    @Autowired
    private PaiGongGuanLiRiQinService paiGongGuanLiRiQinService;

    @Autowired
    private UserService userService;

    //查询员工姓名  等信息
    @GetMapping("/findAllUserName")
    public Map<String, Object> findAllUserName(String truename) {
        List<UserEntity> userEntities = userService.findAllOrByUsername(truename,(short) 0);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //查询所有的日勤管理数据
    @GetMapping("/findAllRiQin")
    public Map<String,Object>findAllRiQin(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQin(page,size);
    }
}
