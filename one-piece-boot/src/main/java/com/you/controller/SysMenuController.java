package com.you.controller;

import cn.hutool.core.map.MapUtil;
import com.you.common.ResultBean;
import com.you.dto.SysMenuDto;
import com.you.entity.SysUser;
import com.you.service.SysMenuService;
import com.you.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
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
     * 获取当前用户菜单列表
     * @return
     */
    @GetMapping("/list")
    public ResultBean list(Principal principal){
        //获取用户信息
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        return ResultBean.success(sysMenuService.list(sysUser.getId()));
    }

}
