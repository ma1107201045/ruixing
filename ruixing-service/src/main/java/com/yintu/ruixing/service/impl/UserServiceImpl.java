package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.common.enumobject.EnumAuthType;
import com.yintu.ruixing.common.enumobject.EnumFlag;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.dao.UserDao;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private CustomerUnitsService customerUnitsService;
    @Autowired
    private DepartmentUserService departmentUserService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CustomerDutyService customerDutyService;
    @Autowired
    private DistrictService districtService;


    @Override
    public void add(UserEntity userEntity) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIsCustomerEqualTo(userEntity.getIsCustomer());
        criteria.andUsernameEqualTo(userEntity.getUsername());
        List<UserEntity> userEntities = this.findByExample(userEntityExample);
        if (userEntities.size() > 0) {
            throw new BaseRuntimeException("添加失败，用户名重复");
        }
        Short authType = userEntity.getAuthType();
        userEntity.setAuthType(authType == null ? (short) 0 : authType);
        userEntity.setLocked((short) 0);
        userEntity.setCreateTime(new Date());
        String password = userEntity.getPassword();
        if (password != null && !password.isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userEntity.setPassword(passwordEncoder.encode(password));
        }
        userDao.insertSelective(userEntity);
    }

    @Override
    public void edit(UserEntity userEntity) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIsCustomerEqualTo(userEntity.getIsCustomer());
        criteria.andUsernameEqualTo(userEntity.getUsername());
        List<UserEntity> userEntities = this.findByExample(userEntityExample);
        if (userEntities.size() > 0 && !userEntities.get(0).getId().equals(userEntity.getId())) {
            throw new BaseRuntimeException("修改失败，用户名重复");
        }
        String password = userEntity.getPassword();
        if (password != null && !password.isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userEntity.setPassword(passwordEncoder.encode(password));
        }
        userDao.updateByPrimaryKeySelective(userEntity);
    }

    @Override
    public void remove(Long id) {
        userDao.deleteByPrimaryKey(id);
    }

    @Override
    public UserEntity findById(Long id) {
        UserEntity userEntity = userDao.selectByPrimaryKey(id);
        if (userEntity != null) {
            userEntity.setCustomerUnitsEntity(customerUnitsService.findById(userEntity.getCustomerUnitsId()));
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            if (userEntity.getIsCustomer().equals(EnumFlag.FlagTrue.getValue())) {
                userEntity.setCustomerUnitsEntity(customerUnitsService.findById(userEntity.getCustomerUnitsId()));
                userEntity.setCustomerDutyEntity(customerDutyService.findSimpleById(userEntity.getCustomerDutyId()));
                userEntity.setProvinceEntity(districtService.findById(userEntity.getProvinceId()));
                userEntity.setCityEntity(districtService.findById(userEntity.getCityId()));
                userEntity.setDistrictEntity(districtService.findById(userEntity.getDistrictId()));
            }
        }
        return userEntity;
    }

    @Override
    public List<UserEntity> findAll(Short isCustomer) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIsCustomerEqualTo(isCustomer);
        return userDao.selectByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findByExample(UserEntityExample userEntityExample) {
        return userDao.selectByExample(userEntityExample);
    }

    @Override
    public void addUserAndRoles(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, String loginUserName) {
        userEntity.setCreateBy(loginUserName);
        userEntity.setCreateTime(new Date());
        userEntity.setModifiedBy(loginUserName);
        userEntity.setModifiedTime(new Date());
        this.add(userEntity);
        this.addRolesByIdAndRoleIds(userEntity.getId(), roleIds);
        this.addDepartmentsByIdAndDepartmentIds(userEntity.getId(), departmentIds, loginUserName);
    }

    @Override
    public void editUserAndRoles(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, String loginUserName) {
        userEntity.setModifiedBy(loginUserName);
        userEntity.setModifiedTime(new Date());
        this.edit(userEntity);
        this.addRolesByIdAndRoleIds(userEntity.getId(), roleIds);
        this.addDepartmentsByIdAndDepartmentIds(userEntity.getId(), departmentIds, loginUserName);

    }

    @Override
    public List<UserEntity> findByTruename(String truename) {
        truename = truename == null ? "" : truename;
        UserEntityExample userEntityExample = new UserEntityExample();
        userEntityExample.createCriteria().andTrueNameLike("%" + truename + "%").andIsCustomerEqualTo((short) 0);
        return this.findByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findAllOrByUsername(String username, Short isCustermer) {
        List<UserEntity> userEntities;
        if (username == null || "".equals(username)) {
            userEntities = this.findAll(isCustermer);
        } else {
            UserEntityExample userEntityExample = new UserEntityExample();
            UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
            criteria.andIsCustomerEqualTo(isCustermer);
            criteria.andUsernameLike("%" + username + "%");
            userEntities = this.findByExample(userEntityExample);
        }
        for (UserEntity userEntity : userEntities) {
            userEntity.setCustomerUnitsEntity(customerUnitsService.findById(userEntity.getCustomerUnitsId()));
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            if (userEntity.getIsCustomer().equals(EnumFlag.FlagTrue.getValue())) {
                userEntity.setCustomerUnitsEntity(customerUnitsService.findById(userEntity.getCustomerUnitsId()));
                userEntity.setCustomerDutyEntity(customerDutyService.findSimpleById(userEntity.getCustomerDutyId()));
                userEntity.setProvinceEntity(districtService.findById(userEntity.getProvinceId()));
                userEntity.setCityEntity(districtService.findById(userEntity.getCityId()));
                userEntity.setDistrictEntity(districtService.findById(userEntity.getDistrictId()));
            }
        }
        return userEntities;
    }


    /**
     * 按照用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户未找到异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<UserEntity> userEntities = userDao.selectByExample(userEntityExample);
        if (userEntities.isEmpty()) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        UserEntity userEntity = userEntities.get(0);
        if (!userEntity.isEnabled()) {
            throw new DisabledException("账户已关闭");
        }
        if (!userEntity.isAccountNonLocked()) {
            throw new LockedException("账户被锁定");
        }
        List<RoleEntity> roleEntities = roleService.findByUserId(userEntity.getId());
        if (roleEntities.isEmpty() && userEntity.getAuthType().equals(EnumAuthType.USER.getValue())) {
            throw new UsernameNotFoundException("用户没有分配角色");
        }
        userEntity.setRoleEntities(roleEntities);
        return userEntity;
    }


    @Override
    public List<RoleEntity> findRolesById(Long id) {
        return roleService.findByUserId(id);
    }

    @Override
    public List<DepartmentEntity> findDepartmentsById(Long id) {
        DepartmentUserEntityExample departmentUserEntityExample = new DepartmentUserEntityExample();
        DepartmentUserEntityExample.Criteria criteria = departmentUserEntityExample.createCriteria();
        criteria.andUserIdEqualTo(id);
        List<DepartmentUserEntity> departmentUserEntities = departmentUserService.findByExample(departmentUserEntityExample);
        List<Long> departmentIds = departmentUserEntities.stream().map(DepartmentUserEntity::getDepartmentId).collect(Collectors.toList());
        return departmentService.findByIds(departmentIds);
    }

    @Override
    public void addRolesByIdAndRoleIds(Long id, Long[] roleIds) {
        //去重
        Set<Long> set = new HashSet<>(Arrays.asList(roleIds));

        UserEntity userEntity = this.findById(id);
        if (userEntity != null) {//判断当前用户是否存在
            //查询当前用户分配的角色
            UserRoleEntityExample userRoleEntityExample = new UserRoleEntityExample();
            UserRoleEntityExample.Criteria criteria = userRoleEntityExample.createCriteria();
            criteria.andUserIdEqualTo(id);
            List<UserRoleEntity> userRoleEntities = userRoleService.findByExample(userRoleEntityExample);

            //删除当前用户分配的角色
            if (userRoleEntities.size() > 0) {
                List<Long> longs = new ArrayList<>();
                for (UserRoleEntity userRoleEntity : userRoleEntities) {
                    longs.add(userRoleEntity.getRoleId());
                }
                criteria.andRoleIdIn(longs);
                userRoleService.removeByExample(userRoleEntityExample);
            }

            //添加当前用户新分配分配的角色
            for (Long roleId : set) {
                if (roleId != null) {
                    RoleEntity roleEntity = roleService.findById(roleId);
                    if (roleEntity != null) {
                        UserRoleEntity userRoleEntity = new UserRoleEntity();
                        userRoleEntity.setUserId(id);
                        userRoleEntity.setRoleId(roleId);
                        userRoleService.add(userRoleEntity);
                    }
                }
            }
        }
    }

    @Override
    public void addDepartmentsByIdAndDepartmentIds(Long id, Long[] departmentsIds, String loginUserName) {
        //去重
        Set<Long> set = new HashSet<>(Arrays.asList(departmentsIds));

        UserEntity userEntity = this.findById(id);
        if (userEntity != null) {//判断当前用户是否存在
            //查询当前用户添加的部门
            DepartmentUserEntityExample departmentUserEntityExample = new DepartmentUserEntityExample();
            DepartmentUserEntityExample.Criteria criteria = departmentUserEntityExample.createCriteria();
            criteria.andUserIdEqualTo(id);
            List<DepartmentUserEntity> departmentUserEntities = departmentUserService.findByExample(departmentUserEntityExample);

            //删除当前用户添加的部门
            if (departmentUserEntities.size() > 0) {
                List<Long> longs = new ArrayList<>();
                for (DepartmentUserEntity departmentUserEntity : departmentUserEntities) {
                    longs.add(departmentUserEntity.getDepartmentId());
                }
                criteria.andDepartmentIdIn(longs);
                departmentUserService.removeByExample(departmentUserEntityExample);
            }

            //添加当前用户新添加的部门
            for (Long departmentId : set) {
                if (departmentId != null) {
                    DepartmentEntity departmentEntity = departmentService.findById(departmentId);
                    if (departmentEntity != null) {
                        DepartmentUserEntity departmentUserEntity = new DepartmentUserEntity();
                        departmentUserEntity.setCreateBy(loginUserName);
                        departmentUserEntity.setCreateTime(new Date());
                        departmentUserEntity.setModifiedBy(loginUserName);
                        departmentUserEntity.setModifiedTime(new Date());
                        departmentUserEntity.setDepartmentId(departmentId);
                        departmentUserEntity.setUserId(id);
                        departmentUserService.add(departmentUserEntity);
                    }
                }
            }
        }
    }


    @Override
    public List<TreeNodeUtil> findPermissionById(Long id, Long parentId, Short isMenu) {
        List<PermissionEntity> permissionEntities = userDao.selectPermissionById(id, parentId, isMenu);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (PermissionEntity permissionEntity : permissionEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(permissionEntity.getId());
            treeNodeUtil.setLabel(permissionEntity.getName());
            treeNodeUtil.setIcon(permissionEntity.getIconCls());
            treeNodeUtil.setChildren(this.findPermissionById(id, permissionEntity.getId(), isMenu));
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
    public List<TreeNodeUtil> findPermission(Long parentId, Short isMenu) {
        List<PermissionEntity> permissionEntities = userDao.selectPermission(parentId, isMenu);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (PermissionEntity permissionEntity : permissionEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(permissionEntity.getId());
            treeNodeUtil.setLabel(permissionEntity.getName());
            treeNodeUtil.setIcon(permissionEntity.getIconCls());
            treeNodeUtil.setChildren(this.findPermission(permissionEntity.getId(), isMenu));
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
    public void editTruenameById(Long id, String truename) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setTrueName(truename);
        userDao.updateByPrimaryKeySelective(userEntity);
    }


}
