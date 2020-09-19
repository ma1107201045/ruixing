package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author:mlf
 * @date:2020/5/19 9:45
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermissionRoleService permissionRoleService;


    @Override
    public void add(RoleEntity roleEntity) {
        RoleEntityExample roleEntityExample = new RoleEntityExample();
        RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        criteria.andNameEqualTo(roleEntity.getName());
        List<RoleEntity> userEntities = this.findByExample(roleEntityExample);
        if (userEntities.size() > 0) {
            throw new BaseRuntimeException("添加失败，角色重复");
        }
        roleDao.insertSelective(roleEntity);
    }

    @Override
    public void edit(RoleEntity roleEntity) {
        RoleEntityExample roleEntityExample = new RoleEntityExample();
        RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        criteria.andNameEqualTo(roleEntity.getName());
        List<RoleEntity> userEntities = this.findByExample(roleEntityExample);
        if (userEntities.size() > 0 && !userEntities.get(0).getId().equals(roleEntity.getId())) {
            throw new BaseRuntimeException("修改失败，角色重复");
        }
        roleDao.updateByPrimaryKeySelective(roleEntity);
    }

    @Override
    public void remove(Long id) {
        roleDao.deleteByPrimaryKey(id);
    }

    @Override
    public RoleEntity findById(Long id) {
        return roleDao.selectByPrimaryKey(id);
    }

    @Override
    public void removeByIds(Long[] ids) {
        RoleEntityExample roleEntityExample = new RoleEntityExample();
        RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        roleDao.deleteByExample(roleEntityExample);
    }

    @Override
    public List<RoleEntity> findAll() {
        RoleEntityExample roleEntityExample = new RoleEntityExample();
        return roleDao.selectByExample(roleEntityExample);
    }

    @Override
    public List<RoleEntity> findByExample(RoleEntityExample roleEntityExample) {
        return roleDao.selectByExample(roleEntityExample);
    }

    @Override
    public void addRoleAndPermissions(RoleEntity roleEntity, Long[] permissionIds) {
        this.add(roleEntity);
        this.addPermissionsByIdAndPermissionIds(roleEntity.getId(), permissionIds);
    }

    @Override
    public void editRoleAndPermissions(RoleEntity roleEntity, Long[] permissionIds) {
        this.edit(roleEntity);
        this.addPermissionsByIdAndPermissionIds(roleEntity.getId(), permissionIds);
    }


    @Override
    public List<RoleEntity> findAllOrByName(String name) {
        List<RoleEntity> roleEntities;
        if (name == null || "".equals(name)) {
            roleEntities = this.findAll();
        } else {
            RoleEntityExample roleEntityExample = new RoleEntityExample();
            RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
            criteria.andNameLike("%" + name + "%");
            roleEntities = this.findByExample(roleEntityExample);
        }
        return roleEntities;
    }

    @Override
    public List<RoleEntity> findByIds(List<Long> ids) {
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (ids.size() > 0) {
            RoleEntityExample roleEntityExample = new RoleEntityExample();
            RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
            criteria.andIdIn(ids);
            roleEntities = roleDao.selectByExample(roleEntityExample);
        }
        return roleEntities;
    }


    @Override
    public List<RoleEntity> findByUserId(Long userId) {
        UserRoleEntityExample userRoleEntityExample = new UserRoleEntityExample();
        UserRoleEntityExample.Criteria criteria = userRoleEntityExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserRoleEntity> userRoleEntities = userRoleService.findByExample(userRoleEntityExample);
        List<Long> roleIds = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            roleIds.add(userRoleEntity.getRoleId());
        }
        return this.findByIds(roleIds);
    }


    @Override
    public List<RoleEntity> findByPermissionId(Long permissionId) {
        PermissionRoleEntityExample permissionRoleEntityExample = new PermissionRoleEntityExample();
        PermissionRoleEntityExample.Criteria criteria = permissionRoleEntityExample.createCriteria();
        criteria.andPermissionIdEqualTo(permissionId);
        List<PermissionRoleEntity> permissionRoleEntities = permissionRoleService.findByExample(permissionRoleEntityExample);
        List<Long> roleIds = new ArrayList<>();
        for (PermissionRoleEntity permissionRoleEntity : permissionRoleEntities) {
            roleIds.add(permissionRoleEntity.getRoleId());
        }
        return this.findByIds(roleIds);
    }


    @Override
    public List<TreeNodeUtil> findPermissionsTreeById(Long id, Long parentId) {
        List<PermissionEntity> permissionEntities = permissionService.findByRoleId(id, parentId);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (PermissionEntity permissionEntity : permissionEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(permissionEntity.getId());
            treeNodeUtil.setLabel(permissionEntity.getName());
            treeNodeUtil.setIcon(permissionEntity.getIconCls());
            treeNodeUtil.setChildren(this.findPermissionsTreeById(id, permissionEntity.getId()));
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", permissionEntity.getParentId());
            map.put("url", permissionEntity.getUrl());
            map.put("method", permissionEntity.getMethod());
            map.put("isMenu", permissionEntity.getIsMenu());
            map.put("path", permissionEntity.getPath());
            map.put("description", permissionEntity.getDescription());
            map.put("roleEntities", permissionEntity.getRoleEntities());
            treeNodeUtil.setA_attr(map);
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }


    @Override
    public void findPermissionsById(Long id, Long parentId, List<TreeNodeUtil> treeNodeUtils) {
        List<PermissionEntity> permissionEntities = permissionService.findByRoleId(id, parentId);
        for (PermissionEntity permissionEntity : permissionEntities) {
            List<PermissionEntity> permission = permissionService.findByRoleId(id, permissionEntity.getId());
            if (permission.size() > 0) {
                findPermissionsById(id, permissionEntity.getId(), treeNodeUtils);
            } else {
                TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
                treeNodeUtil.setId(permissionEntity.getId());
                treeNodeUtil.setLabel(permissionEntity.getName());
                treeNodeUtil.setIcon(permissionEntity.getIconCls());
                Map<String, Object> map = new HashMap<>();
                map.put("parentId", permissionEntity.getParentId());
                map.put("url", permissionEntity.getUrl());
                map.put("method", permissionEntity.getMethod());
                map.put("isMenu", permissionEntity.getIsMenu());
                map.put("path", permissionEntity.getPath());
                map.put("description", permissionEntity.getDescription());
                map.put("roleEntities", permissionEntity.getRoleEntities());
                treeNodeUtil.setA_attr(map);
                treeNodeUtils.add(treeNodeUtil);
            }
        }
    }


    @Override
    public void addPermissionsByIdAndPermissionIds(Long id, Long[] permissionIds) {
        //去重
        Set<Long> set = new HashSet<>(Arrays.asList(permissionIds));
        RoleEntity roleEntity = this.findById(id);
        if (roleEntity != null) {//判断当前用户是否存在
            //查询当前角色分配的权限
            PermissionRoleEntityExample permissionRoleEntityExample = new PermissionRoleEntityExample();
            PermissionRoleEntityExample.Criteria criteria = permissionRoleEntityExample.createCriteria();
            criteria.andRoleIdEqualTo(id);
            List<PermissionRoleEntity> permissionRoleEntities = permissionRoleService.findByExample(permissionRoleEntityExample);

            //删除当前角色分配的权限
            if (permissionRoleEntities.size() > 0) {
                List<Long> longs = new ArrayList<>();
                for (PermissionRoleEntity permissionRoleEntity : permissionRoleEntities) {
                    longs.add(permissionRoleEntity.getPermissionId());
                }
                criteria.andPermissionIdIn(longs);
                permissionRoleService.removeByExample(permissionRoleEntityExample);
            }

            //添加当前角色新分配的权限
            for (Long permissionId : set) {
                if (permissionId != null) {
                    PermissionEntity permissionEntity = permissionService.findById(permissionId);
                    if (permissionEntity != null) {
                        PermissionRoleEntity permissionRoleEntity = new PermissionRoleEntity();
                        permissionRoleEntity.setRoleId(id);
                        permissionRoleEntity.setPermissionId(permissionId);
                        permissionRoleService.add(permissionRoleEntity);
                    }
                }
            }
        }
    }
}
