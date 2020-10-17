package com.yintu.ruixing.guzhangzhenduan;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 线段相关Dao
 */
@Mapper
public interface XianDuanDao {
    void addXianDuan(XianDuanEntity xianDuanEntity);

    void delXianDuan(Long xid);

    void editXianDuan(XianDuanEntity xianDuanEntity);

    XianDuanEntity findXianDuanById(Long xid);

    List<Integer> findId(Long xid);

    List<XianDuanEntity> findAllXianDuan();

    Long findid(@Param("parseLong") long parseLong, @Param("parseLong1") long parseLong1);

    List<XianDuanEntity> findAllXianDuanByName(String xdname);

	List<XianDuanEntity> findAllXianDuanByDwdid(@Param("parseLong") long parseLong, @Param("parseLong1") long parseLong1);

    String findXDJsonByXid(Integer xid);

    List<XianDuanEntity> findAllJsonByDid(Integer did);

    List<String> findXDJsonByDid(Integer did);
}
