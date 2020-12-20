package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/30 18:58
 */
public interface PreSaleFileService extends BaseService<PreSaleFileEntity, Integer> {

    /**
     * @param preSaleFileEntity 售前技术支持文件实体类
     * @param auditorIds        审核人id
     * @param trueName          当前登录用户真实姓名
     */
    void add(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, Integer[] sorts, String trueName);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);

    /**
     * @param preSaleFileEntity 售前技术支持文件实体类
     * @param auditorIds        审核人id
     * @param trueName          当前登录用户真实姓名
     */
    void edit(PreSaleFileEntity preSaleFileEntity, Long[] auditorIds, Integer[] sorts, String trueName);


    /**
     * @param id
     * @return
     */
    PreSaleFileEntity findPreSaleById(Integer id);


    /**
     * @param preSaleId 项目id
     * @param name      文件名称
     * @param type      文件类型
     * @param userId    当前登录人id
     * @return
     */
    List<PreSaleFileEntity> findByPreSaleIdAndNameAndType(Integer preSaleId, String name, String type, Integer userId);

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
     * @param id            文件id
     * @param isPass        是否 审核状态 1.待审核 2.已审核未通过 3.已审核已通过
     * @param context       审核内容
     * @param accessoryName 附件名称
     * @param accessoryPath 附件路径
     * @param passUserId    转交人id
     * @param loginUserId   登录id
     * @param userName      用户名
     * @param trueName      真实姓名
     */
    void audit(Integer id, Short isPass, String context, String accessoryName, String accessoryPath, Integer passUserId, Integer loginUserId, String userName, String trueName);

    /**
     * 查询转交人列表（排除当前审核文件人列表以及当前登录人）
     *
     * @param id          文件id
     * @param loginUserId 当前登录用户id
     * @return 审核人列表
     */
    List<UserEntity> findByOtherAuditors(Integer id, Long loginUserId);


}
