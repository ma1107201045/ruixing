package com.yintu.ruixing.configiration;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.component.CustomAccessDecisionManager;
import com.yintu.ruixing.component.CustomFilterInvocationSecurityMetadataSource;
import com.yintu.ruixing.component.CustomUsernamePasswordAuthenticationFilter;
import com.yintu.ruixing.entity.UserEntity;
import com.yintu.ruixing.component.VerificationCodeException;
import com.yintu.ruixing.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Configuration // 里面已经包含了@Component 所以不用再上下文中在引入入了
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private CustomAccessDecisionManager customAccessDecisionManager;
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;


    /**
     * 密码加密以及密码匹配bean
     *
     * @return
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    /**
     * session注册bean
     *
     * @return
     */
    @Bean
    public SessionRegistryImpl sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }


    /**
     * 自定义登录拦截器
     *
     * @return
     * @throws Exception
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    UserEntity userEntity = (UserEntity) authentication.getPrincipal();
                    userEntity.setPassword(null);
                    JSONObject jo = (JSONObject) JSONObject.toJSON(ResponseDataUtil.ok("登录成功", userEntity));
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }
        );
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.noLogin(authenticationException.getMessage());
                    if (authenticationException instanceof AuthenticationServiceException) {
                        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                        //服务器异常
                        errorData = ResponseDataUtil.error(authenticationException.getMessage());
                    }
                    if (authenticationException instanceof VerificationCodeException) {
                        errorData = ResponseDataUtil.noLogin("验证码不正确");
                    }
                    if (authenticationException instanceof LockedException) {
                        //  respBean.setMsg("账户被锁定，请联系管理员!");
                        errorData = ResponseDataUtil.noLogin("账户被锁定，请联系管理员");
                    } else if (authenticationException instanceof CredentialsExpiredException) {
                        //  respBean.setMsg("密码过期，请联系管理员!");
                        errorData = ResponseDataUtil.noLogin("密码过期，请联系管理员");
                    } else if (authenticationException instanceof AccountExpiredException) {
                        //  respBean.setMsg("账户过期，请联系管理员!");
                        errorData = ResponseDataUtil.noLogin("账户过期，请联系管理员");
                    } else if (authenticationException instanceof DisabledException) {
                        // respBean.setMsg("账户被禁用，请联系管理员!");
                        errorData = ResponseDataUtil.noLogin("账户被禁用，请联系管理员");
                    } else if (authenticationException instanceof BadCredentialsException) {
                        // respBean.setMsg("用户名或者密码输入错误，请重新输入!");
                        errorData = ResponseDataUtil.noLogin("用户名或者密码输入错误，请重新输入");
                    }
                    // out.write(new ObjectMapper().writeValueAsString(respBean));
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }
        );
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/login");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistryImpl());
        sessionStrategy.setMaximumSessions(1);
        customUsernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return customUsernamePasswordAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                }).and().logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = resp.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.ok("注销成功");
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }).permitAll().and().csrf()
                .disable().cors().and().exceptionHandling()
                //认证时，在这里处理结果，不要重定向
                .authenticationEntryPoint((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.noLogin("访问失败，请先登录");
                    if (authenticationException instanceof InsufficientAuthenticationException) {
                        errorData = ResponseDataUtil.noLogin("请求失败，请先登录");
                    }
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                })
                //没有权限时，在这里处理结果，不要重定向
                .accessDeniedHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException accessDeniedException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.noAuthorize(accessDeniedException.getMessage());
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }).and().addFilterAt(new ConcurrentSessionFilter(sessionRegistryImpl(), event -> {
            HttpServletResponse resp = event.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            Map<String, Object> errorData = ResponseDataUtil.noLogin("您已在另一台设备登录，本次登录已下线!");
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class).addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode", "/druid/**");
    }


}
