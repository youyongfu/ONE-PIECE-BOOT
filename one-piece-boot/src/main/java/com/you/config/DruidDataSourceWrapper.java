package com.you.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DruidDataSourceWrapper
 *
 * @author yyf
 * @date 2023/2/8 22:34
 */
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceWrapper extends DruidDataSource {

}
