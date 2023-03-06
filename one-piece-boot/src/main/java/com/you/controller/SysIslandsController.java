package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysIslands;
import com.you.service.SysIslandsService;
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
 * 岛屿管理控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "岛屿管理控制层")
@RestController
@RequestMapping("/sys/islands")
public class SysIslandsController {

    @Resource
    private SysIslandsService sysIslandsService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:islands:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysIslandsService.listPage(keyword,current,size);
    }

    /**
     * 新增岛屿
     * @param sysIslands
     * @return
     */
    @ApiOperation("新增岛屿")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:islands:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysIslands sysIslands) {
        //未选择所属区域，则默认为一级区域
        if(sysIslands.getParentId() == null){
            sysIslands.setParentId(0L);
        }
        sysIslands.setCreatedTime(new Date());
        sysIslandsService.save(sysIslands);
        return ResultBean.success(sysIslands);
    }

    /**
     * 根据id获取武器
     * @param id
     * @return
     */
    @ApiOperation("根据id获取武器")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:islands:list')")
    public ResultBean info(@ApiParam("岛屿id") @PathVariable(name = "id") Long id) {
        return sysIslandsService.getInfoById(id);
    }

    /**
     * 更新岛屿
     * @param sysIslands
     * @return
     */
    @ApiOperation("更新岛屿")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:islands:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysIslands sysIslands) {
        //更新操作
        sysIslands.setUpdatedTime(new Date());
        sysIslandsService.updateById(sysIslands);

        return ResultBean.success(sysIslands);
    }

    /**
     * 根据id删除岛屿
     * @param id
     * @return
     */
    @ApiOperation("根据id删除岛屿")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:islands:delete')")
    public ResultBean delete(@ApiParam("岛屿id") @PathVariable("id") Long id) {
        sysIslandsService.removeById(id);
        return ResultBean.success();
    }

    /**
     * 获取树形数据
     * @return
     */
    @ApiOperation("获取树形数据")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:islands:list')")   //查看权限
    public ResultBean tree(){
        return ResultBean.success(sysIslandsService.treeList());
    }
}

