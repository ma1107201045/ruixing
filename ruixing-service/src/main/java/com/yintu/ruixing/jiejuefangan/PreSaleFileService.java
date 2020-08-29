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
     */
    void add(PreSaleFileEntity preSaleFileEntity, Integer[] auditorIds);

    /**
     * @param preSaleFileEntity 售前技术支持文件实体类
     * @param auditorIds        审核人id
     */
    void edit(PreSaleFileEntity preSaleFileEntity, Integer[] auditorIds);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);

    /**
     * @param id
     * @return
     */
    PreSaleFileEntity findPreSaleById(Integer id);

    /**
     * @param projectId 项目名称
     * @param type      文件类型
     * @return
     */
    List<PreSaleFileEntity> findProjectIdAndType(Integer projectId, String type);

    /**
     * @param outputStream 输出流
     * @param ids          id 集合
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;

    /**
     * 审核人列表信息
     *
     * @return 审核人列表
     */
    List<UserEntity> findUserEntitiesByTruename(String trueName);
}
