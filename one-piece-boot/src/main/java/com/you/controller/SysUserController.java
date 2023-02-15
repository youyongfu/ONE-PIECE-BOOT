package com.you.controller;

import cn.hutool.core.map.MapUtil;
import com.you.common.ResultBean;
import com.you.dto.PasswordDto;
import com.you.entity.SysUser;
import com.you.service.AuthorityService;
import com.you.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;

/**
 * 用户控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController{

    @Resource
    private SysUserService sysUserService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private AuthorityService authorityService;

    /**
     * 获取登录用户信息
     * @return
     */
    @GetMapping("/userInfo")
    public ResultBean userInfo(Principal principal){
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        return ResultBean.success(MapUtil.builder().put("id",sysUser.getId()).put("username",sysUser.getUsername()).put("avatar",sysUser.getAvatar()).build());
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping("/updatePassword")
    public ResultBean updatePassword(@Validated @RequestBody PasswordDto passwordDto,Principal principal){

        //比对旧密码
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        boolean matches = bCryptPasswordEncoder.matches(passwordDto.getCurrentPass(),sysUser.getPassword());
        if(!matches){
            return ResultBean.fail("旧密码不正确");
        }

        //更新密码
        sysUser.setPassword(bCryptPasswordEncoder.encode(passwordDto.getPassword()));
        sysUser.setUpdatedTime(new Date());
        sysUserService.updateById(sysUser);
        return ResultBean.success("密码修改成功");
    }

    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")   //查看权限
    public ResultBean list(){
        return ResultBean.success(sysUserService.list());
    }

    /**
     * 添加用户
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysUser sysUser) {
        sysUser.setCreatedTime(new Date());
        sysUserService.save(sysUser);
        return ResultBean.success(sysUser);
    }

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ResultBean info(@PathVariable(name = "id") Long id) {
        return ResultBean.success(sysUserService.getById(id));
    }

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysUser sysUser) {
        //更新操作
        sysUser.setUpdatedTime(new Date());
        sysUserService.updateById(sysUser);

        return ResultBean.success(sysUser);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ResultBean delete(@PathVariable("id") Long id) {
        return sysUserService.delete(id);
    }
}
