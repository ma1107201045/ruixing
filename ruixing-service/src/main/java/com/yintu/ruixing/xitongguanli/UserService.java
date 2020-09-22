package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.io.OutputStream;
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
     * 添加用户并且分配角色
     *
     * @param userEntity 用户信息
     * @param roleIds    角色id集
     */
    void addUserAndRoles(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, String loginUserName);

    /**
     * 修改用户并且重新分配角色
     *
     * @param userEntity 用户信息
     * @param roleIds    角色id集
     */

    void editUserAndRoles(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, String loginUserName);

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
     * 指定用户分配角色
     *
     * @param Id      用户id
     * @param roleIds 角色id集
     */
    void addRolesByIdAndRoleIds(Long Id, Long[] roleIds);

    /**
     * @param id             用户id
     * @param departmentsIds 部门id集
     */
    void addDepartmentsByIdAndDepartmentIds(Long id, Long[] departmentsIds, String loginUsername);


    /**
     * 通过用户id查询权限(用户菜单栏)
     *
     * @param id       用户id
     * @param parentId 父级id
     * @return 权限树信息集
     */
    List<TreeNodeUtil> findPermissionById(Long id, Long parentId, Short isMenu);

    /**
     * 查询全部权限
     *
     * @return 权限树信息集
     */
    List<TreeNodeUtil> findPermission(Long parentId, Short isMenu);

    /**
     * 修改用户真实姓名
     *
     * @param id       id
     * @param truename 真实姓名
     */
    void editTruenameById(Long id, String truename);


}
