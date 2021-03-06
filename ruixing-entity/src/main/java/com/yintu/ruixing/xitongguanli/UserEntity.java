package com.yintu.ruixing.xitongguanli;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
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
    private static final long serialVersionUID = 4205226663617649421L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String username;
    @JsonIgnore
    @JSONField(serialize = false)
    private String password;

    private String trueName;

    private String phone;

    private String email;

    private Short authType;

    private Short locked;

    private Short enableds;

    private Short isCustomer;

    private Integer paiGongGuanLiState;

    private List<DepartmentEntity> departmentEntities;

    private List<RoleEntity> roleEntities;

    private List<TieLuJuEntity> tieLuJuEntities;

    private List<DianWuDuanEntity> dianWuDuanEntities;

    private List<XianDuanEntity> xianDuanEntities;

    private List<CheZhanEntity> cheZhanEntities;


    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>(roleEntities.size());
        for (RoleEntity roleEntity : roleEntities) {
            list.add(new SimpleGrantedAuthority(roleEntity.getName()));
        }
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否没没被锁
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return locked != null && locked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否开启
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return enableds != null && enableds == 1;
    }
}