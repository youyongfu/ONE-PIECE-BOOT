package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysShipping;
import com.you.service.SysShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 船只管理控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "船只管理控制层")
@RestController
@RequestMapping("/sys/shipping")
public class SysShippingController {

    @Resource
    private SysShippingService sysShippingService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:shipping:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysShippingService.listPage(keyword,current,size);
    }

    /**
     * 新增
     * @param sysShipping
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:shipping:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysShipping sysShipping) {
        return sysShippingService.saveShipping(sysShipping);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:shipping:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return sysShippingService.getInfoById(id);
    }

    /**
     * 更新
     * @param sysShipping
     * @return
     */
    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:shipping:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysShipping sysShipping){
        return sysShippingService.updateShipping(sysShipping);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:shipping:delete')")
    public ResultBean delete(@PathVariable("id") String id) {
        return sysShippingService.deleteShipping(id);
    }
}

