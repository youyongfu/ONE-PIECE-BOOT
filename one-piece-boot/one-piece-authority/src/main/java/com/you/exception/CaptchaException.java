package com.you.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * 验证码异常处理器
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */

public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }
}
