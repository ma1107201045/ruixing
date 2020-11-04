package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/31 16:17
 */
public interface DatabaseOperatingRecordService extends BaseService<DatabaseOperatingRecordEntity, Long> {

    List<DatabaseOperatingRecordEntity> findAll();

    List<String> findLikeTableNames(String databaseName, String... tableName);

    void backup(HttpServletRequest request, String loginUserName);

    void restore(Long id, String loginUserName);


}
