package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysDict;
import com.you.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据字典控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Api(tags = "数据字典控制层")
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    /**
     * 分页获取一级数据字典列表
     * @return
     */
    @ApiOperation("分页获取一级数据字典列表")
    @GetMapping("/listPage")
    @PreAuthorize("hasAuthority('sys:dict:list')")   //查看权限
    public ResultBean listPage(@ApiParam("关键字") @RequestParam String keyword,
                               @ApiParam("页数") @RequestParam Integer current,
                               @ApiParam("条数") @RequestParam Integer size){
        return ResultBean.success(sysDictService.listPage(keyword,current,size));
    }

    /**
     * 获取子数据字典列表
     * @return
     */
    @ApiOperation("获取子数据字典列表")
    @GetMapping("/getChildrenList/{id}")
    @PreAuthorize("hasAuthority('sys:dict:list')")   //查看权限
    public ResultBean getChildrenList(@ApiParam("父数据字典id") @PathVariable(name = "id") Long id){
        return ResultBean.success(sysDictService.getChildrenList(id));
    }

    /**
     * 获取数据字典列表
     * @return
     */
    @ApiOperation("获取数据字典列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:dict:list')")   //查看权限
    public ResultBean list(){
        return ResultBean.success(sysDictService.treeList());
    }

    /**
     * 新增数据字典
     * @param sysDict
     * @return
     */
    @ApiOperation("新增数据字典")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:dict:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysDict sysDict) {
        return sysDictService.saveDict(sysDict);
    }

    /**
     * 根据id获取数据字典
     * @param id
     * @return
     */
    @ApiOperation("根据id获取数据字典")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public ResultBean info(@PathVariable(name = "id") Long id) {
        return ResultBean.success(sysDictService.getById(id));
    }

    /**
     * 更新数据字典
     * @param sysDict
     * @return
     */
    @ApiOperation("更新数据字典")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:dict:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysDict sysDict) {
        return sysDictService.updateDict(sysDict);
    }

    /**
     * 根据id删除数据字典
     * @param id
     * @return
     */
    @ApiOperation("根据id删除数据字典")
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public ResultBean delete(@PathVariable("id") Long id) {
        return sysDictService.delete(id);
    }

    /**
     * 根据编码获取数据字典
     * @param code
     * @return
     */
    @ApiOperation("根据编码获取数据字典")
    @GetMapping("/getListByCode")
    public ResultBean getListByCode(@ApiParam("字典编码") @RequestParam String code) {
        return sysDictService.getListByCode(code);
    }
}

