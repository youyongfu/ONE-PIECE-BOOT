package com.you.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.you.constant.RedisConstant;
import com.you.exception.CaptchaException;
import com.you.handler.LoginFailureHandler;
import com.you.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI();

        if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
            try{
                // 校验验证码
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 交给登录失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {

        String code = httpServletRequest.getParameter("captcha");
        String key = httpServletRequest.getParameter("token");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtils.hget(RedisConstant.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 删除Redis中验证码缓存
        redisUtils.hdel(RedisConstant.CAPTCHA_KEY, key);
    }
}

