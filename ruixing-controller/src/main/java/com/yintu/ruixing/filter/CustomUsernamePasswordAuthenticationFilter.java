package com.yintu.ruixing.filter;

import com.yintu.ruixing.exception.VerificationCodeException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:mlf
 * @date:2020/5/18 16:23
 */

/**
 * （登录认证）自定义用户名密码认证过滤器
 */

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 校验验证码
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        //String verify_code = (String) request.getSession().getAttribute("verify_code");
        //String code = request.getParameter("code");
        // checkCode(response, code, verify_code);
        if (request.getContentType().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            String username = super.obtainUsername(request);
            String password = super.obtainPassword(request);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            super.setDetails(request, authRequest);
            return super.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new VerificationCodeException("验证码不正确");
        }
    }


}
