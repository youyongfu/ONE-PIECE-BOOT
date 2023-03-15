package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysFigure;
import com.you.service.SysFigureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 人物大全控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "人物大全控制层")
@RestController
@RequestMapping("/sys/figure")
public class SysFigureController {

    @Resource
    private SysFigureService sysFigureService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:figure:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysFigureService.listPage(keyword,current,size);
    }

    /**
     * 新增人物
     * @param sysFigure
     * @return
     */
    @ApiOperation("新增人物")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:figure:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysFigure sysFigure) {
        return sysFigureService.saveFigure(sysFigure);
    }

    /**
     * 根据id获取人物详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取人物详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:figure:list')")
    public ResultBean info(@ApiParam("人物id") @PathVariable(name = "id") String id) {
        return sysFigureService.getInfoById(id);
    }

    /**
     * 更新人物
     * @param sysFigure
     * @return
     */
    @ApiOperation("更新人物")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:figure:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysFigure sysFigure) {
        return sysFigureService.updateFigure(sysFigure);
    }

    /**
     * 根据id删除人物
     * @param id
     * @return
     */
    @ApiOperation("根据id删除人物")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:figure:delete')")
    public ResultBean delete(@ApiParam("人物id") @PathVariable("id") String id) {
        return sysFigureService.deleteFigure(id);
    }
}

