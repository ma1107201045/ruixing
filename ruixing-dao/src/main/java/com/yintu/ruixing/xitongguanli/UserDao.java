package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
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

    List<PermissionEntity> selectPermission(Long parentId, Short isMenu);

    List<PermissionEntity> selectPermissionById(Long id, Long parentId, Short isMenu);

    List<PermissionEntity> selectAuthority(String description, Short isMenu);

    List<PermissionEntity> selectAuthorityById(Long id, String description, Short isMenu);

    List<Long> findId(@Param("username") String username);

    Integer findid(@Param("truename") String truename);


}