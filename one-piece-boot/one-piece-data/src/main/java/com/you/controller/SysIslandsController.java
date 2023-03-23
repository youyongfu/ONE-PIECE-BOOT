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
     * 获取树形数据
     * @return
     */
    @ApiOperation("获取树形数据")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:islands:list')")   //查看权限
    public ResultBean tree(){
        return ResultBean.success(sysIslandsService.treeList());
    }

    /**
     * 获取所有数据
     * @return
     */
    @ApiOperation("获取所有数据")
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('sys:islands:list')")   //查看权限
    public ResultBean getAll(){
        return sysIslandsService.getAll();
    }

    /**
     * 新增岛屿
     * @param sysIslands
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:islands:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysIslands sysIslands) {
        return sysIslandsService.saveIslands(sysIslands);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:islands:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return sysIslandsService.getInfoById(id);
    }

    /**
     * 更新
     * @param sysIslands
     * @return
     */
    @ApiOperation("更新")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:islands:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysIslands sysIslands) {
        return sysIslandsService.updateIslands(sysIslands);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:islands:delete')")
    public ResultBean delete(@PathVariable("id") String id) {
        return sysIslandsService.deleteIslands(id);
    }


}

