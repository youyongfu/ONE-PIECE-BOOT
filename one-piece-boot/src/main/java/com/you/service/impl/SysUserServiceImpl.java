package com.you.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysRole;
import com.you.entity.SysUser;
import com.you.entity.SysUserRole;
import com.you.mapper.SysRoleMapper;
import com.you.mapper.SysUserMapper;
import com.you.service.AuthorityService;
import com.you.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private AuthorityService authorityService;

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
     * 分页获取用户列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(String keyword, Integer current, Integer size) {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        //添加条件
        JSONObject jsonObject = JSONObject.parseObject(keyword);
        if(jsonObject.size() > 0){
            String name = jsonObject.getString("name");
            queryWrapper.like(StrUtil.isNotBlank(name), "username", name);
        }

        //分页插件
        Page<SysUser> page= new Page(current,size);

        //分页获取数据
        Page<SysUser> pageData = sysUserMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {

        SysUser sysUser = getById(id);

        //获取角色
        List<SysRole> sysRoleList = sysRoleMapper.getRoleInfoByUserId(id);
        if(CollectionUtils.isNotEmpty(sysRoleList)){
            List<Long> roleds = sysRoleList.stream().map(m -> m.getId()).collect(Collectors.toList());
            sysUser.setRoleIds(roleds);
        }

        return ResultBean.success(sysUser);
    }

    /**
     * 分配角色
     * @param id
     * @param roleIds
     * @return
     */
    @Override
    public ResultBean role(Long id, Long[] roleIds) {

        //组装前端勾选的用户角色信息
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(id);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        });

        //删除原来的用户角色关系
        sysUserMapper.deleteUserRoleByUserId(Arrays.asList(id));

        //保存现在的用户角色关系
        if(CollectionUtils.isNotEmpty(userRoleList)){
            sysUserMapper.batcSaveUserRole(userRoleList);
        }

        // 删除缓存
        SysUser sysUser = getById(id);
        authorityService.clearUserAuthorityInfo(sysUser.getUsername());

        return ResultBean.success(roleIds);
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @Override
    public ResultBean delete(Long[] ids) {

        List<Long> idList = Arrays.asList(ids);

        //删除角色
        removeByIds(idList);

        //删除角色用户关系
        sysUserMapper.deleteUserRoleByUserId(idList);

        return ResultBean.success();
    }

}