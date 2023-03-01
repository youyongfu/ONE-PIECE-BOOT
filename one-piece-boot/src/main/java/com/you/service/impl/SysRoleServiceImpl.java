package com.you.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysMenu;
import com.you.entity.SysRole;
import com.you.entity.SysRoleMenu;
import com.you.mapper.SysMenuMapper;
import com.you.mapper.SysRoleMapper;
import com.you.service.AuthorityService;
import com.you.service.SysRoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
     * 分页获取角色列表
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
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }

        //分页插件
        Page<SysRole> page= new Page(current,size);

        //分页获取数据
        Page<SysRole> pageData = sysRoleMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 根据id获取角色
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {

        SysRole sysRole = getById(id);

        //获取菜单
        List<SysMenu> sysMenuList = sysMenuMapper.getMenuByRoleId(id);
        if(CollectionUtils.isNotEmpty(sysMenuList)){
            List<Long> menuIds = sysMenuList.stream().map(m -> m.getId()).collect(Collectors.toList());
            sysRole.setMenuIds(menuIds);
        }

        return ResultBean.success(sysRole);
    }

    /**
     * 分配权限
     * @param id
     * @param menuIds
     * @return
     */
    @Override
    public ResultBean perm(Long id, Long[] menuIds) {

        //组装前端勾选的角色权限信息
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(id);
            sysRoleMenu.setMenuId(menuId);
            roleMenuList.add(sysRoleMenu);
        });

        //删除原来的角色权限关系
        sysRoleMapper.deleteRoleMenuByRoleId(Arrays.asList(id));

        //保存现在的角色权限关系
        if(CollectionUtils.isNotEmpty(roleMenuList)){
            sysRoleMapper.batcSaveRoleMenu(roleMenuList);
        }

        // 清除所有与该角色相关的权限缓存
        authorityService.clearUserAuthorityInfoByRoleId(id);

        return ResultBean.success(menuIds);
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @Override
    public ResultBean delete(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);

        idList.stream().forEach(id -> {
            // 清除所有与该用户相关的权限缓存
            authorityService.clearUserAuthorityInfoByRoleId(id);
        });

        //删除角色
        removeByIds(idList);

        //删除角色菜单关系
        sysRoleMapper.deleteRoleMenuByRoleId(idList);
        //删除角色用户关系
        sysRoleMapper.deleteUserRoleByRoleId(idList);

        return ResultBean.success();
    }

}
