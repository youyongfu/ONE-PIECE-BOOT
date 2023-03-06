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
import java.util.Date;

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
     * 新增船只
     * @param sysShipping
     * @return
     */
    @ApiOperation("新增船只")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:shipping:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysShipping sysShipping) {
        sysShipping.setCreatedTime(new Date());
        sysShippingService.save(sysShipping);
        return ResultBean.success(sysShipping);
    }

    /**
     * 根据id获取船只
     * @param id
     * @return
     */
    @ApiOperation("根据id获取船只")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:shipping:list')")
    public ResultBean info(@ApiParam("果实id") @PathVariable(name = "id") Long id) {
        return sysShippingService.getInfoById(id);
    }

    /**
     * 更新船只
     * @param sysShipping
     * @return
     */
    @ApiOperation("更新船只")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:shipping:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysShipping sysShipping) {
        //更新操作
        sysShipping.setUpdatedTime(new Date());
        sysShippingService.updateById(sysShipping);

        return ResultBean.success(sysShipping);
    }

    /**
     * 根据id删除船只
     * @param id
     * @return
     */
    @ApiOperation("根据id删除船只")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:shipping:delete')")
    public ResultBean delete(@ApiParam("武器id") @PathVariable("id") Long id) {
        sysShippingService.removeById(id);
        return ResultBean.success();
    }
}

