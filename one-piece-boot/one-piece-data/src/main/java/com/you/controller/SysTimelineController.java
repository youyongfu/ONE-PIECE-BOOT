package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysTimeline;
import com.you.service.SysTimelineService;
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
 * 时间线管理控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "时间线管理控制层")
@RestController
@RequestMapping("/sys/timeline")
public class SysTimelineController {

    @Resource
    private SysTimelineService sysTimelineService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:timeline:list')")   //查看权限
    public ResultBean listPage(@ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysTimelineService.listPage(current,size);
    }

    /**
     * 新增时间线
     * @param sysTimeline
     * @return
     */
    @ApiOperation("新增时间线")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:timeline:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysTimeline sysTimeline) {
        sysTimeline.setCreatedTime(new Date());
        sysTimelineService.save(sysTimeline);
        return ResultBean.success(sysTimeline);
    }

    /**
     * 根据id获取时间线
     * @param id
     * @return
     */
    @ApiOperation("根据id获取时间线")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:timeline:list')")
    public ResultBean info(@ApiParam("时间线id") @PathVariable(name = "id") Long id) {
        return ResultBean.success(sysTimelineService.getById(id));
    }

    /**
     * 更新时间线
     * @param sysTimeline
     * @return
     */
    @ApiOperation("更新时间线")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:timeline:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysTimeline sysTimeline) {
        //更新操作
        sysTimeline.setUpdatedTime(new Date());
        sysTimelineService.updateById(sysTimeline);

        return ResultBean.success(sysTimeline);
    }

    /**
     * 根据id删除时间线
     * @param id
     * @return
     */
    @ApiOperation("根据id删除时间线")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:timeline:delete')")
    public ResultBean delete(@ApiParam("时间线id") @PathVariable("id") Long id) {
        sysTimelineService.removeById(id);
        return ResultBean.success();
    }
}

