package com.yintu.ruixing.master.xitongguanli;

import com.yintu.ruixing.xitongguanli.PermissionEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserEntityExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    long countByExample(UserEntityExample example);

    int deleteByExample(UserEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserEntity record);

    int insertSelective(UserEntity record);

    List<UserEntity> selectByExample(UserEntityExample example);

    UserEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserEntity record, @Param("example") UserEntityExample example);

    int updateByExample(@Param("record") UserEntity record, @Param("example") UserEntityExample example);

    int updateByPrimaryKeySelective(UserEntity record);

    int updateByPrimaryKey(UserEntity record);

    List<PermissionEntity> selectPermission(@Param("parentId") Long parentId, @Param("isMenu") Short isMenu);

    List<PermissionEntity> selectPermissionById(Long id, Long parentId, Short isMenu);

    List<PermissionEntity> selectAuthority(String description, Short isMenu);

    List<PermissionEntity> selectAuthorityById(Long id, String description, Short isMenu);

    List<Long> findId(@Param("username") String username);

    Integer findid(@Param("truename") String truename);


}