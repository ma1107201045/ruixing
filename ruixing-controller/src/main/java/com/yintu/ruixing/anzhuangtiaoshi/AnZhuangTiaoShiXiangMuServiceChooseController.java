package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    //根据线段id  查询对应的
    @GetMapping("/findAllByXDid/{xdid}")
    public Map<String,Object>findAllByXDid(@PathVariable Integer xdid,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityList=anZhuangTiaoShiXiangMuServiceChooseService.findAllByXDid(xdid,page,size);
        PageInfo<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityPageInfo=new PageInfo<>(xiangMuServiceChooseEntityList);
        return ResponseDataUtil.ok("查询车站数据成功",xiangMuServiceChooseEntityPageInfo);
    }
}
