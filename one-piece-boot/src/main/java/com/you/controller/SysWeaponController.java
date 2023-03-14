package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysWeapon;
import com.you.service.SysWeaponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 武器大全控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "武器大全控制层")
@RestController
@RequestMapping("/sys/weapon")
public class SysWeaponController {

    @Resource
    private SysWeaponService sysWeaponService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:weapon:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysWeaponService.listPage(keyword,current,size);
    }

    /**
     * 新增
     * @param sysWeapon
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:weapon:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysWeapon sysWeapon) {
        return sysWeaponService.saveWeapon(sysWeapon);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:weapon:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return sysWeaponService.getInfoById(id);
    }

    /**
     * 更新武器
     * @param sysWeapon
     * @return
     */
    @ApiOperation("更新武器")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:weapon:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysWeapon sysWeapon) {
        return sysWeaponService.updateWeapon(sysWeapon);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:weapon:delete')")
    public ResultBean delete(@PathVariable("id") String id) {
        return sysWeaponService.deleteWeapon(id);
    }
}

