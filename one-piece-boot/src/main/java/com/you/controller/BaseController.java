package com.you.controller;

import com.you.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 基本控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

}
