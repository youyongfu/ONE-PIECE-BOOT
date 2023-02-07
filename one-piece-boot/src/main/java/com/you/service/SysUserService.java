package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.entity.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023-02-06
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

}
