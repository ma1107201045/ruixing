package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/5/19 12:13
 */
public interface PermissionService extends BaseService<PermissionEntity, Long> {


    /**
     * 按照指定条件删除权限
     *
     * @param permissionEntityExample 权限id
     */
    void removeByExample(PermissionEntityExample permissionEntityExample);


    /**
     * @param permissionEntityExample 多条件查询权限列表
     * @return 权限集
     */
    List<PermissionEntity> findByExample(PermissionEntityExample permissionEntityExample);

    /**
     * 获取权限tree
     *
     * @param parentId 父级id
     * @return 权限树
     */
    List<TreeNodeUtil> findPermissionTree(Long parentId);


    /**
     * 指定删除id，以及次节点父节点
     *
     * @param id      权限id
     * @param isFirst 是否第一次
     */
    void removeByIdAndIsFirst(Long id, Boolean isFirst);



    /**
     * @param roleId 角色id
     * @return 权限集
     */
    List<PermissionEntity> findByRoleId(Long roleId, Long parentId);


    /**
     * @param ids 权限id集合
     * @return 权限集
     */
    List<PermissionEntity> findByIds(List<Long> ids, Long parentId);





    /**
     * 查询权限集以及对应的角色集
     *
     * @return 权限集
     */
    List<PermissionEntity> findPermissionAndRole();


}
