package com.yintu.ruixing.entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
    private static final long serialVersionUID = 7348214022276474920L;
    private Long id;

    private String username;

    private String password;

    private String trueName;

    private String phone;

    private String email;

    private Short authType;

    private Short locked;

    private Short enabled;

    private String salt;

    private Date loginTime;

    private List<RoleEntity> roleEntitys;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>(roleEntitys.size());
        for (RoleEntity roleEntity : roleEntitys) {
            list.add(new SimpleGrantedAuthority(roleEntity.getName()));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled == 1;
    }


}