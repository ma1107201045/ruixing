/*
package com.yintu.ruixing.component;


import com.alibaba.fastjson.JSONObject;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.filter.VerificationCodeFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;



@Component
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private VerificationCodeFilter verificationCodeFilter;

    @Autowired
    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    @Autowired
    private AccessDecisionManager accessDecisionManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //对默认的UserDetailsService进行覆盖
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                object.setAccessDecisionManager(accessDecisionManager);
                return object;
            }
        }).anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/sms/login").successHandler((request, response, authenticationException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            JSONObject jo = (JSONObject) JSONObject.toJSON(ResponseDataUtil.ok("登录成功", authenticationException.getPrincipal()));
            servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
            servletOutputStream.flush();
            servletOutputStream.close();

        }).permitAll().failureHandler((request, response, authenticationException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            Map<String,Object> resultDataUtil;
            if (authenticationException instanceof BadCredentialsException) {
                resultDataUtil = ResponseDataUtil.error("用户名或者密码输入错误，请重新输入");
            } else if (authenticationException instanceof DisabledException) {
                resultDataUtil = ResponseDataUtil.error("账户被禁用，请联系管理员");
            } else if (authenticationException instanceof LockedException) {
                resultDataUtil = ResponseDataUtil.error("账户被锁定，请联系管理员");
            } else if (authenticationException instanceof CredentialsExpiredException) {
                resultDataUtil = ResponseDataUtil.error("密码过期，请联系管理员");
            } else if (authenticationException instanceof AccountExpiredException) {
                resultDataUtil = ResponseDataUtil.error("账户过期，请联系管理员");
            } else if (authenticationException instanceof AuthenticationServiceException) {
                resultDataUtil = ResponseDataUtil.noAuthorize(authenticationException.getMessage());
            } else {
                resultDataUtil = ResponseDataUtil.error("登录异常");
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(resultDataUtil);
            servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
            servletOutputStream.flush();
            servletOutputStream.close();
        }).permitAll()
                .and().rememberMe().userDetailsService(userDetailsService).tokenValiditySeconds(60 * 60 * 24 * 365).rememberMeParameter("rememberMe").rememberMeCookieName("REMEMBERME")
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    Map<String,Object> resultDataUtil = ResponseDataUtil.ok("注销成功");
                    JSONObject jo = (JSONObject) JSONObject.toJSON(resultDataUtil);
                    servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
                    servletOutputStream.flush();
                    servletOutputStream.close();

                }).permitAll()
//                .and().httpBasic().authenticationEntryPoint((request, response, authenticationException) -> { //没有登录权限时，在这里处理结果，不要重定向
//            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//            response.setStatus(HttpServletResponse.SC_OK);
//            PrintWriter out = response.getOutputStream();
//            Map<String, Object> errorData = ResponseDataUtil.noLogin(authenticationException.getMessage());
//            JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
//            out.write(jo.toJSONString());
//            out.flush();
//            out.close();
//        })
                .and().exceptionHandling()
                .authenticationEntryPoint((request, response, authenticationException) -> { //没有登录权限时，在这里处理结果，不要重定向
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    Map<String,Object> resultDataUtil = ResponseDataUtil.noAuthorize(authenticationException.getMessage());
                    JSONObject jo = (JSONObject) JSONObject.toJSON(resultDataUtil);
                    servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
                    servletOutputStream.flush();
                    servletOutputStream.close();
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {  //没有访问权限时，在这里处理结果，不要重定向
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    Map<String,Object> resultDataUtil = ResponseDataUtil.noAuthorize(accessDeniedException.getMessage());
                    JSONObject jo = (JSONObject) JSONObject.toJSON(resultDataUtil);
                    servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
                    servletOutputStream.flush();
                    servletOutputStream.close();
                }).and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(event -> {//用户登录踢出上一个相同用户
                    HttpServletResponse response = event.getResponse();
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    Map<String,Object> resultDataUtil = ResponseDataUtil.noAuthorize("您已在另一台设备登录，本次登录已下线");
                    JSONObject jo = (JSONObject) JSONObject.toJSON(resultDataUtil);
                    servletOutputStream.write(jo.toJSONString().getBytes(StandardCharsets.UTF_8));
                    servletOutputStream.flush();
                    servletOutputStream.close();
                });
        //开启跨域访问
        http.cors();
        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        http.csrf().disable();
    }




    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                //忽略验证码接口
                .antMatchers("/captcha", "/files/**")
                //忽略API文档接口
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs", "/webjars/**", "/doc.html")
                //忽略druid接口
                .antMatchers("/druid/**")
                //忽略远程登录接口
                .antMatchers("/remote/**");

    }
}
*/
