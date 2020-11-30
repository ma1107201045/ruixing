package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BaoJingYuJingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaoJingYuJingEntity record);

    int insertSelective(BaoJingYuJingEntity record);

    BaoJingYuJingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaoJingYuJingEntity record);

    int updateByPrimaryKey(BaoJingYuJingEntity record);






    List<BaoJingYuJingEntity> findAllYuJingBaoJing();

    List<BaoJingYuJingEntity> findYuJingBaoJingBySouSuo(@Param("ids") Integer[] ids,@Param("sid") Integer sid,@Param("qid") Integer qid,
                                                        @Param("startTime") Date startTime,@Param("huifuTime") Date huifuTime,@Param("tianChuang") Integer tianChuang,
                                                        @Param("lvChuHuiFu") Integer lvChuHuiFu,@Param("lvChuKaiTong") Integer lvChuKaiTong);
}