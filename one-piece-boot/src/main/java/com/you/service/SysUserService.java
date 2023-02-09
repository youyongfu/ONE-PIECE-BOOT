package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.entity.SysUser;

/**
 * 用户服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);
}
