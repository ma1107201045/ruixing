package com.yintu.ruixing.configiration;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.component.*;
import com.yintu.ruixing.exception.VerificationCodeException;
import com.yintu.ruixing.filter.CustomUsernamePasswordAuthenticationFilter;
import com.yintu.ruixing.xitongguanli.impl.UserServiceImpl;
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
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

@Configuration // 里面已经包含了@Component 所以不用再上下文中在引入入了
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    private CustomAccessDecisionManager customAccessDecisionManager;


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
     * 免密登录
     *
     * @return
     */
    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(UUID.randomUUID().toString(), userServiceImpl);
        tokenBasedRememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 365);
        tokenBasedRememberMeServices.setParameter("rememberMe");
        tokenBasedRememberMeServices.setCookieName("rememberMe");
        return tokenBasedRememberMeServices;
    }

    @Bean
    public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistryImpl());
        sessionStrategy.setMaximumSessions(1);
        sessionStrategy.setExceptionIfMaximumExceeded(false);
        return sessionStrategy;
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
                    sessionRegistryImpl().registerNewSession(httpServletRequest.getSession().getId(), authentication.getPrincipal());
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    JSONObject jo = (JSONObject) JSONObject.toJSON(ResponseDataUtil.ok("登录成功", authentication.getPrincipal()));
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }
        );
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.error(authenticationException.getMessage());
                    if (authenticationException instanceof VerificationCodeException) {
                        errorData = ResponseDataUtil.error("验证码不正确");
                    } else if (authenticationException instanceof BadCredentialsException) {
                        errorData = ResponseDataUtil.error("用户名或者密码输入错误，请重新输入");
                    } else if (authenticationException instanceof DisabledException) {
                        errorData = ResponseDataUtil.error("账户被禁用，请联系管理员");
                    } else if (authenticationException instanceof LockedException) {
                        errorData = ResponseDataUtil.error("账户被锁定，请联系管理员");
                    } else if (authenticationException instanceof CredentialsExpiredException) {
                        errorData = ResponseDataUtil.error("密码过期，请联系管理员");
                    } else if (authenticationException instanceof AccountExpiredException) {
                        errorData = ResponseDataUtil.error("账户过期，请联系管理员");
                    } else if (authenticationException instanceof AuthenticationServiceException) {
                        errorData = ResponseDataUtil.error(authenticationException.getMessage());
                    }
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }
        );
        customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/login");
        customUsernamePasswordAuthenticationFilter.setRememberMeServices(tokenBasedRememberMeServices());
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(concurrentSessionControlAuthenticationStrategy());
        return customUsernamePasswordAuthenticationFilter;
    }


    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        return new ConcurrentSessionFilter(sessionRegistryImpl(), sessionInformationExpiredStrategy -> {
            tokenBasedRememberMeServices().loginFail(sessionInformationExpiredStrategy.getRequest(), sessionInformationExpiredStrategy.getResponse());
            HttpServletResponse resp = sessionInformationExpiredStrategy.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            Map<String, Object> errorData = ResponseDataUtil.error("您已在另一台设备登录，本次登录已下线");
            JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
            out.write(jo.toJSONString());
            out.flush();
            out.close();
        });
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode", "/druid/**", "/websocket/**", "/test/**", "/files/**", "/backups/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**",
                "/remote/support/video/meetings/add/join/person/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userServiceImpl)
                .rememberMe()
                .rememberMeServices(tokenBasedRememberMeServices()).and().authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        return object;
                    }
                })
                .and().logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = resp.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.ok("注销成功");
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }).permitAll()
                .and().csrf().disable().cors()
                .and().exceptionHandling()
                //没有登录权限时，在这里处理结果，不要重定向
                .authenticationEntryPoint((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.noLogin(authenticationException.getMessage());
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                })
                //没有访问权限时，在这里处理结果，不要重定向
                .accessDeniedHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException accessDeniedException) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = httpServletResponse.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.noAuthorize(accessDeniedException.getMessage());
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }).and().addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).addFilterAt(concurrentSessionFilter(), ConcurrentSessionFilter.class);
    }


}
