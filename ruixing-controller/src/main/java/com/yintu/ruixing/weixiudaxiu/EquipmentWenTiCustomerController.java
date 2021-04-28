package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.PhoneCode;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/4/28 18:59
 * @Version 1.0
 * 需求:顾客相关操作
 */
@RestController
@RequestMapping("/customer")
public class EquipmentWenTiCustomerController {
    @Autowired
    private EquipmentWenTiOnlineAcceptService equipmentWenTiOnlineAcceptService;




    //获取验证码
    @GetMapping("/getCaptcha")
    @ResponseBody
    public void getCode(@ApiIgnore HttpSession session, @ApiIgnore HttpServletResponse response, String phone) throws IOException {
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        String code = "{\"code\":\"" + vcode + "\"}";
        System.out.println("captcha=" + code);
        PhoneCode.getCaptchaPhonemsg(phone, code);
        session.setAttribute("captcha", vcode);
        session.setAttribute("phone", phone);
        OutputStream os = response.getOutputStream();
        os.flush();
        os.close();
    }

    //客户登录
    @GetMapping("/smslogin")
    @ResponseBody
    public Map<String, Object> feedbackCaptcha(@ApiIgnore HttpServletRequest request, String code, String phonenum) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        String phone = (String) request.getSession().getAttribute("phone");
        if (code.equals(captcha) && phonenum.equals(phone)) {
            return ResponseDataUtil.ok("验证通过");
        } else {
            return ResponseDataUtil.error("验证码过期或者验证码错误,请重新获取验证码");
        }
    }
    //查询所有的铁路局
    @GetMapping("/findAllTieLuJu")
    public Map<String,Object>findAllTieLuJu(){
        List<TieLuJuEntity> tieLuJuEntityList=equipmentWenTiOnlineAcceptService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功",tieLuJuEntityList);
    }


    //查询所有的电务段
    @GetMapping("/findAllDianWuDuan")
    public Map<String,Object>findAllDianWuDuan(){
        List<DianWuDuanEntity> dianWuDuanEntityList=equipmentWenTiOnlineAcceptService.findAllDianWuDuan();
        return ResponseDataUtil.ok("查询电务段成功",dianWuDuanEntityList);
    }

    //查询所有的线段
    @GetMapping("/findAllXianDuan")
    public Map<String,Object>findAllXianDuan(){
        List<XianDuanEntity> xianDuanEntityList=equipmentWenTiOnlineAcceptService.findAllXianDuan();
        return ResponseDataUtil.ok("查询线段成功",xianDuanEntityList);
    }

    //根据铁路局 自动生成编号
    @GetMapping("/findNumber")
    public Map<String,Object>findNumber(String tljName){
        String Number=equipmentWenTiOnlineAcceptService.findNumber(tljName);
        return ResponseDataUtil.ok("生成编号成功",Number);
    }

    //查询所有的问题类型
    @GetMapping("/findWenTiType")
    public Map<String,Object>findWenTiType(){
        List<EquipmentWenTiCustomerWenTiTypeEntity>typeEntityList=equipmentWenTiOnlineAcceptService.findAllWenTiType();
        return ResponseDataUtil.ok("查询成功",typeEntityList);
    }

    //新增 客户新增反馈问题
    @PostMapping("/addAcceptFeedback")
    public Map<String,Object>addAcceptFeedback(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity){
        String feedbackusername = equipmentWenTiOnlineAcceptFeedbackEntity.getFeedbackusername();
        equipmentWenTiOnlineAcceptFeedbackEntity.setCreatename(feedbackusername);
        equipmentWenTiOnlineAcceptFeedbackEntity.setCreatetime(new Date());
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatename(feedbackusername);
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.addAcceptFeedback(equipmentWenTiOnlineAcceptFeedbackEntity);
        return ResponseDataUtil.ok("新增成功");
    }

    //客户根据id  修改对应的反馈问题
    @PutMapping("/editAcceptFeedbackById/{id}")
    public Map<String,Object>editAcceptFeedbackById(@PathVariable Integer id,EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity){
        String feedbackusername = equipmentWenTiOnlineAcceptFeedbackEntity.getFeedbackusername();
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatename(feedbackusername);
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.editAcceptFeedbackById(equipmentWenTiOnlineAcceptFeedbackEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //客户根据id  删除反馈问题
    @DeleteMapping("/deleteByIds/{ids}")
    public Map<String,Object>deleteByIds(@PathVariable Integer[] ids){
        equipmentWenTiOnlineAcceptService.deleteByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据手机号查询对应的问题反馈
    @GetMapping("/findAcceptFeedbackByphone")
    public Map<String,Object>findAcceptFeedbackByphone(Integer page,Integer size,String phone){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiOnlineAcceptFeedbackEntity>feedbackEntityList=equipmentWenTiOnlineAcceptService.findAcceptFeedbackByphone(phone);
        PageInfo<EquipmentWenTiOnlineAcceptFeedbackEntity>feedbackEntityPageInfo=new PageInfo<>(feedbackEntityList);
        return ResponseDataUtil.ok("查询成功",feedbackEntityPageInfo);
    }


}
