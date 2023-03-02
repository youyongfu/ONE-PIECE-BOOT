package com.you.controller;

import cn.hutool.core.map.MapUtil;
import com.you.common.ResultBean;
import com.you.constant.UserConstant;
import com.you.dto.PasswordDto;
import com.you.entity.SysUser;
import com.you.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
@Api(tags = "用户控制层")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController{

    @Resource
    private SysUserService sysUserService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 获取登录用户信息
     * @return
     */
    @ApiOperation("获取登录用户信息")
    @GetMapping("/userInfo")
    public ResultBean userInfo(Principal principal){
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        return ResultBean.success(MapUtil.builder().put("id",sysUser.getId()).put("username",sysUser.getUsername()).put("avatar",sysUser.getAvatar()).build());
    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation("修改密码")
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
     * 分页获取用户列表
     * @return
     */
    @ApiOperation("分页获取用户列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:user:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysUserService.listPage(keyword,current,size);
    }

    /**
     * 添加用户
     * @param sysUser
     * @return
     */
    @ApiOperation("添加用户")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysUser sysUser) {

        //判断用户名是否已存在
        SysUser oldUser = sysUserService.getByUsername(sysUser.getUsername());
        if(oldUser != null){
            return ResultBean.fail("用户名已存在，无法新增！");
        }
        sysUser.setCreatedTime(new Date());
        sysUser.setPassword(bCryptPasswordEncoder.encode(UserConstant.DEFULT_PASSWORD));
        sysUserService.save(sysUser);
        return ResultBean.success(sysUser);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @ApiOperation("根据id获取用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ResultBean info(@ApiParam("用户id") @PathVariable(name = "id") Long id) {
        return sysUserService.getInfoById(id);
    }

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    @ApiOperation("更新用户")
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
     * @param ids
     * @return
     */
    @ApiOperation("根据id删除用户")
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ResultBean delete(@ApiParam("用户id") @RequestBody Long[] ids) {
        return sysUserService.delete(ids);
    }

    /**
     * 分配角色
     * @param id
     * @return
     */
    @ApiOperation("分配角色")
    @Transactional
    @PostMapping("/role/{id}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public ResultBean role(@ApiParam("用户id") @PathVariable(name = "id") Long id, @ApiParam("角色id") @RequestBody Long[] roleIds) {
        return sysUserService.role(id,roleIds);
    }

    /**
     * 重置密码
     * @return
     */
    @ApiOperation("重置密码")
    @PostMapping("/repass")
    public ResultBean repass(@ApiParam("用户id") @RequestBody Long userId){
        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setPassword(bCryptPasswordEncoder.encode(UserConstant.DEFULT_PASSWORD));
        sysUser.setUpdatedTime(new Date());
        sysUserService.updateById(sysUser);
        return ResultBean.success();
    }
}
