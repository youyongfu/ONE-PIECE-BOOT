package com.you.config;

import com.you.utils.SSHConnectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Druid数据源 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/8
 */
@Configuration
public class DtaSourceConfig {

    @Resource
    private SSHConnectionUtils sshConnectionUtils;

    @Bean
    public DataSource dataSource() throws Exception {
        // 建立SSH连接
        sshConnectionUtils.SSHConnection();
        return new DruidDataSourceWrapper();   //创建Druid数据源
    }
}
