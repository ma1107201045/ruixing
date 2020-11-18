package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;
import java.util.List;

public interface UserService extends UserDetailsService, BaseService<UserEntity, Long> {

    void removeByIds(Long[] ids);

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
     * 添加用户以及其他信息
     *
     * @param userEntity    用户基本信息
     * @param roleIds       角色id集合
     * @param departmentIds 部门id集合
     * @param tids          铁路局id集合
     * @param dids          电务段id集合
     * @param xids          线段id集合
     * @param cids          车站id集合
     */
    void add(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, Long[] tids, Long[] dids, Long[] xids, Long[] cids);

    /**
     * 修改用户以及其他信息
     *
     * @param userEntity    用户基本信息
     * @param roleIds       角色id集合
     * @param departmentIds 部门id集合
     * @param tids          铁路局id集合
     * @param dids          电务段id集合
     * @param xids          线段id集合
     * @param cids          车站id集合
     */
    void edit(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, Long[] tids, Long[] dids, Long[] xids, Long[] cids);

    /**
     * 通过真实姓名查询用户
     *
     * @param truename 真是姓名
     * @return 用户信息集
     */
    List<UserEntity> findByTruename(String truename);

    /**
     * 查询全部用户或者按照用户名查询
     *
     * @param username 用户名
     * @return 用户列表信息
     */
    List<UserEntity> findAllOrByUsername(String username);

    /**
     * 通过用户id查询角色
     *
     * @param id 用户id
     * @return 角色信息
     */

    List<RoleEntity> findRolesById(Long id);

    /**
     * 通过用户id查询角色
     *
     * @param id 用户id
     * @return 角色信息
     */

    List<DepartmentEntity> findDepartmentsById(Long id);

    /**
     * 通过用户id查询铁路局
     *
     * @param id 用户id
     * @return 铁路局信息
     */
    List<TieLuJuEntity> findTieLuJusById(Long id);

    /**
     * 通过用户id查询电务段
     *
     * @param id 用户id
     * @return 电务段信息
     */
    List<DianWuDuanEntity> findDianWuDuansById(Long id);

    /**
     * 通过用户id查询线段
     *
     * @param id 用户id
     * @return 线段信息
     */
    List<XianDuanEntity> findXianDuansById(Long id);

    /**
     * 通过用户id查询车站
     *
     * @param id 用户id
     * @return 车站信息
     */
    List<CheZhanEntity> findCheZhansById(Long id);

    /**
     * 指定用户分配角色
     *
     * @param Id      用户id
     * @param roleIds 角色id集
     */
    void addRolesByIdAndRoleIds(Long Id, Long[] roleIds, String loginUserName);

    /**
     * @param id             用户id
     * @param departmentsIds 部门id集
     */
    void addDepartmentsByIdAndDepartmentIds(Long id, Long[] departmentsIds, String loginUsername);

    /**
     * @param id            用户ID
     * @param tids          铁路局id集合
     * @param dids          电务段id集合
     * @param xids          线段id集合
     * @param cids          车站id集合
     * @param loginUserName 登录用户名
     */
    void addDataByIdAndDataIds(Long id, Long[] tids, Long[] dids, Long[] xids, Long[] cids, String loginUserName);

    /**
     * 查询全部权限
     *
     * @param parentId 父级id
     * @param isMenu   是否是菜单项
     * @return 权限树信息集
     */
    List<TreeNodeUtil> findPermission(Long parentId, Short isMenu);

    /**
     * 通过用户id查询权限(用户菜单栏)
     *
     * @param id       用户id
     * @param parentId 父级id
     * @param isMenu   是否是菜单项
     * @return 权限树信息集
     */
    List<TreeNodeUtil> findPermissionById(Long id, Long parentId, Short isMenu);

    /**
     * 查询每个模块的权限列表
     *
     * @param description 模块标识
     * @param isMenu      是否是菜单项
     * @return 权限实体类
     */
    List<PermissionEntity> findAuthority(String description, Short isMenu);

    /**
     * 通过用户id查询每个模块的权限列表
     *
     * @param id          用户id
     * @param description 模块标识
     * @param isMenu      是否是菜单项
     * @return 权限实体类
     */
    List<PermissionEntity> findAuthorityById(Long id, String description, Short isMenu);


    /**
     * 修改用户真实姓名
     *
     * @param id       id
     * @param truename 真实姓名
     */
    void editTruenameById(Long id, String truename);

    /**
     * 查询员工总数
     *
     * @return 数量
     */
    long findUserSum();


}
