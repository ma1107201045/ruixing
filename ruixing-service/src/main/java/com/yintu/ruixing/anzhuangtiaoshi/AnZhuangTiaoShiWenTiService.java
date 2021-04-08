package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksFileEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntityExample;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/9 10:30
 * @Version 1.0
 * 需求:安装调试 问题跟踪
 */
public interface AnZhuangTiaoShiWenTiService {
    void addWenTi(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity, Integer[] uids, String username, Integer senderid);

    void editWenTiById(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity, Integer id, Integer senderid, String username);

    List<AnZhuangTiaoShiWenTiEntity> findSomeWenTi(Integer page, Integer size, String xdname, Integer receiverid,String startTime, String endTime, String wenTiType,
                                                   String fankuiMode, String shouliDanwei, Integer isNotOver);

    void deleteWenTiByIds(Integer[] ids);

    List<AnZhuangTiaoShiWenTiFileEntity> findAllFanKuiFileById(Integer wid, Integer page, Integer size, String fileName);

    List<AnZhuangTiaoShiWenTiFileEntity> findAllShuChuFileById(Integer wid, Integer page, Integer size, String fileName);

    void addFanKuiFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids, String username, Integer senderid);

    void addShuRuFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids, String username, Integer senderid);

    AnZhuangTiaoShiWenTiFileEntity findById(Integer id);

    void deleteFileByIds(Integer[] ids);

    List<DepartmentEntity> findAllDepartment(DepartmentEntityExample departmentEntityExample);

    List<AnZhuangTiaoShiWorksFileEntity> findFileById(Integer id);

    List<AnZhuangTiaoShiWenTiEntity> findAllNotDoWellWenTi(Integer page, Integer size);

    List<AnZhuangTiaoShiRecordMessageEntity> findRecordMessageById(Integer id);

    void editAuditorByWTId(Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, String username, Integer receiverid, Integer senderId);

    AnZhuangTiaoShiWenTiEntity findWenTiById(Integer id, Integer receiverid);

    void editAuditorByWJId(Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, String username, Integer receiverid, Integer senderId);

    AnZhuangTiaoShiWenTiFileEntity findWenTiFileById(Integer id);

    List<AnZhuangTiaoShiRecordMessageEntity> findFileRecordMessageById(Integer id);

    AnZhuangTiaoShiWenTiEntity findWenTiXiangQingById(Integer id);
}
