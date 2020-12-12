package com.yintu.ruixing.xitongguanli.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.master.xitongguanli.DatabaseOperatingRecordDao;
import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordEntity;
import com.yintu.ruixing.xitongguanli.DatabaseOperatingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/31 16:17
 */
@Slf4j
@Service
@Transactional
public class DatabaseOperatingRecordImpl implements DatabaseOperatingRecordService {
    @Autowired
    private DatabaseOperatingRecordDao databaseOperatingRecordDao;

    @Value("${spring.datasource.druid.master.url}")
    private String jdbcURL;
    @Value("${spring.datasource.druid.master.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.druid.master.password}")
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
    public List<DatabaseOperatingRecordEntity> findAll() {
        return databaseOperatingRecordDao.selectAll();
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
        List<String> tableNames = this.findLikeTableNames(databaseName, "data%", "tb_database_operating_record");
        StringBuilder sb = new StringBuilder();
        for (String tableName : tableNames) {
            sb.append("--ignore-table").append(" ").append(databaseName).append(".").append(tableName).append(" ");
        }
        String basePath = SystemUtil.getOsInfo().isWindows() ? backupPathOfWindowPrefix : backupPathOfUnixPrefix;
        String uuidStr = UUID.fastUUID().toString();
        File file = new File(basePath + uuidStr);
        if (!file.exists())
            if (!file.mkdirs())
                throw new BaseRuntimeException("创建文件有误");
        String backupName = uuidStr + File.separator + databaseName + "-" + DateUtil.format(new Date(), "yyyy-MM-dd") + ".sql";
        String backupPath = basePath + backupName;//物理路径
        String requestPath = prefix + backupName;//请求路径
        String mysqlFormatCommand = "mysqldump --single-transaction -h %s -u %s -p%s --databases %s %s > %s";
        String mysqlCommand = String.format(mysqlFormatCommand, host, jdbcUsername, jdbcPassword, databaseName, sb.toString(), backupPath);
        String result = this.execCommand(mysqlCommand);

        System.out.println(mysqlCommand);
        System.out.println(result);
        if (StrUtil.containsIgnoreCase(result, "[ERROR]"))
            throw new BaseRuntimeException("备份失败");
        DatabaseOperatingRecordEntity databaseOperatingRecordEntity = new DatabaseOperatingRecordEntity();
        databaseOperatingRecordEntity.setCreateBy(loginUserName);
        databaseOperatingRecordEntity.setCreateTime(new Date());
        databaseOperatingRecordEntity.setBackupFilePath(requestPath);
        databaseOperatingRecordEntity.setBackupFileName(backupName);
        this.add(databaseOperatingRecordEntity);

    }

    @Override
    public void restore(Long id, String loginUserName) {
        DatabaseOperatingRecordEntity databaseOperatingRecordEntity = this.findById(id);
        if (databaseOperatingRecordEntity != null) {
            String strArr = StrUtil.subBetween(jdbcURL, "/", "?");
            String[] arr = strArr.split("/");
            String host = arr[1];
            String databaseName = arr[2];
            String basePath = SystemUtil.getOsInfo().isWindows() ? backupPathOfWindowPrefix : backupPathOfUnixPrefix;
            String backupPath = basePath + databaseOperatingRecordEntity.getBackupFileName();//物理路径
            if (FileUtil.exist(backupPath)) {
                String mysqlFormatCommand = "mysql -h %s -u %s -p%s %s < %s";
                String mysqlCommand = String.format(mysqlFormatCommand, host, jdbcUsername, jdbcPassword, databaseName, backupPath);
                String result = this.execCommand(mysqlCommand);
                System.out.println(mysqlCommand);
                System.out.println(result);
                if (StrUtil.containsIgnoreCase(result, "[ERROR]"))
                    throw new BaseRuntimeException("还原失败");
            }
            databaseOperatingRecordEntity.setModifiedBy(loginUserName);
            databaseOperatingRecordEntity.setModifiedTime(new Date());
            this.edit(databaseOperatingRecordEntity);
        }
    }

    public String execCommand(String cmd) {
        return RuntimeUtil.getResult(SystemUtil.getOsInfo().isWindows() ? RuntimeUtil.exec("cmd", "/c", cmd) : RuntimeUtil.exec("sh", "-c", cmd), Charset.defaultCharset());
    }
}
