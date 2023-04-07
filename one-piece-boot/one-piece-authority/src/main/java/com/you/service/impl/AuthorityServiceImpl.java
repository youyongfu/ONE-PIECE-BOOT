package com.you.service.impl;

import com.you.entity.SysMenu;
import com.you.entity.SysRole;
import com.you.entity.SysUser;
import com.you.mapper.SysMenuMapper;
import com.you.mapper.SysRoleMapper;
import com.you.mapper.SysUserMapper;
import com.you.service.AuthorityService;
import com.you.utils.RedisUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限信息服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/10
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取用户权限信息
     * @param userId
     * @return
     */
    @Override
    public String getUserAuthorityInfo(String userId) {

        String authority = "";

        SysUser sysUser = sysUserMapper.selectById(userId);

        if (redisUtils.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            //从redis获取权限信息
            authority = (String) redisUtils.get("GrantedAuthority:" + sysUser.getUsername());
        } else {
            //获取该用户的角色信息
            List<SysRole> sysRoleList = sysRoleMapper.getRoleInfoByUserId(userId);

            //获取角色编码
            if(CollectionUtils.isNotEmpty(sysRoleList)){
                String roleCode = sysRoleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCode.concat(",");
            }

            //获取该用户的菜单权限信息
            List<SysMenu> sysMenuList = sysMenuMapper.getMenuByUserId(userId);

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

    /**
     * 删除用户权限信息
     * @param username
     */
    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtils.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(String roleId) {
        List<SysUser> userInfoList = sysUserMapper.getUserInfoByRoleId(roleId);
        userInfoList.forEach(u -> clearUserAuthorityInfo(u.getUsername()));
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(String menuId) {
        List<SysUser> userInfoList = sysUserMapper.getUserInfoByMenuId(menuId);
        userInfoList.forEach(u -> clearUserAuthorityInfo(u.getUsername()));
    }


}
