package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.entity.SysMenu;
import com.you.entity.SysRole;
import com.you.entity.SysUser;
import com.you.mapper.SysMenuMapper;
import com.you.mapper.SysUserMapper;
import com.you.service.SysRoleService;
import com.you.service.SysUserService;
import com.you.utils.RedisUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    /**
     * 获取用户权限信息
     * @param userId
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {

        String authority = "";

        SysUser sysUser = sysUserMapper.selectById(userId);

        if (redisUtils.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            //从redis获取权限信息
            authority = (String) redisUtils.get("GrantedAuthority:" + sysUser.getUsername());
        } else {
            //获取该用户的角色信息
            List<SysRole> sysRoleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            //获取角色编码
            if(CollectionUtils.isNotEmpty(sysRoleList)){
                String roleCode = sysRoleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCode.concat(",");
            }

            //获取该用户的菜单信息
            List<SysMenu> sysMenuList = sysMenuMapper.getMenuInfoByUserId(userId);

            //获取菜单授权码
            if(CollectionUtils.isNotEmpty(sysMenuList)){
                String menuPerms  = sysMenuList.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms );
            }

            //缓存权限信息到redis
            redisUtils.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60);
        }

        return authority;
    }


}