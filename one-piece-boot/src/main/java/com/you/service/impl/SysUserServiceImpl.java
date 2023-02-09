package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.entity.SysUser;
import com.you.mapper.SysUserMapper;
import com.you.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }


}