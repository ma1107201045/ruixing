package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/3 20:40
 * @Version 1.0
 * 需求:产品交付的问题反馈
 */
public interface ChanPinJiaoFuWenTiService {

    List<ChanPinJiaoFuWenTiEntity> findAllData(Integer page, Integer size,String xiangMuNumber,Integer wenTiState);

    List<DepartmentEntity> findAllDepartment();

    void addWenTi(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity);

    void editWenTiById(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity);

    List<ChanPinJiaoFuWenTiEntity> findWenTiByName(String xiangMuName, Integer page, Integer size);

    void deletWenTiByIds(Integer[] ids);

    List<ChanPinJiaoFuWenTiEntity> findXiangMuMing();

    List<ChanPinJiaoFuWenTiEntity> downLoadByIds(Integer[] ids);

    void exportFile(ServletOutputStream outputStream, Integer[] ids)throws IOException;

    void addWenTiFile(ChanPinJiaoFuWenTiFileEntity chanPinJiaoFuWenTiFileEntity,String username);

    List<ChanPinJiaoFuWenTiFileEntity> findWenTiFileByType(Integer wtid,Integer fileType);

    void deleteWenTiFileByid(Integer id);

    List<ChanPinJiaoFuWenTiEntity> findAllNotOverWenTi();

}
