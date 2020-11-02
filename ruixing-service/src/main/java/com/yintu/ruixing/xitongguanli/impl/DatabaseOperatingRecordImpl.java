package com.yintu.ruixing.xitongguanli.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordDao;
import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordEntity;
import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/31 16:17
 */
@Service
@Transactional
public class DatabaseOperatingRecordImpl implements DatabaseOperatingRecordService {
    @Autowired
    private DatabaseOperatingRecordDao databaseOperatingRecordDao;

    @Value("${spring.datasource.druid.url}")
    private String jdbcURL;
    @Value("${spring.datasource.druid.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.druid.password}")
    private String jdbcPassword;

    private static final String backupPathOfWindowPrefix = "C:\\data\\ruixing\\backups\\";

    private static final String backupPathOfUnixPrefix = "/data/ruixing/backups/";

    @Override
    public void add(DatabaseOperatingRecordEntity entity) {
        databaseOperatingRecordDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        databaseOperatingRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(DatabaseOperatingRecordEntity entity) {
        databaseOperatingRecordDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public DatabaseOperatingRecordEntity findById(Long id) {
        return databaseOperatingRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public List<String> findLikeTableNames(String databaseName, String... tableName) {
        return databaseOperatingRecordDao.selectLikeTableNames(databaseName, tableName);
    }

    @Override
    public void backup(HttpServletRequest request, String loginUserName) {
        String prefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/backups" + "/";
        String strArr = StrUtil.subBetween(jdbcURL, "/", "?");
        String[] arr = strArr.split("/");
        String host = arr[1];
        String databaseName = arr[2];
        List<String> tableNames = this.findLikeTableNames(databaseName, "alarm%", "data%", "tb_database_operating_record");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tableNames.size(); i++) {
            if (i == tableNames.size() - 1) {
                sb.append("--ignore-table").append(" ").append(databaseName).append(".").append(tableNames.get(i));
            } else {
                sb.append("--ignore-table").append(" ").append(databaseName).append(".").append(tableNames.get(i)).append(" ");
            }
        }
        String backupPath = SystemUtil.getOsInfo().isWindows() ? backupPathOfWindowPrefix : backupPathOfUnixPrefix;
        String uuidStr = UUID.fastUUID().toString();
        File file = new File(backupPath + uuidStr);
        if (!file.exists())
            if (!file.mkdirs())
                throw new BaseRuntimeException("创建文件有误");
        String backupName = uuidStr + File.separator + databaseName + ".sql";
        String backupPathName = backupPath + backupName;
        String requestPath = prefix + backupName;
        String cmdFormat = "CMD /C mysqldump --single-transaction --column-statistics=0 -h %s -u %s -p%s --databases %s %s>%s";
        String cmd = String.format(cmdFormat, host, jdbcUsername, jdbcPassword, databaseName, sb.toString(), backupPathName);
        System.out.println(cmd);
        String result = RuntimeUtil.getResult(RuntimeUtil.exec(cmd), Charset.defaultCharset());
        System.out.println(result);
        DatabaseOperatingRecordEntity databaseOperatingRecordEntity = new DatabaseOperatingRecordEntity();
        databaseOperatingRecordEntity.setCreateBy(loginUserName);
        databaseOperatingRecordEntity.setCreateTime(new Date());
        databaseOperatingRecordEntity.setModifiedBy(loginUserName);
        databaseOperatingRecordEntity.setModifiedTime(new Date());
        databaseOperatingRecordEntity.setBackupFilePath(requestPath);
        databaseOperatingRecordEntity.setBackupFileName(backupName);
        this.add(databaseOperatingRecordEntity);

    }
}
