package com.you.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * 用户禁用异常处理器
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */

public class ForbiddenException extends AuthenticationException {

    public ForbiddenException(String msg) {
        super(msg);
    }
}
