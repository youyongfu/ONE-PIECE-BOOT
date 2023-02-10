package com.you.controller;

import com.you.utils.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 基本控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
public class BaseController {

    @Resource
    HttpServletRequest req;

    @Resource
    RedisUtils redisUtils;

}
