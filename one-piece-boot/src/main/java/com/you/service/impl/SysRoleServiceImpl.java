package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysRole;
import com.you.mapper.SysRoleMapper;
import com.you.service.AuthorityService;
import com.you.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private AuthorityService authorityService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(Long id) {
        // 清除所有与该用户相关的权限缓存
        authorityService.clearUserAuthorityInfoByRoleId(id);

        //删除角色
        removeById(id);

        //删除角色菜单关系
        sysRoleMapper.deleteRoleMenuByRoleId(id);
        //删除角色用户关系
        sysRoleMapper.deleteUserRoleByRoleId(id);

        return ResultBean.success();
    }
}
