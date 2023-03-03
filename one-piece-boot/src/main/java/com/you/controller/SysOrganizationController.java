package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysOrganization;
import com.you.service.SysOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 组织管理控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "组织管理控制层")
@RestController
@RequestMapping("/sys/organization")
public class SysOrganizationController {

    @Resource
    private SysOrganizationService sysOrganizationService;

    /**
     * 分页获取组织列表
     * @return
     */
    @ApiOperation("分页获取组织列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:organization:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return sysOrganizationService.listPage(keyword,current,size);
    }

    /**
     * 新增组织
     * @param sysOrganization
     * @return
     */
    @ApiOperation("新增组织")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:organization:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysOrganization sysOrganization) {
        //未选择上级菜单，则默认为添加目录
        if(sysOrganization.getParentId() == null){
            sysOrganization.setParentId(0L);
        }
        sysOrganization.setCreatedTime(new Date());
        sysOrganizationService.save(sysOrganization);
        return ResultBean.success(sysOrganization);
    }

    /**
     * 获取组织树形数据
     * @return
     */
    @ApiOperation("获取组织树形数据")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:organization:list')")   //查看权限
    public ResultBean tree(){
        return ResultBean.success(sysOrganizationService.treeList());
    }

    /**
     * 根据id获取组织
     * @param id
     * @return
     */
    @ApiOperation("根据id获取组织")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:organization:list')")
    public ResultBean info(@ApiParam("角色id") @PathVariable(name = "id") Long id) {
        return sysOrganizationService.getInfoById(id);
    }

    /**
     * 更新组织
     * @param sysOrganization
     * @return
     */
    @ApiOperation("更新组织")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:organization:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysOrganization sysOrganization) {
        //更新操作
        sysOrganization.setUpdatedTime(new Date());
        sysOrganizationService.updateById(sysOrganization);

        return ResultBean.success(sysOrganization);
    }
}

