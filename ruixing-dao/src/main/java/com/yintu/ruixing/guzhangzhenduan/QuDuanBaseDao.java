package com.yintu.ruixing.guzhangzhenduan;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuDuanBaseDao {
    //int deleteByPrimaryKey(Integer id);

    int insert(QuDuanBaseEntity record);

    //int insertSelective(QuDuanBaseEntity record);

    QuDuanBaseEntity selectByPrimaryKey(Integer id);

    List<QuDuanBaseEntity> selectByCzId(Integer czId);


    //int updateByPrimaryKeySelective(QuDuanBaseEntity record);

    int updateByPrimaryKey(QuDuanBaseEntity record);


    List<QuDuanBaseEntity> findAllQuDuan();

    List<QuDuanBaseEntity> findAllDianMaHua();

    List<QuDuanBaseEntity> findAllQuDuanByCid(Integer cid);

    List<QuDuanBaseEntity> findAllDianMaHuaByCid(Integer cid);

    void insertSelective(QuDuanBaseEntity quDuanBaseEntity);

    void updateByPrimaryKeySelective(QuDuanBaseEntity quDuanBaseEntity);

    void deleteByPrimaryKey(Integer id);

    void deletQuDuanByIds(Integer[] ids);

    Integer lastParentid();

    Integer findParentid(Integer id);

    Integer findId(Integer id);

    void updateParentid(@Param("id1") Integer id1, @Param("parentid") Integer parentid);

    List<QuDuanBaseEntity> findAllQuDuanName();

    List<QuDuanBaseEntity> findQuDuanByCid(Long cid);

    List<QuDuanBaseEntity> findQuDuanByQuDuanYunYingName(String qudunyunyingname);

    Integer findQDid(String quduanname);

    List<QuDuanBaseEntity> findQuDuanByIds(@Param("parseInt") Integer parseInt, @Param("parseInt1") Integer parseInt1);

    List<QuDuanBaseEntity> findQuDuanByTid(Integer tid);

    List<QuDuanBaseEntity> findQuDuanByDid(Integer did);

    List<QuDuanBaseEntity> findQuDuanByXid(Integer xid);

    List<QuDuanBaseEntity> findQuDuanBycid(Integer cid);

    List<QuDuanBaseEntity> findDianMaHuaByTid(Integer tid);

    List<QuDuanBaseEntity> findDianMaHuaByDid(Integer did);

    List<QuDuanBaseEntity> findDianMaHuaByXid(Integer xid);

    List<QuDuanBaseEntity> findDianMaHuaBycid(Integer cid);

    List<QuDuanBaseEntity> findDianMaHuaByCid(Integer cid);

    QuDuanBaseEntity selectByCzIdAndQuduanyunyingName(Integer czId, String quDuanYunYingName);

    String findQuduanName(@Param("stationId") Integer stationId, @Param("sectionId") Integer sectionId);

    List<QuDuanBaseEntity> findQuDuanDatasByCid(Integer cid);
}