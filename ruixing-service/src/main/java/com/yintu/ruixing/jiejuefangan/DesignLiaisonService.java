package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/3 11:04
 */
public interface DesignLiaisonService extends BaseService<DesignLiaisonEntity, Integer> {

    List<DesignLiaisonEntity> findAll();

    /**
     * 添加
     *
     * @param entity   项目实体类
     * @param trueName 当前登录用户真实姓名
     */
    void add(DesignLiaisonEntity entity, String trueName);


    /**
     * 批量删除
     *
     * @param ids 项目id集
     */
    void remove(Integer[] ids);


    /**
     * 修改
     *
     * @param entity   项目实体类
     * @param trueName 当前登录用户真实姓名
     */
    void edit(DesignLiaisonEntity entity, String trueName);


    /**
     * 根据年份查询项目名称
     *
     * @param year 年份
     * @return 项目id  项目名称
     */
    List<DesignLiaisonEntity> findByYear(Integer year);


    /**
     * @param year        年份
     * @param projectName 项目名称
     * @return
     */
    List<DesignLiaisonEntity> findByExample(Integer year, String projectName);


    /**
     * 按照项目日期去重
     *
     * @return 年份集合
     */
    List<Integer> findByDistinctProjectDate();


    /**
     * 年份 项目名 文件类型三级树
     *
     * @return 树信息集合
     */
    List<TreeNodeUtil> findByTree();
}
