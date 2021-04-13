package com.yintu.ruixing.paigongguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/4/13 14:39
 * @Version 1.0
 * 需求:报工评论
 */
@RestController
@RequestMapping("/commentAll")
public class PaiGongGuanLiBaoGongCommentController extends SessionController {
    @Autowired
    private PaiGongGuanLiBaoGongCommentService paiGongGuanLiBaoGongCommentService;



    //新增评论
    @PostMapping("/addComment")
    public Map<String,Object>addComment(PaiGongGuanLiBaoGongCommentEntity paiGongGuanLiBaoGongCommentEntity){
        Integer userid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiBaoGongCommentEntity.setUserid(userid);
        paiGongGuanLiBaoGongCommentEntity.setUsername(username);
        paiGongGuanLiBaoGongCommentEntity.setCreatename(username);
        paiGongGuanLiBaoGongCommentEntity.setCreatetime(new Date());
        paiGongGuanLiBaoGongCommentEntity.setUpdatename(username);
        paiGongGuanLiBaoGongCommentEntity.setUpdatetime(new Date());
        paiGongGuanLiBaoGongCommentService.addComment(paiGongGuanLiBaoGongCommentEntity);
        return ResponseDataUtil.ok("评论成功");
    }

    //查询评论
    @GetMapping("/findComment")
    public Map<String,Object>findComment(Integer baoGongId,Integer baogongtype){
        List<PaiGongGuanLiBaoGongCommentEntity>commentEntityList=paiGongGuanLiBaoGongCommentService.findComment(baoGongId,baogongtype);
        return ResponseDataUtil.ok("查询评论成功",commentEntityList);
    }





}
