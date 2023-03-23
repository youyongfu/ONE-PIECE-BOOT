package com.you.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");           //允许任何域名使用
        corsConfiguration.addAllowedHeader("*");           //允许任何请求头
        corsConfiguration.addAllowedMethod("*");           //允许任何类型请求
        corsConfiguration.addExposedHeader("Authorization");     //校验当前登陆用户的Token是否过期，过期就让用户重新进行登录
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 设置允许跨域的路径
                .allowedOrigins("*")                // 设置允许跨域请求的域名
                .allowedMethods("GET", "POST", "DELETE", "PUT")       // 设置允许的方法
                .maxAge(3600);           // 跨域允许时间
    }

}
