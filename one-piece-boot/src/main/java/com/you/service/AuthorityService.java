package com.you.service;

/**
 * 权限信息服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/10
 */
public interface AuthorityService {

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username);
}
