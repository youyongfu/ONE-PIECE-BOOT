package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysMenu;
import com.you.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 菜单控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "菜单控制层")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController{

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取当前用户导航和权限信息
     * @return
     */
    @ApiOperation("获取当前用户导航和权限信息")
    @GetMapping("/nav")
    public ResultBean nav(Principal principal){
        return sysMenuService.nav(principal);
    }

    /**
     * 分页获取一级菜单列表
     * @return
     */
    @ApiOperation("分页获取一级菜单列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:menu:list')")   //查看权限
    public ResultBean listPage(@ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysMenuService.listPage(current,size);
    }

    /**
     * 获取子菜单列表
     * @return
     */
    @ApiOperation("获取子菜单列表")
    @GetMapping("/getChildrenList/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")   //查看权限
    public ResultBean getChildrenList(@ApiParam("父菜单id") @PathVariable(name = "id") String id){
        return sysMenuService.getChildrenList(id);
    }

    /**
     * 获取树形数据
     * @return
     */
    @ApiOperation("获取树形数据")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:menu:list')")   //查看权限
    public ResultBean tree(){
        return ResultBean.success(sysMenuService.treeList());
    }

    /**
     * 添加菜单
     * @param sysMenu
     * @return
     */
    @Transactional
    @ApiOperation("添加菜单")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysMenu sysMenu) {
        return sysMenuService.saveMenu(sysMenu);
    }

    /**
     * 根据id获取菜单
     * @param id
     * @return
     */
    @ApiOperation("根据id获取菜单")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return ResultBean.success(sysMenuService.getById(id));
    }

    /**
     * 更新菜单
     * @param sysMenu
     * @return
     */
    @Transactional
    @ApiOperation("更新菜单")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysMenu sysMenu) {
        return sysMenuService.updateMenu(sysMenu);
    }

    /**
     * 根据id删除菜单
     * @param id
     * @return
     */
    @ApiOperation("根据id删除菜单")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ResultBean delete(@PathVariable("id") String id) {
        return sysMenuService.delete(id);
    }
}
