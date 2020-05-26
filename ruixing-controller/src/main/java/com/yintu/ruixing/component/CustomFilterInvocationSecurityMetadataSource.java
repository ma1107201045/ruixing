package com.yintu.ruixing.component;

import com.yintu.ruixing.common.util.StringUtils;
import com.yintu.ruixing.entity.PermissionEntity;
import com.yintu.ruixing.entity.RoleEntity;
import com.yintu.ruixing.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

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
        String requestUrl = filterInvocation.getRequestUrl();
        String requestMethod = filterInvocation.getRequest().getMethod();
        //restful 风格api /users/1/roles  数据库是/users+请求方式
        String[] strArray = requestUrl.split("/");
        StringBuilder newRequestUrl = new StringBuilder();
        for (String s : strArray) {
            if (!StringUtils.isNumber(s)) {
                newRequestUrl.append("/").append(s);
            }
        }
        List<PermissionEntity> permissionEntities = permissionService.findPermissionAndRole();
        for (PermissionEntity permissionEntity : permissionEntities) {
            if (antPathMatcher.match(permissionEntity.getUrl() == null ? "" : permissionEntity.getUrl(), newRequestUrl.toString()) && requestMethod.equals(permissionEntity.getMethod() == null ? permissionEntity.getMethod() : permissionEntity.getMethod().toUpperCase())) {
                List<RoleEntity> roleEntities = permissionEntity.getRoleEntities();
                String[] str = new String[roleEntities.size()];
                for (int i = 0; i < roleEntities.size(); i++) {
                    str[i] = roleEntities.get(i).getName();
                }
                return SecurityConfig.createList(str.length == 0 ? new String[]{"ROLE_LOGIN"} : str);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
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
