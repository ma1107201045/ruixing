package configiration;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.controller.component.CustomAccessDecisionManager;
import com.yintu.ruixing.controller.component.CustomFilterInvocationSecurityMetadataSource;
import com.yintu.ruixing.controller.component.FileUsernamePasswordAuthenticationFilterComponent;
import com.yintu.ruixing.entity.rbac.UserEntity;
import com.yintu.ruixing.service.rbac.UserService;
import com.yintu.ruixing.service.rbac.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Configuration // 里面已经包含了@Component 所以不用再上下文中在引入入了
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private FileUsernamePasswordAuthenticationFilterComponent fileUsernamePasswordAuthenticationFilterComponent;
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
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * session注册bean
     *
     * @return
     */
    @Bean
    SessionRegistryImpl sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    /**
     * configure(HttpSecurity)方法定义了哪些URL路径应该被保护
     */
    protected void configure(HttpSecurity http) throws Exception {
        // ((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();
        http.authorizeRequests()
                .antMatchers("/druid/**").permitAll()// druid不需要拦截
                .antMatchers("/api/**").permitAll() //  调用api不需要拦截
                .antMatchers("/login.html").permitAll() //   登录页面不拦截
                .antMatchers(HttpMethod.POST, "/checkLogin").permitAll() //登录页面不拦截
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .anyRequest().authenticated().and()


                .formLogin()
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    UserEntity userEntity = (UserEntity) authentication.getPrincipal();
                    userEntity.setPassword(null);
                    //RespBean ok = RespBean.ok("登录成功!", hr);
                    // String s = new ObjectMapper().writeValueAsString(ok);
                    JSONObject jo = (JSONObject) JSONObject.toJSON(ResponseDataUtil.ok("登录成功!", userEntity));
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Map<String, Object> errorData = ResponseDataUtil.error(exception.getMessage());
                    // RespBean respBean = RespBean.error(exception.getMessage());
                    if (exception instanceof LockedException) {
                        //  respBean.setMsg("账户被锁定，请联系管理员!");
                        errorData = ResponseDataUtil.error("账户被锁定，请联系管理员");
                    } else if (exception instanceof CredentialsExpiredException) {
                        //  respBean.setMsg("密码过期，请联系管理员!");
                        errorData = ResponseDataUtil.error("密码过期，请联系管理员");
                    } else if (exception instanceof AccountExpiredException) {
                        //  respBean.setMsg("账户过期，请联系管理员!");
                        errorData = ResponseDataUtil.error("账户过期，请联系管理员");
                    } else if (exception instanceof DisabledException) {
                        // respBean.setMsg("账户被禁用，请联系管理员!");
                        errorData = ResponseDataUtil.error("账户被禁用，请联系管理员");
                    } else if (exception instanceof BadCredentialsException) {
                        // respBean.setMsg("用户名或者密码输入错误，请重新输入!");
                        errorData = ResponseDataUtil.error("用户名或者密码输入错误，请重新输入");
                    }
                    // out.write(new ObjectMapper().writeValueAsString(respBean));
                    JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                    out.write(jo.toJSONString());
                    out.flush();
                    out.close();
                }).permitAll().and()

                .logout().logoutSuccessHandler((req, resp, authentication) -> {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            // out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功!")));
            Map<String, Object> errorData = ResponseDataUtil.ok("注销成功！");
            JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
            out.flush();
            out.close();
        }).permitAll().and()

                .csrf().disable().exceptionHandling()
                //没有认证时，在这里处理结果，不要重定向
                .authenticationEntryPoint((req, resp, authException) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            resp.setStatus(401);
                            PrintWriter out = resp.getWriter();
                            Map<String, Object> errorData = ResponseDataUtil.noLogin("访问失败");
                            //RespBean respBean = RespBean.error("访问失败!");
                            if (authException instanceof InsufficientAuthenticationException) {
                                //respBean.setMsg("请求失败，请联系管理员!");
                                errorData = ResponseDataUtil.noLogin("请求失败，请联系管理员");
                            }
                            //out.write(new ObjectMapper().writeValueAsString(respBean));
                            JSONObject jo = (JSONObject) JSONObject.toJSON(errorData);
                            out.write(jo.toJSONString());
                            out.flush();
                            out.close();
                        }
                ).and()

                .addFilterAt(new ConcurrentSessionFilter(sessionRegistryImpl(), event -> {
                    HttpServletResponse resp = event.getResponse();
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(401);
                    PrintWriter out = resp.getWriter();
                    // out.write(new ObjectMapper().writeValueAsString(RespBean.error("您已在另一台设备登录，本次登录已下线!")));
                    Map<String, Object> errorData = ResponseDataUtil.noLogin("您已在另一台设备登录，本次登录已下线!");
                    out.flush();
                    out.close();
                }), ConcurrentSessionFilter.class).

                addFilterAt(fileUsernamePasswordAuthenticationFilterComponent, UsernamePasswordAuthenticationFilter.class);

    }


//    /**
//     * 忽略静态资源
//     */
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        /*
//         * 在springboot中忽略静态文件路径，直接写静态文件的文件夹 springboot默认有静态文件的放置路径，如果应用spring
//         * security，配置忽略路径 不应该从springboot默认的静态文件开始
//         * 如：在本项目中，所有的js和css都放在static下，如果配置忽略路径，则不能以static开始
//         * 配置成web.ignoring().antMatchers("/static/*");这样是不起作用的
//         */
//       // web.ignoring().antMatchers("/themes/**", "/script/**");
//
//    }

//    /**
//     * 配置自定义用户服务
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userDetailsService);
//////.passwordEncoder(passwordEncoder());
//
//    }


}
