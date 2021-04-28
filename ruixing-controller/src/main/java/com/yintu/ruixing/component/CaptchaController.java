package com.yintu.ruixing.component;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.PhoneCode;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/11/26 14:02
 * @Version: 1.0
 */
@Controller
@RequestMapping("/captcha")
@Api(tags = "登录有关接口")
public class CaptchaController {


    @GetMapping("/getCaptcha")
    public void getCode(@ApiIgnore HttpSession session, @ApiIgnore HttpServletResponse response,String phone) throws IOException {
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        String vcode="";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        String code="{\"code\":\""+vcode+"\"}";
        System.out.println("captcha="+code);
        PhoneCode.getCaptchaPhonemsg(phone,code);
        session.setAttribute("captcha", vcode);
        OutputStream os = response.getOutputStream();
        os.flush();
        os.close();
    }

    @GetMapping("/smsgoin")
    public Map<String,Object> feedbackCaptcha( @ApiIgnore HttpServletRequest request, String code){
        String captcha =(String) request.getSession().getAttribute("captcha");
        if (code.equals(captcha)){
           return ResponseDataUtil.ok("验证通过");
        }else {
            return ResponseDataUtil.error("验证码过期或者验证码错误,请重新获取验证码");
        }
    }


}
