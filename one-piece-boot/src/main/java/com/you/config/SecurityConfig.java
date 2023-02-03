package com.you.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //白名单
    private static final String[] URL_WHITELIST = {
            "/captcha",
    };


    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()

                // 登录配置
                .formLogin()
                .and()
                .logout()

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                // 异常处理器
                .and()
                .exceptionHandling()

                // 配置自定义的过滤器
                .and()
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }
}
