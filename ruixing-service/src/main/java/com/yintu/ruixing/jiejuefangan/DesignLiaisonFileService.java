package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/3 11:08
 */
public interface DesignLiaisonFileService extends BaseService<DesignLiaisonFileEntity, Integer> {

    /**
     * @param designLiaisonFileEntity 设计联络文件实体类
     * @param auditorIds              审核人ids
     * @param trueName                当前登录用户真实姓名
     */
    void add(DesignLiaisonFileEntity designLiaisonFileEntity, Long[] auditorIds, String trueName);

    /**
     * @param designLiaisonFileEntity 设计联络文件实体类
     * @param auditorIds              审核人ids
     * @param trueName                当前登录用户真实姓名
     */
    void edit(DesignLiaisonFileEntity designLiaisonFileEntity, Long[] auditorIds, String trueName);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);

    /**
     * @param id 设计联络id
     * @return 设计联络文件
     */
    DesignLiaisonFileEntity findDesignLiaisonById(Integer id);

    /**
     * @param designLiaisonId 项目id
     * @param name            项目名称
     * @param type            文件类型
     * @param userId          当前登录人id
     * @return
     */
    List<DesignLiaisonFileEntity> findByDesignLiaisonIdIdAndNameAndType(Integer designLiaisonId, String name, String type, Integer userId);

    /**
     * @param outputStream 输出流
     * @param ids          id 集合
     * @param userId       当前登录人id
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException;

    /**
     * 审核文件
     *
     * @param id          文件id
     * @param isPass      是否 审核状态 1.待审核 2.已审核未通过 3.已审核已通过
     * @param reason      已审核未通过理由
     * @param loginUserId 登录id
     * @param userName    用户名
     * @param trueName    真实姓名
     */

    void audit(Integer id, Short isPass, String reason, Integer loginUserId, String userName, String trueName);

}
