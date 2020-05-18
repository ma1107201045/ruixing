package com.yintu.ruixing.controller.component;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
/*
 *这个类的作用，主要是根据用户传来的请求地址，分析出请求需要的角色
 */

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
//    @Autowired
//    private MenuService menuService;
//    AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        String requestUrl = ((FilterInvocation) object).getRequestUrl();
//        List<Menu> menus = menuService.getAllMenusWithRole();
//        for (Menu menu : menus) {
//            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
//                List<Role> roles = menu.getRoles();
//                String[] str = new String[roles.size()];
//                for (int i = 0; i < roles.size(); i++) {
//                    str[i] = roles.get(i).getName();
//                }
//                return SecurityConfig.createList(str);
//            }
//        }
//        return SecurityConfig.createList("ROLE_LOGIN");
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return true;
//    }
}
