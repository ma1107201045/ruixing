package com.yintu.ruixing.xitongguanli.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.DistrictService;
import com.yintu.ruixing.common.enumobject.EnumAuthType;
import com.yintu.ruixing.common.enumobject.EnumFlag;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.CustomerDutyService;
import com.yintu.ruixing.danganguanli.CustomerUnitsService;
import com.yintu.ruixing.xitongguanli.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
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
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            if (userEntity.getIsCustomer().equals(EnumFlag.FlagTrue.getValue())) {
                userEntity.setProvinceEntity(districtService.findById(userEntity.getProvinceId()));
                userEntity.setCityEntity(districtService.findById(userEntity.getCityId()));
                userEntity.setDistrictEntity(districtService.findById(userEntity.getDistrictId()));
            }
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
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setRoleEntities(this.findRolesById(userEntity.getId()));
            if (userEntity.getIsCustomer().equals(EnumFlag.FlagTrue.getValue())) {
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

    @Override
    public void exportFile(OutputStream outputStream, Long[] ids) throws IOException {
        //excel标题
        String title = "顾客档案列表信息";
        //excel表名
        String[] headers = {"序号", "部门", "姓名", "手机", "座机", "邮箱", "省份", "城市", "县区", "邮寄地址", "创建人", "创建时间", "更新人", "更新时间"};
        //获取数据
        UserEntityExample userEntityExample = new UserEntityExample();
        UserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andIsCustomerEqualTo(EnumFlag.FlagTrue.getValue());
        criteria.andIdIn(Arrays.asList(ids));
        List<UserEntity> userEntities = this.findByExample(userEntityExample).stream().
                sorted(Comparator.comparing(UserEntity::getId).reversed())
                .collect(Collectors.toList());
        for (UserEntity userEntity : userEntities) {
            userEntity.setDepartmentEntities(this.findDepartmentsById(userEntity.getId()));
            userEntity.setProvinceEntity(districtService.findById(userEntity.getProvinceId()));
            userEntity.setCityEntity(districtService.findById(userEntity.getCityId()));
            userEntity.setDistrictEntity(districtService.findById(userEntity.getDistrictId()));
        }

        //excel元素
        String[][] content = new String[userEntities.size()][headers.length];
        for (int i = 0; i < userEntities.size(); i++) {
            content[i][0] = userEntities.get(i).getId().toString();
            content[i][1] = userEntities.get(i).getCustomerUnitsEntity().getName();
            List<DepartmentEntity> departmentEntities = userEntities.get(i).getDepartmentEntities();
            StringBuilder sb = new StringBuilder();
            for (DepartmentEntity departmentEntity : departmentEntities) {
                sb.append(departmentEntity.getName()).append("\r\n");
            }
            content[i][2] = sb.toString();
            content[i][3] = userEntities.get(i).getCustomerDutyEntity().getName();
            content[i][4] = userEntities.get(i).getTrueName();
            content[i][5] = userEntities.get(i).getPhone();
            content[i][6] = userEntities.get(i).getTelephone();
            content[i][7] = userEntities.get(i).getEmail();
            content[i][8] = userEntities.get(i).getProvinceEntity().getName();
            content[i][9] = userEntities.get(i).getCityEntity().getName();
            content[i][10] = userEntities.get(i).getDistrictEntity().getName();
            content[i][11] = userEntities.get(i).getAddress() + content[i][8] + content[i][9] + content[i][10];
            content[i][12] = userEntities.get(i).getCreateBy();
            content[i][13] = DateUtil.format(userEntities.get(i).getCreateTime(), "yyy-MM-dd hh:ss:mm");
            content[i][14] = userEntities.get(i).getModifiedBy();
            content[i][15] = DateUtil.format(userEntities.get(i).getModifiedTime(), "yyy-MM-dd hh:ss:mm");
        }
        //创建XSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
