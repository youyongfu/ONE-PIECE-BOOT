package com.you.config;

import com.you.filter.CaptchaFilter;
import com.you.filter.JwtAuthenticationFilter;
import com.you.handler.JwtAccessDeniedHandler;
import com.you.handler.JwtAuthenticationEntryPoint;
import com.you.handler.LoginFailureHandler;
import com.you.handler.LoginSuccessHandler;
import com.you.service.impl.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Configuration
@EnableWebSecurity              //启用web安全
@EnableGlobalMethodSecurity(prePostEnabled = true)      //开启spring方法级安全,prePostEnabled = true 会解锁 @PreAuthorize(在方法执行前进行验证) 和 @PostAuthorize(在方法执行后进行验证) 。
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    UserDetailService userDetailService;


    /**
     * 注册密码转换器
     * @return
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    //白名单
    private static final String[] URL_WHITELIST = {
            "/captcha","/login"
    };

    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()

                // 登录配置
                .formLogin()
                .successHandler(loginSuccessHandler)         //登录成功处理
                .failureHandler(loginFailureHandler)         //登录失败处理
                .and()
                .logout()

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()           //白名单放行
                .anyRequest().authenticated()

                // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)               //认证失败处理
                .accessDeniedHandler(jwtAccessDeniedHandler)                         //权限不足处理

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())      //jwt校验
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)   //验证码校验
        ;

    }

    /**
     * 使用认证名称来查找对应的用户数据，交给Spring Security使用
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
