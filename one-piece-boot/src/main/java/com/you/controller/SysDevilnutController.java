package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysDevilnut;
import com.you.service.SysDevilnutService;
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
 * 果实图鉴控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "果实图鉴控制层")
@RestController
@RequestMapping("/sys/devilnut")
public class SysDevilnutController {

    @Resource
    private SysDevilnutService sysDevilnutService;

    /**
     * 分页获取列表
     * @return
     */
    @ApiOperation("分页获取列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:devilnut:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysDevilnutService.listPage(keyword,current,size);
    }

    /**
     * 新增果实
     * @param sysDevilnut
     * @return
     */
    @ApiOperation("新增果实")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:devilnut:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysDevilnut sysDevilnut) {
        sysDevilnut.setCreatedTime(new Date());
        sysDevilnutService.save(sysDevilnut);
        return ResultBean.success(sysDevilnut);
    }

    /**
     * 根据id获取果实
     * @param id
     * @return
     */
    @ApiOperation("根据id获取果实")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:devilnut:list')")
    public ResultBean info(@ApiParam("果实id") @PathVariable(name = "id") Long id) {
        return sysDevilnutService.getInfoById(id);
    }

    /**
     * 更新果实
     * @param sysDevilnut
     * @return
     */
    @ApiOperation("更新果实")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:devilnut:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysDevilnut sysDevilnut) {
        //更新操作
        sysDevilnut.setUpdatedTime(new Date());
        sysDevilnutService.updateById(sysDevilnut);

        return ResultBean.success(sysDevilnut);
    }

    /**
     * 根据id删除果实
     * @param id
     * @return
     */
    @ApiOperation("根据id删除果实")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:devilnut:delete')")
    public ResultBean delete(@ApiParam("果实id") @PathVariable("id") Long id) {
        sysDevilnutService.removeById(id);
        return ResultBean.success();
    }
}

