package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysRole;
import com.you.service.AuthorityService;
import com.you.service.SysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 角色控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */

@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController{

    @Resource
    private AuthorityService authorityService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取角色列表
     * @return
     */
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
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreatedTime(new Date());
        sysRoleService.save(sysRole);
        return ResultBean.success(sysRole);
    }

    /**
     * 根据id获取角色
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ResultBean info(@PathVariable(name = "id") Long id) {
        return ResultBean.success(sysRoleService.getById(id));
    }

    /**
     * 更新角色
     * @param sysRole
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysRole sysRole) {
        //更新操作
        sysRole.setUpdatedTime(new Date());
        sysRoleService.updateById(sysRole);

        // 清除所有与该用户相关的权限缓存
        authorityService.clearUserAuthorityInfoByRoleId(sysRole.getId());

        return ResultBean.success(sysRole);
    }

    /**
     * 根据id删除角色
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public ResultBean delete(@PathVariable("id") Long id) {
        return sysRoleService.delete(id);
    }
}
