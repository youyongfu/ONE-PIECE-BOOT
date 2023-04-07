package com.you.controller;

import com.you.common.ResultBean;
import com.you.dto.PasswordDto;
import com.you.entity.SysUser;
import com.you.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;

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

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public ResultBean userInfo(Principal principal){
        return sysUserService.userInfo(principal);
    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public ResultBean updatePassword(@Validated @RequestBody PasswordDto passwordDto,Principal principal){
        return sysUserService.updatePassword(passwordDto,principal);
    }

    /**
     * 重置密码
     * @return
     */
    @ApiOperation("重置密码")
    @PostMapping("/repass")
    public ResultBean repass(String id){
        return sysUserService.rePass(id);
    }

    /**
     * 上传头像
     */
    @ApiOperation("上传头像")
    @PostMapping("/uploadAvatar")
    @PreAuthorize("hasAuthority('sys:user:upload')")
    public ResultBean uploadAvatar(MultipartFile file, Principal principal,String type) {
        return sysUserService.uploadAvatar(file,principal.getName(),type);
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
        return sysUserService.saveUser(sysUser);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @ApiOperation("根据id获取用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
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
        return sysUserService.updateUser(sysUser);
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
    public ResultBean delete(@ApiParam("用户id") @RequestBody String[] ids) {
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
    public ResultBean role(@ApiParam("用户id") @PathVariable(name = "id") String id, @ApiParam("角色id") @RequestBody String[] roleIds) {
        return sysUserService.role(id,roleIds);
    }

}
