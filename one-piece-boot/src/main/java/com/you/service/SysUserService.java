package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysUser;

/**
 * 用户服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    ResultBean delete(Long id);

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getInfoById(Long id);

    ResultBean role(Long id, Long[] roleIds);
}
