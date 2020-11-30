package com.yintu.ruixing.configiration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: mlf
 * @Date: 2020/11/30 13:33
 * @Version: 1.0
 */
@Configuration
@MapperScans({
        @MapperScan(basePackages = "com.yintu.ruixing.master", sqlSessionTemplateRef = "masterSqlSessionTemplate"),
        @MapperScan(basePackages = "com.yintu.ruixing.slave", sqlSessionTemplateRef = "slaveSqlSessionTemplate")
})
@Slf4j
public class DataSourceConfiguration {


    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.master")
    @Primary
    public DataSource masterDataSource() {
        log.info("MasterDataSource init..................................................");
        return DruidDataSourceBuilder.create().build();
    }


    @Bean("masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }


    @Bean("masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setTypeAliasesPackage("com.yintu.ruixing");
        //如果不使用xml的方式配置mapper，则可以省去下面这行mapper location的配置。
        //        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        //                .getResources("classpath:mapper/mysql/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean("masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory masterSqlSessionFactory) {
        return new SqlSessionTemplate(masterSqlSessionFactory);
    }


    @Bean("slaveDataSource")
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSource slaveDataSource() {
        log.info("SlaveDataSource init..................................................");
        return DruidDataSourceBuilder.create().build();
    }


    @Bean("slaveTransactionManager")
    public DataSourceTransactionManager slaveTransactionManager() {
        return new DataSourceTransactionManager(slaveDataSource());
    }


    @Bean("slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource slaveDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(slaveDataSource);
        sessionFactory.setTypeAliasesPackage("com.yintu.ruixing");
        //如果不使用xml的方式配置mapper，则可以省去下面这行mapper location的配置。
        //        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        //                .getResources("classpath:mapper/mysql/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean("slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory slaveSqlSessionFactory) {
        return new SqlSessionTemplate(slaveSqlSessionFactory);
    }

}
