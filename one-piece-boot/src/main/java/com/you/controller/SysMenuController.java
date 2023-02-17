package com.you.controller;

import cn.hutool.core.map.MapUtil;
import com.you.common.ResultBean;
import com.you.constant.MenuConstant;
import com.you.dto.SysMenuDto;
import com.you.entity.SysMenu;
import com.you.entity.SysUser;
import com.you.service.AuthorityService;
import com.you.service.SysMenuService;
import com.you.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * 用户控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController{

    @Resource
    private AuthorityService authorityService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取当前用户导航和权限信息
     * @return
     */
    @GetMapping("/nav")
    public ResultBean nav(Principal principal){

        //获取用户信息
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        //获取当前用户导航信息
        List<SysMenuDto> menuList = sysMenuService.getCurrentUserNav(sysUser.getId());

        return ResultBean.success(MapUtil.builder().put("menuList",menuList).map());
    }

    /**
     * 获取菜单列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")   //查看权限
    public ResultBean list(){
        return ResultBean.success(sysMenuService.treeList());
    }

    /**
     * 添加菜单
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysMenu sysMenu) {
        sysMenu.setCreatedTime(new Date());
        sysMenu.setStatu(MenuConstant.STATUS_ON);
        sysMenuService.save(sysMenu);
        return ResultBean.success(sysMenu);
    }

    /**
     * 根据id获取菜单
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ResultBean info(@PathVariable(name = "id") Long id) {
        return ResultBean.success(sysMenuService.getById(id));
    }

    /**
     * 更新菜单
     * @param sysMenu
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysMenu sysMenu) {
        //更新操作
        sysMenu.setUpdatedTime(new Date());
        sysMenuService.updateById(sysMenu);

        // 清除所有与该菜单相关的权限缓存
        authorityService.clearUserAuthorityInfoByMenuId(sysMenu.getId());

        return ResultBean.success(sysMenu);
    }

    /**
     * 根据id删除菜单
     * @param id
     * @return
     */
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ResultBean delete(@PathVariable("id") Long id) {
        return sysMenuService.delete(id);
    }
}
