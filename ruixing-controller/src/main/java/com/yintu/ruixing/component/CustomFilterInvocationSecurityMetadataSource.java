package com.yintu.ruixing.component;

import com.yintu.ruixing.xitongguanli.PermissionEntity;
import com.yintu.ruixing.xitongguanli.RoleEntity;
import com.yintu.ruixing.xitongguanli.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/5/18 19:56
 */

/**
 * （授权）自定义资源（url）权限（role）数据源（从数据库中查出每个Permissiond对应的角色，放到Collection<ConfigAttribute>）
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PermissionService permissionService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String requestUrl = filterInvocation.getRequestUrl().split("[?]")[0];
        String requestMethod = filterInvocation.getRequest().getMethod();
        List<PermissionEntity> permissionEntities = permissionService.findPermissionAndRole();
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        for (PermissionEntity permissionEntity : permissionEntities) {
            if (permissionEntity.getUrl() != null) {
                if (antPathMatcher.match(permissionEntity.getUrl(), requestUrl) && requestMethod.toUpperCase().equals(permissionEntity.getMethod())) {
                    List<RoleEntity> roleEntities = permissionEntity.getRoleEntities();
                    for (RoleEntity roleEntity : roleEntities) {
                        configAttributes.add(new SecurityConfig(roleEntity.getName().trim()));
                    }
                }
            }
        }
        if (configAttributes.size() == 0) {
            //如果不想在数据库中的权限分配给角色则可以在此处写逻辑
            if (antPathMatcher.match("/common/**", requestUrl) ||
                    antPathMatcher.match("/audit/total/**", requestUrl) ||
                    antPathMatcher.match("/indexs/**", requestUrl)) {
                return SecurityConfig.createList("ROLE_ALL_PERMISSION");
            } else {
                return SecurityConfig.createList("ROLE_NOT_PERMISSION");

            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
