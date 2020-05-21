package com.yintu.ruixing.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.entity.UserEntity;
import com.yintu.ruixing.entity.UserEntityExample;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    /**
     * T
     * 添加用户
     *
     * @param userEntity 用户信息
     */
    void add(UserEntity userEntity);

    /**
     * 修改用户
     *
     * @param userEntity 用户信息
     */
    void edit(UserEntity userEntity);

    /**
     * 删除用户
     *
     * @param id
     */
    void remove(Long id);

    /**
     * 按照id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserEntity findById(Long id);

    /**
     * 查询所有用户
     *
     * @return 用户信息
     */
    List<UserEntity> findAll();

    /**
     * 通过条件查询用户
     *
     * @param userEntityExample 条件封装类
     * @return 用户信息集
     */
    List<UserEntity> findByExample(UserEntityExample userEntityExample);

    /**
     * 查询全部用户或者按照用户名查询
     * @param username 用户名
     * @return 用户列表信息
     */
    List<UserEntity> findAllOrByUsername(String username);


}
