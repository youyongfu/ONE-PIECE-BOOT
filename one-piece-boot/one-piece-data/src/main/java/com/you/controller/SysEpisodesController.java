package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysEpisodes;
import com.you.service.SysEpisodesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 剧集控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "剧集控制层")
@RestController
@RequestMapping("/sys/episodes")
public class SysEpisodesController {

    @Resource
    private SysEpisodesService sysEpisodesService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:episodes:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysEpisodesService.listPage(keyword,current,size);
    }

    /**
     * 获取所有数据
     * @return
     */
    @ApiOperation("获取所有数据")
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('sys:episodes:list')")   //查看权限
    public ResultBean getAll(){
        return sysEpisodesService.getAll();
    }

    /**
     * 新增
     * @param sysEpisodes
     * @return
     */
    @ApiOperation("新增")
    @Transactional
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:episodes:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysEpisodes sysEpisodes) {
        return sysEpisodesService.saveEpisodes(sysEpisodes);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:episodes:list')")
    public ResultBean info(@PathVariable(name = "id") String id) {
        return sysEpisodesService.getInfoById(id);
    }

    /**
     * 更新
     * @param sysEpisodes
     * @return
     */
    @ApiOperation("更新")
    @Transactional
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:episodes:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysEpisodes sysEpisodes) {
        return sysEpisodesService.updateEpisodes(sysEpisodes);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:episodes:delete')")
    public ResultBean delete(@PathVariable("id") String id) {
        return sysEpisodesService.deleteEpisodes(id);
    }
}

