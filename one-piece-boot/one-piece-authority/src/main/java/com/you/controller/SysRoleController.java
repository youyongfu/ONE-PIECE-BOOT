package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysRole;
import com.you.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "角色控制层")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController{

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页获取角色列表
     * @return
     */
    @ApiOperation("分页获取角色列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:role:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysRoleService.listPage(keyword,current,size);
    }

    /**
     * 获取角色列表
     * @return
     */
    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")   //查看权限
    public ResultBean list(){
        return ResultBean.success(sysRoleService.list());
    }

    /**
     * 添加角色
     * @param sysRole
     * @return
     */
    @ApiOperation("添加角色")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysRole sysRole) {
        return sysRoleService.saveRole(sysRole);
    }

    /**
     * 根据id获取角色
     * @param id
     * @return
     */
    @ApiOperation("根据id获取角色")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return sysRoleService.getInfoById(id);
    }

    /**
     * 更新角色
     * @param sysRole
     * @return
     */
    @ApiOperation("更新角色")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysRole sysRole) {
        return sysRoleService.updateRole(sysRole);
    }

    /**
     * 根据id删除角色
     * @param ids
     * @return
     */
    @ApiOperation("根据id删除角色")
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public ResultBean delete(@RequestBody String[] ids) {
        return sysRoleService.delete(ids);
    }

    /**
     * 分配权限
     * @param id
     * @return
     */
    @ApiOperation("分配权限")
    @Transactional
    @PostMapping("/perm/{id}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public ResultBean perm(@PathVariable(name = "id") String id, @ApiParam("菜单id") @RequestBody String[] menuIds) {
        return sysRoleService.perm(id,menuIds);
    }
}
