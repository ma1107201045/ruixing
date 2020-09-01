package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.jiejuefangan.BiddingFileEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/2 14:26
 */
public interface BiddingFileService extends BaseService<BiddingFileEntity, Integer> {
    /**
     * @param biddingFileEntity 投招标文件实体类
     * @param auditorIds        审核人ids
     * @param trueName          当前登录用户真实姓名
     */
    void add(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName);

    /**
     * @param biddingFileEntity 投招标文件实体类
     * @param auditorIds        审核人id
     * @param trueName          当前登录用户真实姓名
     */
    void edit(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void remove(Integer[] ids);


    BiddingFileEntity findBiddingById(Integer id);

    /**
     * @param biddingId 项目id
     * @param name      文件名
     * @param type      文件类型
     * @param userId    当前登录人id
     * @return
     */
    List<BiddingFileEntity> findByBiddingIdAndNameAndType(Integer biddingId, String name, String type, Integer userId);

    /**
     * @param outputStream 输出流
     * @param ids          id 集合
     * @param userId       当前登录人id
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException;


}
