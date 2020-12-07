package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DepartmentUserService departmentUserService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private DataStatsService dataStatsService;


    @Override
    public void add(UserEntity userEntity) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
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
        criteria.andUsernameEqualTo(userEntity.getUsername());
        List<UserEntity> userEntities = this.findByExample(userEntityExample);
        if (userEntities.size() > 0 && !userEntities.get(0).getId().equals(userEntity.getId())) {
            throw new BaseRuntimeException("修改失败，用户名重复");
        }
        userEntity.setPassword(null);
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
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            userEntity.setTieLuJuEntities(this.findTieLuJusById(userEntity.getId()));
            userEntity.setDianWuDuanEntities(this.findDianWuDuansById(userEntity.getId()));
            userEntity.setXianDuanEntities(this.findXianDuansById(userEntity.getId()));
            userEntity.setCheZhanEntities(this.findCheZhansById(userEntity.getId()));
        }
        return userEntity;
    }

    @Override
    public void removeByIds(Long[] ids) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        userDao.deleteByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findByIds(List<Long> ids) {
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIdIn(ids);
        return userDao.selectByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findAll() {
        UserEntityExample userEntityExample = new UserEntityExample();
        return userDao.selectByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findByExample(UserEntityExample userEntityExample) {
        return userDao.selectByExample(userEntityExample);
    }

    @Override
    public void add(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, Long[] tids, Long[] dids, Long[] xids, Long[] cids) {
        this.add(userEntity);
        this.addRolesByIdAndRoleIds(userEntity.getId(), roleIds, userEntity.getModifiedBy());
        this.addDepartmentsByIdAndDepartmentIds(userEntity.getId(), departmentIds, userEntity.getModifiedBy());
        this.addDataByIdAndDataIds(userEntity.getId(), tids, dids, xids, cids, userEntity.getModifiedBy());
    }

    @Override
    public void edit(UserEntity userEntity, Long[] roleIds, Long[] departmentIds, Long[] tids, Long[] dids, Long[] xids, Long[] cids) {
        this.edit(userEntity);
        this.addRolesByIdAndRoleIds(userEntity.getId(), roleIds, userEntity.getModifiedBy());
        this.addDepartmentsByIdAndDepartmentIds(userEntity.getId(), departmentIds, userEntity.getModifiedBy());
        this.addDataByIdAndDataIds(userEntity.getId(), tids, dids, xids, cids, userEntity.getModifiedBy());
    }

    @Override
    public void editPassword(Long id, String oldPassword, String newPassword, String loginUserName) {
        if (oldPassword != null && !oldPassword.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
            UserEntity userEntity = userDao.selectByPrimaryKey(id);
            if (userEntity == null)
                throw new BaseRuntimeException("当前用户不存在");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(oldPassword, userEntity.getPassword()))
                throw new BaseRuntimeException("旧密码错误");
            userEntity.setModifiedBy(loginUserName);
            userEntity.setModifiedTime(new Date());
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userDao.updateByPrimaryKeySelective(userEntity);
        }
    }

    @Override
    public List<UserEntity> findByTruename(String truename) {
        truename = truename == null ? "" : truename;
        UserEntityExample userEntityExample = new UserEntityExample();
        userEntityExample.createCriteria().andTrueNameLike("%" + truename + "%");
        return this.findByExample(userEntityExample);
    }

    @Override
    public List<UserEntity> findAllOrByUsername(String username) {
        List<UserEntity> userEntities;
        if (username == null || "".equals(username)) {
            userEntities = this.findAll();
        } else {
            UserEntityExample userEntityExample = new UserEntityExample();
            UserEntityExample.Criteria criteria = userEntityExample.createCriteria();

            criteria.andUsernameLike("%" + username + "%");
            userEntities = this.findByExample(userEntityExample);
        }
        for (UserEntity userEntity : userEntities) {
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            userEntity.setTieLuJuEntities(this.findTieLuJusById(userEntity.getId()));
            userEntity.setDianWuDuanEntities(this.findDianWuDuansById(userEntity.getId()));
            userEntity.setXianDuanEntities(this.findXianDuansById(userEntity.getId()));
            userEntity.setCheZhanEntities(this.findCheZhansById(userEntity.getId()));
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
        List<RoleEntity> roleEntities = roleService.findByUserId(userEntity.getId());
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
    public List<TreeNodeUtil> findDepartmentsTreeById(Long id, Long parentId) {
        List<DepartmentEntity> departmentEntities = departmentService.findByUserId(id, parentId);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (DepartmentEntity departmentEntity : departmentEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(departmentEntity.getId());
            treeNodeUtil.setLabel(departmentEntity.getName());
            treeNodeUtil.setChildren(this.findDepartmentsTreeById(id, departmentEntity.getId()));
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", departmentEntity.getParentId());
            treeNodeUtil.setA_attr(map);
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

    @Override
    public void findDepartmentsTreeById(Long id, Long parentId, List<TreeNodeUtil> treeNodeUtils) {
        List<DepartmentEntity> departmentEntities = departmentService.findByUserId(id, parentId);
        for (DepartmentEntity departmentEntity : departmentEntities) {
            List<DepartmentEntity> department = departmentService.findByUserId(id, departmentEntity.getId());
            if (department.size() > 0) {
                findDepartmentsTreeById(id, departmentEntity.getId(), treeNodeUtils);
            } else {
                TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
                treeNodeUtil.setId(departmentEntity.getId());
                treeNodeUtil.setLabel(departmentEntity.getName());
                treeNodeUtil.setChildren(this.findDepartmentsTreeById(id, departmentEntity.getId()));
                Map<String, Object> map = new HashMap<>();
                map.put("parentId", departmentEntity.getParentId());
                treeNodeUtil.setA_attr(map);
                treeNodeUtils.add(treeNodeUtil);
            }
        }
    }


    @Override
    public List<TieLuJuEntity> findTieLuJusById(Long id) {
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(id);
        List<TieLuJuEntity> tieLuJuEntities = new ArrayList<>();
        for (UserDataEntity userDataEntity : userDataEntities) {
            tieLuJuEntities.add(dataStatsService.findByTid(userDataEntity.getTId()));
        }
        return tieLuJuEntities;
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuansById(Long id) {
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(id);
        List<DianWuDuanEntity> dianWuDuanEntities = new ArrayList<>();
        for (UserDataEntity userDataEntity : userDataEntities) {
            dianWuDuanEntities.add(dataStatsService.findByDid(userDataEntity.getDId()));
        }
        return dianWuDuanEntities;
    }

    @Override
    public List<XianDuanEntity> findXianDuansById(Long id) {
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(id);
        List<XianDuanEntity> xianDuanEntities = new ArrayList<>();
        for (UserDataEntity userDataEntity : userDataEntities) {
            xianDuanEntities.add(dataStatsService.findByXid(userDataEntity.getXId()));
        }
        return xianDuanEntities;
    }

    @Override
    public List<CheZhanEntity> findCheZhansById(Long id) {
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(id);
        List<CheZhanEntity> cheZhanEntities = new ArrayList<>();
        for (UserDataEntity userDataEntity : userDataEntities) {
            cheZhanEntities.add(dataStatsService.findByCid(userDataEntity.getCId()));
        }
        return cheZhanEntities;
    }

    @Override
    public void addRolesByIdAndRoleIds(Long id, Long[] roleIds, String loginUserName) {
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
                        userRoleEntity.setCreateBy(loginUserName);
                        userRoleEntity.setCreateTime(new Date());
                        userRoleEntity.setModifiedBy(loginUserName);
                        userRoleEntity.setModifiedTime(new Date());
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
    public void addDataByIdAndDataIds(Long id, Long[] tids, Long[] dids, Long[] xids, Long[] cids, String loginUserName) {
        UserEntity userEntity = this.findById(id);
        if (userEntity != null) {//判断当前用户是否存在
            List<UserDataEntity> userDataEntities = userDataService.findByUserId(id);
            if (userDataEntities.size() > 0) {
                userDataService.removeByUserId(id);
            }
            if (tids != null && dids != null && xids != null && cids != null) {
                for (int i = 0; i < tids.length; i++) {
                    Long tid = tids[i];
                    Long did = i + 1 > dids.length ? null : dids[i];
                    Long xid = i + 1 > xids.length ? null : xids[i];
                    Long cid = i + 1 > cids.length ? null : cids[i];
                    UserDataEntity userDataEntity = new UserDataEntity();
                    userDataEntity.setCreateBy(loginUserName);
                    userDataEntity.setCreateTime(new Date());
                    userDataEntity.setModifiedBy(loginUserName);
                    userDataEntity.setModifiedTime(new Date());
                    userDataEntity.setUserId(id);
                    userDataEntity.setTId(tid);
                    userDataEntity.setDId(did);
                    userDataEntity.setXId(xid);
                    userDataEntity.setCId(cid);
                    userDataService.add(userDataEntity);
                }
            }
        }
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
    public List<PermissionEntity> findAuthority(String description, Short isMenu) {
        return userDao.selectAuthority(description, isMenu);
    }

    @Override
    public List<PermissionEntity> findAuthorityById(Long id, String description, Short isMenu) {
        return userDao.selectAuthorityById(id, description, isMenu);
    }

    @Override
    public void editTruenameById(Long id, String truename) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setTrueName(truename);
        userDao.updateByPrimaryKeySelective(userEntity);
    }

    public long findUserSum() {
        return userDao.countByExample(new UserEntityExample());
    }


}
