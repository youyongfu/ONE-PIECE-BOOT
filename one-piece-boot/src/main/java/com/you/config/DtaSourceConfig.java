package com.you.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.you.utils.SSHConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Druid数据源 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/8
 */
@Configuration
public class DtaSourceConfig {

    @Autowired
    private SSHConnectionUtils sshConnectionUtils;

    @Bean
    public DataSource dataSource() throws Exception {
        // 建立SSH连接
        sshConnectionUtils.SSHConnection();
        return new DruidDataSource();   //创建Druid数据源
    }
}
